package com.example.luchobolivar.hospitaleleden.HttpURLConnection;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Didier_Narváez on 17/04/2017.
 */

public class HttpConnection {

    public String enviarDatosGet(String enlace) {

        String linea;
        int respuesta;

        //Variable que va a recibir los datos
        StringBuilder result = null;
        try {

            //instanciamos nuestra URL
            URL url = new URL(enlace);
            // realizamos la conexión con el servicio web
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //Obtenemos la respuesta de la conexión
            respuesta = connection.getResponseCode();

            result = new StringBuilder();

            //validamos la respuesta, si es igual a 200

            if (respuesta == HttpURLConnection.HTTP_OK) {
                //Obtenemos la respuesta que se trae
                InputStream in = new BufferedInputStream(connection.getInputStream());
                //Se encarga de leer la respuesta
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                //Llenamos la variable result
                while ((linea = reader.readLine()) != null) {
                    result.append(linea);
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return result.toString();

    }

}
