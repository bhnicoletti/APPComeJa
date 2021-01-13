package br.com.nicoletti.comeja.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Nicoletti on 18/04/2016.
 */
public class Ingrediente {

    private Long idIngrediente;
    private String nomeIngrediente;
    private Boolean adicional;
    private Double valorAdicional;
    private Long empresa;

    public Long getIdIngrediente() {
        return idIngrediente;
    }

    public void setIdIngrediente(Long idIngrediente) {
        this.idIngrediente = idIngrediente;
    }

    public String getNomeIngrediente() {
        return nomeIngrediente;
    }

    public void setNomeIngrediente(String nomeIngrediente) {
        this.nomeIngrediente = nomeIngrediente;
    }

    public Boolean getAdicional() {
        return adicional;
    }

    public void setAdicional(Boolean adicional) {
        this.adicional = adicional;
    }

    public Double getValorAdicional() {
        return valorAdicional;
    }

    public void setValorAdicional(Double valorAdicional) {
        this.valorAdicional = valorAdicional;
    }

    public Long getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Long empresa) {
        this.empresa = empresa;
    }


    public Ingrediente() {
    }


}