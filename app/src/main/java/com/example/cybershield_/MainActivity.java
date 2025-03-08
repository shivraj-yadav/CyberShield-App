package com.example.cybershield_;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnFileScan, btnWebsiteSecurity, btnWifiSecurity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize buttons
        btnFileScan = findViewById(R.id.btn_file_scan);
        btnWebsiteSecurity = findViewById(R.id.btn_website_security);
        btnWifiSecurity = findViewById(R.id.btn_wifi_security);

        // Set onClickListeners
        btnFileScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(FileScanActivity.class);
            }
        });

        btnWebsiteSecurity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(WebsiteSecurityActivity.class);
            }
        });

        btnWifiSecurity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(WifiSecurityActivity.class);
            }
        });
    }

    // Method to start new activity
    private void openActivity(Class<?> activityClass) {
        Intent intent = new Intent(MainActivity.this, activityClass);
        startActivity(intent);
    }
}
