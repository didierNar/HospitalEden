package com.example.luchobolivar.hospitaleleden.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.luchobolivar.hospitaleleden.R;
import com.example.luchobolivar.hospitaleleden.modelo.ConsultasPacientes;
import com.example.luchobolivar.hospitaleleden.modelo.Usuario;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Didier_Narváez on 21/05/2017.
 */

public class AdaptadorUsuarioAtendido extends BaseAdapter {

    private Activity actividad;
    private List<ConsultasPacientes> datosConsulta;

    public AdaptadorUsuarioAtendido(Activity actividad, List<ConsultasPacientes> datosConsulta) {
        this.actividad = actividad;
        this.datosConsulta = datosConsulta;
    }

    @Override
    public int getCount() {
        return datosConsulta.size();
    }

    @Override
    public Object getItem(int i) {
        return datosConsulta.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    //Se construyen los diferentes layouts que serán agregados a la lista
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (convertView == null){
            LayoutInflater inf = (LayoutInflater) actividad.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.elemento_lista_usuario_atendido, null);
        }

        ConsultasPacientes dato = datosConsulta.get(position);

        TextView id = (TextView) v.findViewById(R.id.tvIdUser);
        id.setText(dato.getNombre());

        TextView nombre = (TextView) v.findViewById(R.id.tvNombreUser);
        nombre.setText(dato.getApellido());

        TextView tel = (TextView) v.findViewById(R.id.tvValorConsulta);
        tel.setText(dato.getValor()+"");

        return v;

    }

    public void clear() {
        datosConsulta.clear();
    }

    public void addAll (ArrayList<ConsultasPacientes> dato){
        for (int i=0; i<dato.size();i++){
            datosConsulta.add(dato.get(i));
        }
    }

}


