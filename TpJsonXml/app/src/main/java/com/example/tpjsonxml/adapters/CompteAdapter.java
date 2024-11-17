package com.example.tpjsonxml.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tpjsonxml.MainActivity;
import com.example.tpjsonxml.MainActivity3;
import com.example.tpjsonxml.R;
import com.example.tpjsonxml.api.ApiService;
import com.example.tpjsonxml.config.RetrofitClient;
import com.example.tpjsonxml.model.Compte;

import java.text.DecimalFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompteAdapter extends RecyclerView.Adapter<CompteAdapter.CompteViewHolder> {
    private List<Compte> comptes;
    private Context context;
    private ApiService apiService = RetrofitClient.getApi();
    ;


    public CompteAdapter(List<Compte> comptes, Context context) {
        this.comptes = comptes;
        this.context = context;
    }

    @NonNull
    public CompteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comptelayout, parent, false);
        return new CompteViewHolder(view);
    }

    public void onBindViewHolder(@NonNull CompteViewHolder holder, int position) {
        Compte compte = comptes.get(position);
        holder.idTextView.setText(String.valueOf("ID : " + compte.getId()));
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String formattedNumber = decimalFormat.format(compte.getSolde());
        holder.soldeTextView.setText(String.valueOf("Solde : " + formattedNumber));
        holder.creationDateTextView.setText("Date : " + compte.getDateCreation());
        holder.type.setText("Type : " + compte.getType());

        holder.buttonSupp.setOnClickListener(v -> {
            deleteCompte(compte.getId(), position);
        });

        holder.buttonModif.setOnClickListener(v -> {
            if (context instanceof MainActivity) {
                MainActivity mainActivity = (MainActivity) context;

                Intent intent = new Intent(mainActivity, MainActivity3.class);
                intent.putExtra("compte", compte);
                if (context instanceof MainActivity) {
                    ((MainActivity) context).editActivityResultLauncher.launch(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return comptes.size();
    }

    public void updateComptes(List<Compte> newComptes) {
        this.comptes = newComptes;
        notifyDataSetChanged();
    }

    private void deleteCompte(long compteId, int position) {
        apiService.deleteCompte(compteId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    comptes.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Compte deleted successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to delete compte: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    static class CompteViewHolder extends RecyclerView.ViewHolder {
        TextView idTextView, soldeTextView, creationDateTextView, type;
        Button buttonSupp, buttonModif;

        public CompteViewHolder(@NonNull View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.compte_id);
            soldeTextView = itemView.findViewById(R.id.compte_solde);
            creationDateTextView = itemView.findViewById(R.id.compte_creation_date);
            type = itemView.findViewById(R.id.type);
            buttonModif = itemView.findViewById(R.id.buttonModif);
            buttonSupp = itemView.findViewById(R.id.buttonSupp);
        }


    }
}

