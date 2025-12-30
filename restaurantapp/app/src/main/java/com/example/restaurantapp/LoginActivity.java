package com.example.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.restaurantapp.guest.GuestHomeActivity;
import com.example.restaurantapp.staff.StaffDashboardActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    // IMPORTANT: Replace with your actual Student ID
    private static final String STUDENT_ID = "10927274"; 
    private static final String API_BASE_URL = "http://10.240.72.69/comp2000/coursework/";

    private EditText usernameEditText;
    private EditText passwordEditText;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.etUsernameLogin);
        passwordEditText = findViewById(R.id.etPasswordLogin);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnGoRegister = findViewById(R.id.btnGoRegister);

        requestQueue = Volley.newRequestQueue(this);

        btnLogin.setOnClickListener(v -> loginUser());
        btnGoRegister.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    private void loginUser() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = API_BASE_URL + "read_user/" + STUDENT_ID + "/" + username;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject user = response.getJSONObject("user");
                        String apiPassword = user.getString("password");

                        // Manually check if the password matches
                        if (password.equals(apiPassword)) {
                            String role = user.getString("usertype");
                            if ("staff".equalsIgnoreCase(role)) {
                                startActivity(new Intent(LoginActivity.this, StaffDashboardActivity.class));
                            } else {
                                startActivity(new Intent(LoginActivity.this, GuestHomeActivity.class));
                            }
                            finish(); // Close the login activity
                        } else {
                            Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(LoginActivity.this, "Invalid response from server", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    // Handle network errors (like user not found)
                    Toast.makeText(LoginActivity.this, "Login failed: User not found or network error", Toast.LENGTH_LONG).show();
                });

        requestQueue.add(jsonObjectRequest);
    }
}
