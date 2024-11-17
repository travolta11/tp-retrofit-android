package com.example.tpjsonxml;

import android.content.Intent;
import android.os.Bundle;

import com.example.tpjsonxml.adapters.CompteAdapter;
import com.example.tpjsonxml.api.ApiService;
import com.example.tpjsonxml.config.RetrofitClient;
import com.example.tpjsonxml.model.Compte;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tpjsonxml.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ApiService apiService;
    private RecyclerView compteRecyclerView;
    private CompteAdapter compteAdapter;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    public ActivityResultLauncher<Intent> editActivityResultLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Button openButton = findViewById(R.id.button2);
        Spinner spinner2 = findViewById(R.id.spinner2);
        String[] items = new String[]{"JSON", "XML"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter);

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                fetchComptes("JSON");
            }
        });


        editActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    Compte updatedCompte = (Compte) data.getSerializableExtra("updatedCompte");
                    if (updatedCompte != null) {
                        fetchComptes("JSON");
                    }
                }
            }
        });


        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                activityResultLauncher.launch(intent);
            }
        });

        compteRecyclerView = findViewById(R.id.compte_recycler_view);
        compteRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        apiService = RetrofitClient.getApi();
        if (apiService == null) {
            Log.e("MainActivity", "ApiService is null!");
        } else {
            Log.d("MainActivity", "ApiService initialized successfully.");
        }
        fetchComptes("JSON");
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    fetchComptes("XML");
                } else {
                    fetchComptes("JSON");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    public void fetchComptes(String format) {
        Log.d("MainActivity", "Fetching comptes in format: " + format);
        Call<List<Compte>> call = null;
        if (format.equals("XML")) {
            call = apiService.getComptesAsXml();
        } else {
            call = apiService.getComptesAsJson();
        }
        call.enqueue(new Callback<List<Compte>>() {
            @Override
            public void onResponse(Call<List<Compte>> call, Response<List<Compte>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Compte> comptes = response.body();
                    displayComptes(comptes);
                } else {
                    Log.e("MainActivity", "Failed to fetch comptes: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Compte>> call, Throwable t) {
                Log.e("MainActivity", "Error fetching comptes: " + t.getMessage());
            }
        });
    }

    private void displayComptes(List<Compte> comptes) {
        if (compteAdapter == null) {
            compteAdapter = new CompteAdapter(comptes, this);
            compteRecyclerView.setAdapter(compteAdapter);
        } else {
            compteAdapter.updateComptes(comptes);
        }
    }
}