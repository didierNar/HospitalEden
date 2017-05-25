package com.example.luchobolivar.hospitaleleden.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.luchobolivar.hospitaleleden.R;
import com.example.luchobolivar.hospitaleleden.modelo.Usuario;

import java.util.ArrayList;

/**
 * Created by Didier_Narváez on 26/04/2017.
 */

public class AdaptadorUsuario extends BaseAdapter {

    private Activity actividad;
    private ArrayList<Usuario> usuarios;

    public AdaptadorUsuario(Activity actividad, ArrayList<Usuario> usuarios){
        super();
        this.actividad = actividad;
        this.usuarios = usuarios;
    }


    @Override
    public int getCount() {
        return usuarios.size();
    }

    @Override
    public Object getItem(int i) {
        return usuarios.get(i);
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
            v = inf.inflate(R.layout.elemento_lista, null);
        }

        Usuario usuario = usuarios.get(position);

        TextView id = (TextView) v.findViewById(R.id.tvIdUser);
        id.setText(usuario.getIdentificacion()+"");

        TextView nombre = (TextView) v.findViewById(R.id.tvNombreUser);
        nombre.setText(usuario.getNombre());

        TextView tel = (TextView) v.findViewById(R.id.tvTelefono);
        tel.setText(usuario.getTelefono());

        return v;

    }

    public void clear() {
        usuarios.clear();
    }

    public void addAll (ArrayList<Usuario> usuario){
        for (int i=0; i<usuario.size();i++){
            usuarios.add(usuario.get(i));
        }
    }

}
