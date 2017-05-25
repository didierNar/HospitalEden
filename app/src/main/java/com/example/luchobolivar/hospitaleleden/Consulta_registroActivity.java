package com.example.luchobolivar.hospitaleleden;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.luchobolivar.hospitaleleden.HttpURLConnection.HttpConnection;
import com.example.luchobolivar.hospitaleleden.modelo.DatosMedico;
import com.example.luchobolivar.hospitaleleden.modelo.DireccionIP;
import com.example.luchobolivar.hospitaleleden.modelo.HorarioMedico;
import com.example.luchobolivar.hospitaleleden.modelo.UsuarioLogueado;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Consulta_registroActivity extends AppCompatActivity {

    private Spinner tipoCita;
    private Spinner espe;
    private Spinner fechas;
    private Spinner medicos;

    private String enlaceMedicos;
    private String enlaceMedicosGenerales;
    private String enlaceHorarioMedicoGeneral;
    private String enlaceModiHoraOcu;
    private String enlaceRegistroCitas;
    private String enlaceRegimen;
    private String ip;
    private String regimen;
    private int idSedeReg;

    private int idTipoCit;
    private EditText motivoCita;
    private EditText horas;
    boolean medicosGenerales;
    private EditText sede;
    private int numId;
    private double valorRegimen;

    List<DatosMedico> datosMedico;
    List<DatosMedico> datosMedicosGenerales;
    List<HorarioMedico> horario;

    ArrayAdapter<CharSequence> adapterTipoCita;
    ArrayAdapter<CharSequence> adapterEspe;
    HttpConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityconsultaregistro);

        medicos = (Spinner) findViewById(R.id.cbMedicos);
        espe = (Spinner) findViewById(R.id.cbEspecializaciones);
        fechas = (Spinner) findViewById(R.id.cbFechas);
        motivoCita = (EditText) findViewById(R.id.etMotivoCita);

        tipoCita = (Spinner) findViewById(R.id.cbTipoCita);
        espe.setVisibility(View.GONE);

        datosMedico = new ArrayList<DatosMedico>();
        datosMedicosGenerales = new ArrayList<DatosMedico>();
        horario = new ArrayList<HorarioMedico>();


        sede = (EditText) findViewById(R.id.etSedeMedico);
        sede.setKeyListener(null);
        horas = (EditText) findViewById(R.id.etHoras);
        horas.setKeyListener(null);

        connection = new HttpConnection();
        ip = DireccionIP.getIp();


        adapterTipoCita = ArrayAdapter.createFromResource(this, R.array.tipoCita, android.R.layout.simple_spinner_item);
        adapterTipoCita.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoCita.setAdapter(adapterTipoCita);


        tipoCita.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 3 || position == 1) {
                    espe.setVisibility(View.VISIBLE);
                    especializacionesCombo();
                } else if (position == 2) {
                    espe.setVisibility(View.GONE);
                    espe.setAdapter(null);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        medicos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (medicosGenerales) {
                    for (int i = 0; i < datosMedicosGenerales.size(); i++) {
                        String nombre = medicos.getSelectedItem().toString();
                        if (nombre.equals(datosMedicosGenerales.get(i).getNombre())) {
                            numId = 0;
                            numId = datosMedicosGenerales.get(i).getNumIdentificacion();
                            sede.setText(datosMedicosGenerales.get(i).getSede());
                            horario.clear();
                            horarioMedicos();
                        }
                    }
                } else {
                    for (int i = 0; i < datosMedico.size(); i++) {
                        String nombre = medicos.getSelectedItem().toString();
                        if (nombre.equals(datosMedico.get(i).getNombre())) {
                            numId = 0;
                            numId = datosMedico.get(i).getNumIdentificacion();
                            sede.setText(datosMedico.get(i).getSede());
                            horario.clear();
                            horarioMedicos();
                        }
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        fechas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                HorarioMedico fechaSelec = (HorarioMedico) fechas.getSelectedItem();
                for (int i = 0; i < horario.size(); i++) {
                    if (fechaSelec.getId() == horario.get(i).getId()) {
                        horas.setText(horario.get(i).getHoraInicio() + "-" + horario.get(i).getHoraFin());

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    public void especializacionesCombo() {

        adapterEspe = ArrayAdapter.createFromResource(this, R.array.tipoEspe, android.R.layout.simple_spinner_item);
        adapterEspe.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        espe.setAdapter(adapterEspe);

    }

    public void tipoBusqueda(View view) {

        datosMedicosGenerales.clear();
        datosMedico.clear();
        horario.clear();
        String tipoS = tipoCita.getSelectedItem().toString();
        if (tipoS.equals("Seleccione Opcion")) {
            Toast.makeText(getApplicationContext(), "Debe seleccionar una opcion ", Toast.LENGTH_SHORT).show();
        } else {
            if (espe.getVisibility() == View.GONE || espe.getSelectedItemPosition() == 0) {
                medicosGenerales = true;
                llenarMedicosGeneral();

            } else if (espe.getSelectedItemPosition() == 1 || espe.getSelectedItemPosition() == 2 || espe.getSelectedItemPosition() == 3) {
                medicosGenerales = false;
                llenarMedicos();
            }

        }
    }


    public void llenarMedicos() {
        String especializacion = espe.getSelectedItem().toString();
        enlaceMedicos = "http://" + ip + "/serviciosWebHospital/ConsultaCitaEspe.php?espe=" + especializacion;
        new listarMedicos().execute(enlaceMedicos);


    }

    public void horarioMedicos() {
        enlaceHorarioMedicoGeneral = "http://" + ip + "/serviciosWebHospital/horariosMedico.php?numId=" + numId;
        new HorarioMedicoGen().execute(enlaceHorarioMedicoGeneral);
    }

    public void obtenerValorCita() {

        enlaceRegimen = "http://" + ip + "/serviciosWebHospital/regimen.php?numId=" + UsuarioLogueado.getUsuario().getIdentificacion();
        new regimenPaciente().execute(enlaceRegimen);

    }

    public void tipoDeCitaId() {

        String tipoCi = tipoCita.getSelectedItem().toString();
        if (tipoCi.equals("Prioritaria")) {
            idTipoCit = 1;
        } else if (tipoCi.equals("General")) {
            idTipoCit = 2;
        } else {
            idTipoCit = 3;
        }

    }

    public void obtenerIdSede() {
        String sedeReg = sede.getText().toString();
        if (medicosGenerales) {
            for (int i = 0; i< datosMedicosGenerales.size(); i++) {
                if(sedeReg.equals(datosMedicosGenerales.get(i).getSede())){
                    idSedeReg = datosMedicosGenerales.get(i).getIdSede();

                }
            }
        }else{
            for (int i = 0; i< datosMedicosGenerales.size(); i++) {
                if (sedeReg.equals(datosMedico.get(i).getSede())) {
                    idSedeReg = datosMedico.get(i).getIdSede();

                }
            }
        }

    }


    public void registrarCita(View view) {

        String motCita = motivoCita.getText().toString();
        String fechaReg = fechas.getSelectedItem().toString();
        String horaReg = horas.getText().toString();
        String[] datos = horaReg.split("-");
        String horaInReg = datos[1];
        HorarioMedico datoIdHora = (HorarioMedico) fechas.getSelectedItem();

        int id = UsuarioLogueado.getUsuario().getIdentificacion();
        String soli = "Solicitado";
        obtenerValorCita();
        double valor = valorRegimen;
        String motivo = motCita;
        tipoDeCitaId();
        int idTip = idTipoCit;
        int numIdPaciente = UsuarioLogueado.getUsuario().getIdentificacion();
        String idHisto = "1";
        String fecha_hora = fechaReg + " " + horaInReg;
        obtenerIdSede();
        int idSedeRegistro = idSedeReg;

        enlaceRegistroCitas = "http://" + ip + "/serviciosWebHospital/RegistroCita.php?soli=" + soli + "&valor=" + valor + "&motivo=" + motivo +
                "&idTip=" + idTip + "&numIdPaciente=" + numIdPaciente + "&idHisto=" + idHisto + "&fecha_hora=" + fecha_hora +
                "&idSedeRegistro=" + idSedeRegistro;

        enlaceModiHoraOcu = "http://" + ip + "/serviciosWebHospital/ocupacionHoraCita.php?id=" + datoIdHora.getId();

        new edicionHoraOcu().execute(enlaceModiHoraOcu);
        new registrarCita().execute(enlaceRegistroCitas);

    }

    class edicionHoraOcu extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            int res = obtenerDatosJSONHoraOcu(resultado);
            if (res == 1) {

            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String resultado = connection.enviarDatosGet(enlaceModiHoraOcu);
            return resultado;
        }
    }

    public int obtenerDatosJSONHoraOcu(String respuesta) {
        Log.e("Respuesta", respuesta);
        int resultado = 0;
        try {
            JSONObject json = new JSONObject(respuesta);

            String res = json.getString("registro");

            if (res.equals("1")) {
                resultado = 1;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    class registrarCita extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            int res = obtenerDatosJSONReg(resultado);
            if (res == 1) {

                Toast.makeText(getApplicationContext(), "Registro exitoso", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String resultado = connection.enviarDatosGet(enlaceRegistroCitas);
            return resultado;
        }
    }

    public int obtenerDatosJSONReg(String respuesta) {
        Log.e("Respuesta", respuesta);
        int resultado = 0;
        try {
            JSONObject json = new JSONObject(respuesta);

            String res = json.getString("registro");

            if (res.equals("1")) {
                resultado = 1;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultado;
    }


    class regimenPaciente extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            int r = obtenerDatosJSONRegimenPaciente(resultado);
            if (r > 0) {
                if (regimen.equals("Contributivo")) {
                    valorRegimen = 20000;
                } else {
                    valorRegimen = 10000;
                }

            } else {
                Toast.makeText(getApplicationContext(), "El medico no tiene horarios disponibles ", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String resultado = connection.enviarDatosGet(enlaceRegimen);
            return resultado;
        }
    }


    public int obtenerDatosJSONRegimenPaciente(String respuesta) {
        Log.e("Respuesta", respuesta);
        int resultado = 0;
        try {
            JSONArray json = new JSONArray(respuesta);
            //Verficamos que el tamaño del json sea mayor que 0
            if (json.length() > 0) {
                resultado = 1;
                for (int i = 0; i < json.length(); i++) {
                    JSONObject row = json.getJSONObject(i);
                    regimen = row.getString("DESCRIPCION");

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultado;
    }


    class HorarioMedicoGen extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            int r = obtenerDatosJSONHorarioMedico(resultado);
            if (r > 0) {
                ArrayAdapter<HorarioMedico> spinnerArrayAdapter = new ArrayAdapter<HorarioMedico>(getApplicationContext(),
                        android.R.layout.simple_spinner_item, horario);
                fechas.setAdapter(spinnerArrayAdapter);
            } else {
                Toast.makeText(getApplicationContext(), "El medico no tiene horarios disponibles ", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String resultado = connection.enviarDatosGet(enlaceHorarioMedicoGeneral);
            return resultado;
        }
    }

    public int obtenerDatosJSONHorarioMedico(String respuesta) {
        Log.e("Respuesta", respuesta);
        int resultado = 0;
        try {
            JSONArray json = new JSONArray(respuesta);
            //Verficamos que el tamaño del json sea mayor que 0
            if (json.length() > 0) {
                resultado = 1;
                for (int i = 0; i < json.length(); i++) {
                    JSONObject row = json.getJSONObject(i);

                    int id = row.getInt("ID");
                    String fechaGuardar = row.getString("FECHA");
                    String horaInicioGuar = row.getString("HORA_INICIO");
                    String horaFin = row.getString("HORA_FINAL");

                    HorarioMedico fechHora = new HorarioMedico(id, fechaGuardar, horaInicioGuar, horaFin);
                    horario.add(fechHora);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    class listarMedicos extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            int r = obtenerDatosJSONMedicos(resultado);
            if (r > 0) {
                ArrayAdapter<DatosMedico> spinnerArrayAdapter = new ArrayAdapter<DatosMedico>(getApplicationContext(),
                        android.R.layout.simple_spinner_item, datosMedico);
                medicos.setAdapter(spinnerArrayAdapter);
            } else {
                Toast.makeText(getApplicationContext(), "No hay medicos con dicha especializacion", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String resultado = connection.enviarDatosGet(enlaceMedicos);
            return resultado;
        }
    }


    public int obtenerDatosJSONMedicos(String respuesta) {
        Log.e("Respuesta", respuesta);
        int resultado = 0;
        try {
            JSONArray json = new JSONArray(respuesta);
            //Verficamos que el tamaño del json sea mayor que 0
            if (json.length() > 0) {
                resultado = 1;
                for (int i = 0; i < json.length(); i++) {
                    JSONObject row = json.getJSONObject(i);

                    int numID = row.getInt("NUMERO_IDENTIFICACION");
                    String nombre = row.getString("NOMBRE");
                    String apellido = row.getString("APELLIDO");
                    String sede = row.getString("DESCRIPCION");
                    int idSede = row.getInt("ID");

                    DatosMedico d = new DatosMedico(numID, nombre, apellido, sede, idSede);
                    datosMedico.add(d);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public void llenarMedicosGeneral() {
        enlaceMedicosGenerales = "http://" + ip + "/serviciosWebHospital/medicosCitasGenerales.php";
        new listarMedicosGenerales().execute(enlaceMedicosGenerales);
    }

    class listarMedicosGenerales extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            int r = obtenerDatosJSONMedicosGenerales(resultado);
            if (r > 0) {
                ArrayAdapter<DatosMedico> spinnerArrayAdapter = new ArrayAdapter<DatosMedico>(getApplicationContext(),
                        android.R.layout.simple_spinner_item, datosMedicosGenerales);
                medicos.setAdapter(spinnerArrayAdapter);
            } else {
                Toast.makeText(getApplicationContext(), "No hay medicos con dicha especializacion", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String resultado = connection.enviarDatosGet(enlaceMedicosGenerales);
            return resultado;
        }
    }

    public int obtenerDatosJSONMedicosGenerales(String respuesta) {
        Log.e("Respuesta", respuesta);
        int resultado = 0;
        try {
            JSONArray json = new JSONArray(respuesta);
            //Verficamos que el tamaño del json sea mayor que 0
            if (json.length() > 0) {
                resultado = 1;
                for (int i = 0; i < json.length(); i++) {
                    JSONObject row = json.getJSONObject(i);

                    int numID = row.getInt("NUMERO_IDENTIFICACION");
                    String nombre = row.getString("NOMBRE");
                    String apellido = row.getString("APELLIDO");
                    String sede = row.getString("DESCRIPCION");
                    int idSede = row.getInt("ID");

                    DatosMedico d = new DatosMedico(numID, nombre, apellido, sede, idSede);
                    datosMedicosGenerales.add(d);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultado;
    }


}
