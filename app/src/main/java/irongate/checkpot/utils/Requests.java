package irongate.checkpot.utils;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

import irongate.checkpot.model.User;

/**
 * Created by Iron on 02.02.2018.
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public class Requests {
    public static final String TAG = "REQUEST";
    private static final int READ_TIMEOUT = 5000;
    private static final int CONNECTION_TIMEOUT = 5000;

    static public void getRequest(String url, GetRequestListener listener) {
        getRequest(url, listener, null);
    }
    
    static public void getRequest(String url, GetRequestListener listener, Map<String, String> headers) {
        Log.d(TAG, "GET -> " + url);
        new Thread(new GetRequestRun(url, listener, headers)).start();
    }

    static private class GetRequestRun implements Runnable {
        private URL url;
        private final GetRequestListener listener;
        private Map<String, String> headers;

        GetRequestRun(String urlString, GetRequestListener listener, Map<String, String> headers) {
            super();
            this.listener = listener;
            this.headers = headers;
            if(headers != null)
            this.headers.put("eauth", getAuthToken());
            try {
                url = new URL(urlString);
            } catch (MalformedURLException e) {
                Log.d(TAG, "GetRequestTask: invalid URL:" + urlString + e);
            }
        }

        @Override
        public void run() {
            if (url == null)
                return;

            HttpURLConnection connection;
            InputStream inputStream;
            try {
                connection = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                Log.d(TAG, "GetRequest openConnection()" + e);
                if (listener != null)
                    listener.onException(e, 0, null);
                return;
            }

            try {
                connection.setRequestMethod("GET");
            } catch (ProtocolException e) {
                Log.d(TAG, "GetRequest setRequestMethod(GET) " + e);
            }

            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);

            if (headers != null) {
                for (String key : headers.keySet()) {
                    connection.setRequestProperty(key, headers.get(key));
                }
            }

            try {
                connection.connect();
            }
            catch (SecurityException se) {
                Log.d(TAG, "GetRequest: SecurityException: " + se);
                if (listener != null)
                    listener.onException(se, 0, null);
                return;
            }
            catch (IOException ex) {
                Log.d(TAG, "GetRequest: IOException: " + ex);
                if (listener != null)
                    listener.onException(ex, 0, null);
                return;
            }

            // Если ответ не нужен
            if (listener == null)
                return;

            try {
                inputStream = connection.getInputStream();
            } catch (IOException e) {
                int status = 0;
                try {
                    status = connection.getResponseCode();
                } catch (IOException e1) {
                    Log.d(TAG, "getResponseCode:" + e1);
                }
                String message = stringFromStream(connection.getErrorStream());
                Log.d(TAG, "GET <- " + url + " ERROR responseCode:" + status + " message:" + message);
                listener.onException(e, status, message);
                return;
            }
            String response = stringFromStream(inputStream);
            Log.d(TAG, "GET <- " + url + " " + response);
            listener.onResponse(response);
        }
    }

    static public void postRequest(String url, JSONObject object, GetRequestListener listener) {
        postRequest(url, object, listener, null);
    }

    static public void postRequest(String url, JSONObject object, GetRequestListener listener, Map<String, String> headers) {
        postRequest(url, object, listener, headers, false);
    }

    static public void postRequest(String url, JSONObject object, GetRequestListener listener, Map<String, String> headers, boolean putRequest) {
        Log.d(TAG, (putRequest ? "PUT" : "POST") + " -> " + url + " ");
        new Thread(new PostRequestRun(url, object.toString(), listener, headers, putRequest)).start();
    }

    static private class PostRequestRun implements Runnable {
        private URL url;
        private String data;
        private final GetRequestListener listener;
        private final Map<String, String> headers;
        private final String method;

        PostRequestRun(String urlString, String data, GetRequestListener listener, Map<String, String> headers, boolean putRequest) {
            super();
            try {
                url = new URL(urlString);
            } catch (MalformedURLException e) {
                Log.d(TAG, "PostRequestTask: invalid URL:" + urlString + e);
            }
            this.data = data;
            this.listener = listener;
            this.headers = headers;
            if(headers != null)
            this.headers.put("eauth", getAuthToken());
            method = putRequest ? "PUT" : "POST";
        }

        @Override
        public void run() {
            if (url == null)
                return;

            HttpURLConnection connection;
            try {
                connection = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                Log.d(TAG, "PostRequest openConnection()" + e);
                if (listener != null)
                    listener.onException(e, 0, null);

                return;
            }
            try {
                connection.setRequestMethod(method);
            } catch (ProtocolException e) {
                Log.d(TAG, "PostRequest setRequestMethod(" + method+ ")" + e);
            }
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "*/*");

            if (headers != null) {
                for (String key : headers.keySet()) {
                    connection.setRequestProperty(key, headers.get(key));
                }
            }

            try {
                OutputStream os = connection.getOutputStream();
                os.write(data.getBytes("UTF-8"));
                os.close();
            } catch (IOException e) {
                if (listener != null)
                    listener.onException(e, 0, null);
                Log.d(TAG, "PostRequestRun.run() getOutputStream" + e);
            }

            try {
                connection.connect();
            }
            catch (SecurityException se) {
                Log.d(TAG, "PostRequest: SecurityException: " + se);
                if (listener != null)
                    listener.onException(se, 0, null);
                return;
            } catch (IOException e) {
                Log.d(TAG, "PostRequest: IOException " + e);
                if (listener != null)
                    listener.onException(e, 0, null);
            }

            // Если ответ не нужен
            if (listener == null)
                return;

            InputStream inputStream;
            try {
                inputStream = connection.getInputStream();
            } catch (IOException e) {
                int status = 0;
                try {
                    status = connection.getResponseCode();
                } catch (IOException e1) {
                    Log.d(TAG, "getResponseCode:" + e1);
                }
                String message = stringFromStream(connection.getErrorStream());
                Log.d(TAG, method + " <- " + url + " ERROR responseCode:" + status + " message:" + message);
                listener.onException(e, status, message);
                return;
            }
            String response = stringFromStream(inputStream);
            Log.d(TAG, method + " <- " + url + " " + response);
            listener.onResponse(response);
        }
    }

    public interface GetRequestListener {
        void onResponse(String response);
        void onException(Exception e, int status, String message);
    }

    static private String stringFromStream(InputStream stream) {
        if (stream == null)
            return null;

        InputStreamReader streamReader = new InputStreamReader(stream);
        BufferedReader reader = new BufferedReader(streamReader);
        StringBuilder stringBuilder = new StringBuilder();
        String inputLine;
        try {
            while ((inputLine = reader.readLine()) != null) {
                stringBuilder.append(inputLine);
            }
            reader.close();
        } catch (IOException e) {
            Log.d(TAG, "Requests: Reading fail: " + e);
        }
        try {
            streamReader.close();
        } catch (IOException e) {
            Log.d(TAG, "Requests: " + e);
        }
        return stringBuilder.toString();
    }

    static private String getAuthToken() {
        if(User.getUser().getAuthToken() != null)
        {
            Log.d("TARLOGS", "getAuthToken: OK! ===================>>>>>>>>>>>>>>>" + User.getUser().getAuthToken());
            return User.getUser().getAuthToken();
        } else {
            Log.d("TARLOGS", "getAuthToken: NULL! ===================>>>>>>>>>>>>>>>");
            return "-";
        }
    }
}
