package com.example.luchobolivar.hospitaleleden.modelo;

/**
 * Created by Didier_Narváez on 18/05/2017.
 */

public class DireccionIP {

    private static String ip;

    public static String getIp() {
        return "192.168.26.155:8888";
    }

    public static void setIp(String ip) {
        DireccionIP.ip = ip;
    }
}
