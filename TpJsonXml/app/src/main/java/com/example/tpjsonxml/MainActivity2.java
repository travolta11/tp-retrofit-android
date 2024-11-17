package com.example.tpjsonxml;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tpjsonxml.api.ApiService;
import com.example.tpjsonxml.config.RetrofitClient;
import com.example.tpjsonxml.model.Compte;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity2 extends AppCompatActivity {
    public String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC")); // Optional: Use UTC
        return dateFormat.format(date);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        Spinner spinner = findViewById(R.id.spinner);
        EditText soldeTxt = findViewById(R.id.textInputEditText1);
        String[] items = new String[]{"EPARGNE", "COURANT"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        TextInputEditText textInputEditText = findViewById(R.id.textInputEditText1);
        TextInputLayout textInputLayout = findViewById(R.id.textInputLayout1);
        Button createButton = findViewById(R.id.button3);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputText = textInputEditText.getText().toString();
                if (TextUtils.isEmpty(inputText)) {
                    textInputLayout.setError("Ce Champs ne peux pas être vide");
                } else {
                    textInputLayout.setError(null);
                    Compte compte = new Compte();
                    compte.setSolde(Double.valueOf(soldeTxt.getText().toString()));
                    compte.setType(spinner.getSelectedItem().toString());
                    compte.setDateCreation(new Date());
                    ApiService apiService = RetrofitClient.getRetrofit().create(ApiService.class);
                    Call<Void> call = apiService.createCompte(compte);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(MainActivity2.this, "Données envoyées avec succès !", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                finish();
                            } else {
                                Toast.makeText(MainActivity2.this, "Erreur : " + response.code(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(MainActivity2.this, "Échec de la connexion : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }


}