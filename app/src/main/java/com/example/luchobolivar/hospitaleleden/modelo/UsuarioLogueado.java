package com.example.luchobolivar.hospitaleleden.modelo;

/**
 * Created by Didier_Narváez on 17/05/2017.
 */

public class UsuarioLogueado {

    private static Usuario usuario;

    public UsuarioLogueado() {
        usuario = null;
    }

    public static Usuario getUsuario() {
        return usuario;
    }

    public static void setUsuario(Usuario usuario) {
        UsuarioLogueado.usuario = usuario;
    }
}
