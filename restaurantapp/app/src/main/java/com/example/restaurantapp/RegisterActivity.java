package com.example.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private static final String STUDENT_ID = "10927274"; 
    private static final String API_BASE_URL = "http://10.0.2.2:8080/";

    private EditText usernameEditText, firstNameEditText, lastNameEditText, emailEditText, contactEditText, passwordEditText, confirmPasswordEditText;
    private Switch staffSwitch;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameEditText = findViewById(R.id.etUsernameRegister);
        firstNameEditText = findViewById(R.id.etFirstName);
        lastNameEditText = findViewById(R.id.etLastName);
        emailEditText = findViewById(R.id.etEmailRegister);
        contactEditText = findViewById(R.id.etContact);
        passwordEditText = findViewById(R.id.etPasswordRegister);
        confirmPasswordEditText = findViewById(R.id.etConfirmPassword);
        staffSwitch = findViewById(R.id.switchStaff);
        Button btnCreateAccount = findViewById(R.id.btnCreateAccount);
        Button btnBackToLogin = findViewById(R.id.btnBackToLogin);

        requestQueue = Volley.newRequestQueue(this);

        btnCreateAccount.setOnClickListener(v -> registerUser());
        btnBackToLogin.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void registerUser() {
        String username = usernameEditText.getText().toString().trim();
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String contact = contactEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        if (username.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || contact.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = API_BASE_URL + "comp2000/coursework/create_user/" + STUDENT_ID;

        String role = staffSwitch.isChecked() ? "staff" : "guest";

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("username", username);
            requestBody.put("password", password);
            requestBody.put("firstname", firstName);
            requestBody.put("lastname", lastName);
            requestBody.put("email", email);
            requestBody.put("contact", contact);
            requestBody.put("usertype", role);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                response -> {
                    Toast.makeText(RegisterActivity.this, "Registration successful! Please log in.", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                },
                error -> {
                    String errorMessage = "Registration failed.";
                    NetworkResponse response = error.networkResponse;
                    if (response != null && response.data != null) {
                        try {
                            String responseBody = new String(response.data, "utf-8");
                            JSONObject data = new JSONObject(responseBody);
                            errorMessage += "\n" + data.getString("detail");
                        } catch (Exception e) {
                            errorMessage += "\nCould not parse error response.";
                        }
                    } else {
                        errorMessage += "\nNo response from server.";
                    }
                    Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                });

        requestQueue.add(jsonObjectRequest);
    }
}
