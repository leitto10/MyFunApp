package app3.com.myfunapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

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
                        Log.e(TAG, response.body().string());
                        if (response.isSuccessful()) {

                        } else {
                            alertUserAboutErroe();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "ID exception caught: ", e);
                    }
                }
            });
        }
        Log.d(TAG, "Main UI is running....");
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
