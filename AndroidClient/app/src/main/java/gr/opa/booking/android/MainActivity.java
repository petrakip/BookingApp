package gr.opa.booking.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    EditText ip;
    EditText port;
    Button apply;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apply= findViewById(R.id.applyBtn);
        apply.setOnClickListener(v -> {
            ip = findViewById(R.id.masterIp);
            String masterIp = ip.getText().toString();
            Log.v("Ip: ", masterIp);

            port = findViewById(R.id.masterPort);
            int masterPort = Integer.parseInt(port.getText().toString());
            Log.v("Ip: ", port.getText().toString());

            ((ClientApp) getApplicationContext()).connectToServer(masterIp, masterPort);

            Intent nextMainMenuActivity =
                    new Intent(getApplicationContext(), MainMenu.class);
            startActivity(nextMainMenuActivity);

        });




    }

}