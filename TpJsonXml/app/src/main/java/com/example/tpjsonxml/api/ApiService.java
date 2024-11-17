package com.example.tpjsonxml.api;


import com.example.tpjsonxml.model.Compte;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @Headers("Accept: application/json")
    @GET("api/banque/compte")
    Call<List<Compte>> getComptesAsJson();

    @POST("api/banque/comptes")
    Call<Void> createCompte(@Body Compte compte);

    @PUT("api/banque/comptes/{id}")
    Call<Void> updateCompte(@Path("id") long compteId, @Body Compte compte);

    @DELETE("/api/banque/comptes/{id}")
    Call<Void> deleteCompte(@Path("id") long compteId);

    @Headers("Accept: application/xml")
    @GET("api/banque/compte")
    Call<List<Compte>> getComptesAsXml();
}