package com.example.tpjsonxml.model;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.text.SimpleDateFormat;
import java.util.Locale;

@XmlRootElement(name = "compte")
public class Compte implements Serializable {
    @XmlElement(name = "id")
    private Long id;
    @XmlElement(name = "solde")
    private double solde;
    @XmlElement(name = "dateCreation")
    private String dateCreation;
    @XmlElement(name = "type")
    private String type;
    @Override
    public String toString() {
        return "Compte{" +
                "id=" + id +
                ", solde=" + solde +
                ", dateCreation=" + dateCreation +
                ", type='" + type + '\'' +
                '}';
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public void setDateCreation(Date dateCreation) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        this.dateCreation = dateFormat.format(dateCreation);
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public double getSolde() {
        return solde;
    }

    public Long getId() {
        return id;
    }


}
