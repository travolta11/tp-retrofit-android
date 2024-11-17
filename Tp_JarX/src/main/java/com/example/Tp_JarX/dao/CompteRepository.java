package com.example.Tp_JarX.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Tp_JarX.entities.Compte;

@Repository
public interface CompteRepository extends JpaRepository<Compte,Long> {

}
