package br.com.nicoletti.comeja.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Nicoletti on 18/04/2016.
 */
public class Categoria implements Parcelable {
    private Long idCategoria;
    private String tituloCategoria;

    public Long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getTituloCategoria() {
        return tituloCategoria;
    }

    public void setTituloCategoria(String tituloCategoria) {
        this.tituloCategoria = tituloCategoria;
    }




    public Categoria() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.idCategoria);
        dest.writeString(this.tituloCategoria);
    }

    protected Categoria(Parcel in) {
        this.idCategoria = (Long) in.readValue(Long.class.getClassLoader());
        this.tituloCategoria = in.readString();
    }

    public static final Parcelable.Creator<Categoria> CREATOR = new Parcelable.Creator<Categoria>() {
        @Override
        public Categoria createFromParcel(Parcel source) {
            return new Categoria(source);
        }

        @Override
        public Categoria[] newArray(int size) {
            return new Categoria[size];
        }
    };
}
