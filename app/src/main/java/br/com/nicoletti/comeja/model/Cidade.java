package br.com.nicoletti.comeja.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Nicoletti on 21/07/2016.
 */
public class Cidade implements Parcelable {
    private Long id;
    private String nome;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public Cidade() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.nome);
    }

    protected Cidade(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.nome = in.readString();
    }

    public static final Parcelable.Creator<Cidade> CREATOR = new Parcelable.Creator<Cidade>() {
        @Override
        public Cidade createFromParcel(Parcel source) {
            return new Cidade(source);
        }

        @Override
        public Cidade[] newArray(int size) {
            return new Cidade[size];
        }
    };
}
