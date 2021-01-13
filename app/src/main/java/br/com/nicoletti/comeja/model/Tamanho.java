package br.com.nicoletti.comeja.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Nicoletti on 25/06/2016.
 */
public class Tamanho implements Parcelable {
    private Long idTamanho;
    private String tamanho;
    private Double valorTamanho;


    public Long getIdTamanho() {
        return idTamanho;
    }

    public void setIdTamanho(Long idTamanho) {
        this.idTamanho = idTamanho;
    }

    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public Double getValorTamanho() {
        return valorTamanho;
    }

    public void setValorTamanho(Double valorTamanho) {
        this.valorTamanho = valorTamanho;
    }

    public Tamanho() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.idTamanho);
        dest.writeString(this.tamanho);
        dest.writeValue(this.valorTamanho);
    }

    protected Tamanho(Parcel in) {
        this.idTamanho = (Long) in.readValue(Long.class.getClassLoader());
        this.tamanho = in.readString();
        this.valorTamanho = (Double) in.readValue(Double.class.getClassLoader());
    }

    public static final Parcelable.Creator<Tamanho> CREATOR = new Parcelable.Creator<Tamanho>() {
        @Override
        public Tamanho createFromParcel(Parcel source) {
            return new Tamanho(source);
        }

        @Override
        public Tamanho[] newArray(int size) {
            return new Tamanho[size];
        }
    };
}
