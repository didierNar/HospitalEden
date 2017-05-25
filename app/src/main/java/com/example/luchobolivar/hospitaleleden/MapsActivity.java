package com.example.luchobolivar.hospitaleleden;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;


import com.example.luchobolivar.hospitaleleden.HttpURLConnection.HttpConnection;
import com.example.luchobolivar.hospitaleleden.modelo.DireccionIP;
import com.example.luchobolivar.hospitaleleden.modelo.Sede;
import com.example.luchobolivar.hospitaleleden.modelo.Usuario;
import com.example.luchobolivar.hospitaleleden.modelo.UsuarioLogueado;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private MapView mvMapa;
    private HttpConnection conection;

    String enlace;
    String enlaceLugarCita;

    LatLng miPosicion;
    private String ip;
    private ArrayList<Sede> sedes;

    private Marker markerPosicion;
    private Usuario logueado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mvMapa = (MapView) findViewById(R.id.mvMapa);
        mvMapa.onCreate(savedInstanceState);

        sedes = new ArrayList<Sede>();
        logueado = UsuarioLogueado.getUsuario();

        ip = DireccionIP.getIp();

        mvMapa.getMapAsync(this);
        conection = new HttpConnection();
        enlace = "http://"+ip+"/serviciosWebHospital/listarSedes.php?";
        new listarSedes().execute(enlace);

        enlaceLugarCita = "http://"+ip+"/serviciosWebHospital/UbicacionCitasUsuario.php?USUARIO_NUMERO_IDENTIFICACION="+
        UsuarioLogueado.getUsuario().getIdentificacion();

        new poliLineas().execute(enlaceLugarCita);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Location location = obtenerLocation();

        double latitud = location.getLatitude();
        double longitud = location.getLongitude();

        // Add a marker in Sydney and move the camera
        //LatLng miPosicion = new LatLng(4.540878,-75.6673385);
        miPosicion = new LatLng(latitud, longitud);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //El metodo .icon() nos sirve para colocar el icono qe deseemos
        markerPosicion = mMap.addMarker(new MarkerOptions().position(miPosicion).title("Mi ubicacion"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(miPosicion, 15));

        mMap.setOnMarkerClickListener(this);

    }

    public Location obtenerLocation() {
        //Objeto que utilizamos para trabajar en el servicio d ela ubicacion
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //la ubicacion que obtenermo es por medio de la red de internet
        Location myLocation = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (myLocation == null) {
            //permite crear los criterios para traajar con el proveedor de localizacion
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            //le mandamos al objeto manager el provedor que vamos atrabajar deacuerdo al criterio que definimos
            String provider = lm.getBestProvider(criteria, true);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                return myLocation;

            }
            myLocation = lm.getLastKnownLocation(provider);
        }
        return myLocation;
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        if(marker.getTitle().equals(markerPosicion.getTitle().toString())){
            Toast.makeText(MapsActivity.this,marker.getTitle(), Toast.LENGTH_LONG).show();
        }
        return false;
    }

    public int obtenerDatosJSONPoli(String respuesta){
        Log.e("respuesta",respuesta);
        int resultado = 0;
        try{
            JSONArray json = new JSONArray(respuesta);
            if (json.length()>0){
                resultado = 1;
            }
            for(int i=0;i<json.length(); i++){
                JSONObject row = json.getJSONObject(i);

                double latitud = row.getDouble("LATITUD");
                double longitud = row.getDouble("LONGITUD");

                LatLng posSede = new LatLng(latitud, longitud);

                PolylineOptions rectOptions = new PolylineOptions().add(miPosicion, posSede);
                mMap.addPolyline(rectOptions.width(10).color(Color.RED));

            }

        } catch (JSONException e){
            e.printStackTrace();
        }
        return resultado;
    }

    class poliLineas extends AsyncTask<String, Float, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String resultado = conection.enviarDatosGet(enlaceLugarCita);
            return resultado;
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);

            int r = obtenerDatosJSONPoli(resultado);
            if(r==0){
                Toast.makeText(getApplicationContext(),"No hay citas registradas", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(),"Rutas para las citas", Toast.LENGTH_LONG).show();
            }

        }
    }


    public int obtenerDatosJSON(String respuesta){
        Log.e("respuesta",respuesta);
        int resultado = 0;
        try{
            JSONArray json = new JSONArray(respuesta);
            sedes = new ArrayList<Sede>();
            if (json.length()>0){
                resultado = 1;
            }
            for(int i=0;i<json.length(); i++){
                JSONObject row = json.getJSONObject(i);
                int id = row.getInt("ID");
                String desc = row.getString("DESCRIPCION");
                int eps = row.getInt("EPS_ID");
                double latitud = row.getDouble("LATITUD");
                double longitud = row.getDouble("LONGITUD");
                int ciudad = row.getInt("CIUDAD_ID");

                sedes.add(new Sede(id,desc,eps,latitud,longitud, ciudad));
            }

        } catch (JSONException e){
            e.printStackTrace();
        }
        return resultado;
    }

    class listarSedes extends AsyncTask<String, Float, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String resultado = conection.enviarDatosGet(enlace);
            return resultado;
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);

            int r = obtenerDatosJSON(resultado);
            if(r==0){
                Toast.makeText(getApplicationContext(),"No hay sedes registradas", Toast.LENGTH_LONG).show();
            }else{
                agregarSedes();
            }

        }
    }

    public void agregarSedes(){

        for (int i=0; i<sedes.size(); i++){
            LatLng posicion = new LatLng(sedes.get(i).getLatitud(), sedes.get(i).getLongitud());
            mMap.addMarker(new MarkerOptions().position(posicion).title(sedes.get(i).getDescripcion()));
        }

    }

    protected void onDestroy() {
        super.onDestroy();
        mvMapa.onDestroy();
    }

    protected void onPause() {
        super.onPause();
        mvMapa.onPause();
    }

    protected void onResume() {
        super.onResume();
        mvMapa.onResume();
    }

}
