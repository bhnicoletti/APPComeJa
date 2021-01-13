package br.com.nicoletti.comeja.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Nicoletti on 18/04/2016.
 */
public class Endereco implements Parcelable {
    private Long idEndereco;
    private String estadoEndereco;
    private String cidadeEndereco;
    private String bairroEndereco;
    private String ruaEndereco;
    private String numeroEndereco;
    private String complementoEndereco;
    private String cepEndereco;
    private String status;
    private Long pessoa;

    public Long getIdEndereco() {
        return idEndereco;
    }

    public void setIdEndereco(Long idEndereco) {
        this.idEndereco = idEndereco;
    }

    public String getEstadoEndereco() {
        return estadoEndereco;
    }

    public void setEstadoEndereco(String estadoEndereco) {
        this.estadoEndereco = estadoEndereco;
    }

    public String getCidadeEndereco() {
        return cidadeEndereco;
    }

    public void setCidadeEndereco(String cidadeEndereco) {
        this.cidadeEndereco = cidadeEndereco;
    }

    public String getBairroEndereco() {
        return bairroEndereco;
    }

    public void setBairroEndereco(String bairroEndereco) {
        this.bairroEndereco = bairroEndereco;
    }

    public String getRuaEndereco() {
        return ruaEndereco;
    }

    public void setRuaEndereco(String ruaEndereco) {
        this.ruaEndereco = ruaEndereco;
    }

    public String getNumeroEndereco() {
        return numeroEndereco;
    }

    public void setNumeroEndereco(String numeroEndereco) {
        this.numeroEndereco = numeroEndereco;
    }

    public String getComplementoEndereco() {
        return complementoEndereco;
    }

    public void setComplementoEndereco(String complementoEndereco) {
        this.complementoEndereco = complementoEndereco;
    }

    public String getCepEndereco() {
        return cepEndereco;
    }

    public void setCepEndereco(String cepEndereco) {
        this.cepEndereco = cepEndereco;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getPessoa() {
        return pessoa;
    }

    public void setPessoa(Long pessoa) {
        this.pessoa = pessoa;
    }

    public Endereco() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.idEndereco);
        dest.writeString(this.estadoEndereco);
        dest.writeString(this.cidadeEndereco);
        dest.writeString(this.bairroEndereco);
        dest.writeString(this.ruaEndereco);
        dest.writeString(this.numeroEndereco);
        dest.writeString(this.complementoEndereco);
        dest.writeString(this.cepEndereco);
        dest.writeString(this.status);
        dest.writeValue(this.pessoa);
    }

    protected Endereco(Parcel in) {
        this.idEndereco = (Long) in.readValue(Long.class.getClassLoader());
        this.estadoEndereco = in.readString();
        this.cidadeEndereco = in.readString();
        this.bairroEndereco = in.readString();
        this.ruaEndereco = in.readString();
        this.numeroEndereco = in.readString();
        this.complementoEndereco = in.readString();
        this.cepEndereco = in.readString();
        this.status = in.readString();
        this.pessoa = (Long) in.readValue(Long.class.getClassLoader());
    }

    public static final Parcelable.Creator<Endereco> CREATOR = new Parcelable.Creator<Endereco>() {
        @Override
        public Endereco createFromParcel(Parcel source) {
            return new Endereco(source);
        }

        @Override
        public Endereco[] newArray(int size) {
            return new Endereco[size];
        }
    };
}
