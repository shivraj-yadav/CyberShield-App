package com.example.cybershield_;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class WifiSecurityActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST = 100;
    private WifiManager wifiManager;
    private TextView tvWifiName, tvSignalStrength, tvEncryptionType, tvSecurityStatus, tvIpAddress, tvDnsServer, tvGateway;
    private ProgressBar signalStrengthBar;
    private ScrollView scrollWifiInfo;
    private Button btnScanWifiSecurity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_security);

        // Initialize UI components
        btnScanWifiSecurity = findViewById(R.id.btnScanWifiSecurity);
        tvWifiName = findViewById(R.id.tvWifiName);
        tvSignalStrength = findViewById(R.id.tvSignalStrength);
        tvEncryptionType = findViewById(R.id.tvEncryptionType);
        tvSecurityStatus = findViewById(R.id.tvSecurityStatus);
        tvIpAddress = findViewById(R.id.tvIpAddress);
        tvDnsServer = findViewById(R.id.tvDnsServer);
        tvGateway = findViewById(R.id.tvGateway);
        signalStrengthBar = findViewById(R.id.signalStrengthBar);
        scrollWifiInfo = findViewById(R.id.scrollWifiInfo);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        // Initially hide the ScrollView
        scrollWifiInfo.setVisibility(View.GONE);

        // Handle button click
        btnScanWifiSecurity.setOnClickListener(v -> {
            // Check permission before scanning
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
            } else {
                scrollWifiInfo.setVisibility(View.VISIBLE); // Show details on button click
                updateWifiDetails();
            }
        });
    }

    private void updateWifiDetails() {
        if (wifiManager == null || !wifiManager.isWifiEnabled()) {
            tvWifiName.setText("WiFi Disabled");
            return;
        }

        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo != null) {
            tvWifiName.setText("WiFi Name: " + wifiInfo.getSSID());
            tvSignalStrength.setText("Signal Strength: " + wifiInfo.getRssi() + " dBm");
            signalStrengthBar.setProgress(wifiInfo.getRssi() + 100);

            tvIpAddress.setText("IP Address: " + formatIpAddress(wifiInfo.getIpAddress()));
        }

        // Get security type
        String securityType = getSecurityType(wifiInfo.getSSID());
        tvEncryptionType.setText("Encryption: " + securityType);
        tvSecurityStatus.setText(securityType.equals("Open") ? "⚠️ Unsecured" : "✅ Secured");

        // Get DHCP info for DNS and Gateway
        DhcpInfo dhcp = wifiManager.getDhcpInfo();
        if (dhcp != null) {
            tvDnsServer.setText("DNS Server: " + formatIpAddress(dhcp.dns1));
            tvGateway.setText("Gateway: " + formatIpAddress(dhcp.gateway));
        }
    }

    private String getSecurityType(String ssid) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return "Permission Required";
        }

        List<ScanResult> scanResults = wifiManager.getScanResults();
        for (ScanResult scanResult : scanResults) {
            if (scanResult.SSID.equals(ssid)) {
                String capabilities = scanResult.capabilities;
                if (capabilities.contains("WPA")) return "WPA/WPA2";
                if (capabilities.contains("WEP")) return "WEP";
                return "Open";
            }
        }
        return "Unknown";
    }

    private String formatIpAddress(int ip) {
        try {
            return InetAddress.getByAddress(new byte[]{
                    (byte) (ip & 0xFF),
                    (byte) ((ip >> 8) & 0xFF),
                    (byte) ((ip >> 16) & 0xFF),
                    (byte) ((ip >> 24) & 0xFF)
            }).getHostAddress();
        } catch (UnknownHostException e) {
            return "N/A";
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                scrollWifiInfo.setVisibility(View.VISIBLE);
                updateWifiDetails();
            } else {
                tvSecurityStatus.setText("Location Permission Denied ❌");
            }
        }
    }
}
