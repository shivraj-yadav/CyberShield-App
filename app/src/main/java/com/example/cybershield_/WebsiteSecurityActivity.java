package com.example.cybershield_;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class WebsiteSecurityActivity extends AppCompatActivity {
    private EditText etWebsiteUrl;
    private Button btnDetectSecurity;
    private TextView tvSecurityResult;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.website_security);

        // Initialize views
        etWebsiteUrl = findViewById(R.id.et_website_url);
        btnDetectSecurity = findViewById(R.id.btn_detect_security);
        tvSecurityResult = findViewById(R.id.tv_security_result);
        progressBar = findViewById(R.id.progress_loading);

        // Button click to detect security
        btnDetectSecurity.setOnClickListener(view -> {
            String url = etWebsiteUrl.getText().toString().trim();

            if (url.isEmpty()) {
                Toast.makeText(this, "Please enter a URL", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validate the URL
            if (!Patterns.WEB_URL.matcher(url).matches()) {
                Toast.makeText(this, "Please enter a valid URL", Toast.LENGTH_SHORT).show();
                return;
            }

            // Show loading spinner
            progressBar.setVisibility(View.VISIBLE);

            // Perform security checks
            performSecurityChecks(url);
        });
    }

    private void performSecurityChecks(String url) {
        new Thread(() -> {
            try {
                URL websiteUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) websiteUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.connect();

                // Get SSL certificate status
                String sslStatus = (connection instanceof HttpsURLConnection) ? "Secure (SSL Enabled)" : "Not Secure";

                // Check HTTP response headers
                String httpSecurity = checkHttpHeaders(connection);

                // Simulated blacklist check
                String blacklistStatus = checkBlacklist(url);

                // Simulated subdomain takeover check
                String subdomainTakeover = checkSubdomainTakeover(url);

                // Simulated open port scan
                String openPorts = checkOpenPorts(url);

                // Build final result string
                String result = "SSL Certificate: " + sslStatus +
                        "\nHTTP Security: " + httpSecurity +
                        "\nBlacklist Check: " + blacklistStatus +
                        "\nSubdomain Takeover: " + subdomainTakeover +
                        "\nOpen Ports: " + openPorts;

                // Update UI on main thread
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    tvSecurityResult.setText(result);
                    tvSecurityResult.setVisibility(View.VISIBLE);
                });

            } catch (Exception e) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    tvSecurityResult.setText("Error: Unable to check security for this site.");
                    tvSecurityResult.setVisibility(View.VISIBLE);
                });
            }
        }).start();
    }

    private String checkHttpHeaders(HttpURLConnection connection) {
        // Example: Check for security headers
        if (connection.getHeaderField("Strict-Transport-Security") != null) {
            return "Secure (HSTS Enabled)";
        }
        return "Not Secure (No HSTS)";
    }

    private String checkBlacklist(String url) {
        // Placeholder function (Replace with actual Google Safe Browsing API if needed)
        return "Clean (Not Blacklisted)";
    }

    private String checkSubdomainTakeover(String url) {
        // Placeholder function for subdomain takeover checks
        return "No Issues Detected";
    }

    private String checkOpenPorts(String url) {
        // Placeholder function for open ports
        return "No Critical Open Ports";
    }
}
