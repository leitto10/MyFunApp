package app3.com.myfunapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private CurrentWeather currentWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String apiKey = "649470020dd9cb27f76d7dc1b5ccc2bf";
        double latitud = 37.8267;
        double longitud = -122.4233;

        String forcastURL = "https://api.darksky.net/forecast/" +
                apiKey + "/" + latitud + "," + longitud;

        if(isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(forcastURL)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    try {
                        String jsonData = response.body().string();
                        Log.e(TAG, jsonData);
                        if (response.isSuccessful()) {
                            currentWeather = getCurrentDetails(jsonData);
                        } else {
                            alertUserAboutErroe();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "ID exception caught: ", e);
                    } catch (JSONException e){
                        Log.e(TAG, "JSON Exception caught", e);
                    }
                }
            });
        }
        Log.d(TAG, "Main UI is running....");
    }

    private CurrentWeather getCurrentDetails(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);

        String timezone = forecast.getString("timezone");
        Log.i(TAG, "FROM JSON: " + timezone);

        JSONObject currently = forecast.getJSONObject("currently");
        CurrentWeather currentWeather = new CurrentWeather();

        currentWeather.setHumidity(currently.getDouble("humidity"));
        currentWeather.setTime(currently.getLong("time"));
        currentWeather.setIcon(currently.getString("icon"));
        currentWeather.setLocationLabel("Alcatraz Island, CA");
        currentWeather.setPrecipChange(currently.getDouble("precipProbability"));
        currentWeather.setSummary(currently.getString("summary"));
        currentWeather.setTemperature(currently.getDouble("temperature"));
        currentWeather.setTimeZone(timezone);

        Log.d(TAG, currentWeather.getFormattedTime());

        return currentWeather;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isAvailable = false;

        if(networkInfo != null && networkInfo.isConnected()){
            isAvailable = true;
        }else{
            Toast.makeText(this, "Sorry, the network is unavailable.", Toast.LENGTH_LONG).show();

        }
        return isAvailable;
    }

    private void alertUserAboutErroe() {
        AlertDialogeFragment dialog = new AlertDialogeFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }
}
