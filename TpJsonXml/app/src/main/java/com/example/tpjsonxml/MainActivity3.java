package com.example.tpjsonxml;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tpjsonxml.api.ApiService;
import com.example.tpjsonxml.config.RetrofitClient;
import com.example.tpjsonxml.model.Compte;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity3 extends AppCompatActivity {
    private EditText editText;
    private Spinner spinnerUpdate;
    private Button updateBtn;
    private Compte compte;

    private ApiService apiService = RetrofitClient.getApi();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        editText = findViewById(R.id.editText);
        spinnerUpdate = findViewById(R.id.spinnerUpdate);
        updateBtn = findViewById(R.id.updateBtn);
        String[] items = new String[]{"EPARGNE", "COURANT"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUpdate.setAdapter(adapter);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("compte")) {
            compte = (Compte) intent.getSerializableExtra("compte");
            if (compte != null) {
                editText.setText(String.valueOf(compte.getSolde()));
                int spinnerPosition = adapter.getPosition(compte.getType());
                spinnerUpdate.setSelection(spinnerPosition);
            }
        }
        updateBtn.setOnClickListener(v -> {
            double updatedSolde = Double.parseDouble(editText.getText().toString());
            String updatedType = spinnerUpdate.getSelectedItem().toString();

            compte.setSolde(updatedSolde);
            compte.setType(updatedType);

            apiService.updateCompte(compte.getId(), compte).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("updatedCompte", compte);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    } else {
                        Toast.makeText(MainActivity3.this, "Failed to update compte: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(MainActivity3.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

    }
}