package br.edu.ufrgs.inf.bpm.rest;

import org.apache.http.NameValuePair;

import javax.ws.rs.core.MediaType;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class ClientConnection {

    /*
    public static boolean makeConnectionTest(String urlConnection) {
        boolean hasConnected = false;
        try {
            URL url = new URL(urlConnection);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            hasConnected = connection.getResponseCode() == HttpURLConnection.HTTP_OK;
            connection.disconnect();
        } catch (IOException ignored) {
        }
        return hasConnected;
    }
    */

    /*
    public static String makeConnectionPlainText(String urlConnection, String input) {
        return makeConnection(urlConnection, input, MediaType.TEXT_PLAIN);
    }

    public static String makeConnectionJson(String urlConnection, String input) {
        return makeConnection(urlConnection, input, MediaType.APPLICATION_JSON);
    }

    public static String makeConnectionXml(String urlConnection, String input) {
        return makeConnection(urlConnection, input, MediaType.APPLICATION_XML);
    }
    */

    public static String makeConnection(String urlConnection, String input, String mediaTypeRequest, String requestMethod) {
        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL(urlConnection);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod(requestMethod);
            connection.setRequestProperty("Content-Type", mediaTypeRequest);

            if (!input.isEmpty()) {
                OutputStream os = connection.getOutputStream();
                os.write(input.getBytes());
                os.flush();
            }

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));

            String aux;

            while ((aux = br.readLine()) != null) {
                response.append(aux).append("\n");
            }
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return response.toString();
    }

    public static String makeConnectionForm(String urlConnection, List<NameValuePair> params, String requestMethod) {
        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL(urlConnection);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod(requestMethod);
            connection.setRequestProperty("Content-Type", MediaType.APPLICATION_FORM_URLENCODED);

            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getQuery(params));
            writer.flush();
            writer.close();
            os.close();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));

            String aux;

            while ((aux = br.readLine()) != null) {
                response.append(aux).append("\n");
            }
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return response.toString();
    }

    private static String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (NameValuePair pair : params) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }
        return result.toString();
    }

}