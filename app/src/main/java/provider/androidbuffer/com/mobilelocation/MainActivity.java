package provider.androidbuffer.com.mobilelocation;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button btn;
    GPSTracker gpsTracker;
    Double latitude;
    Double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                gpsTracker = new GPSTracker(MainActivity.this);
                if (gpsTracker.canGetLocation()){

                    latitude = gpsTracker.getLatitude();
                    longitude = gpsTracker.getLongitude();

                    Log.d("data latitude",latitude+"");
                    Log.d("data longitude",longitude+"");

                    callMap();
                }
            }
        });
    }

    private void callMap(){
        String uri = String.format(Locale.ENGLISH,"geo:%f,%f",latitude,longitude);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }
}
