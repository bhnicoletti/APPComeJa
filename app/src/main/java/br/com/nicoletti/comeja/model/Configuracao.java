package br.com.nicoletti.comeja.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Nicoletti on 21/06/2016.
 */
public class Configuracao implements Parcelable {
    private Long id;
    private Integer versaoAppAndroid;
    private Integer versaoAppIphone;
    private String tituloMensagem;
    private String mensagemMural;
    private boolean status;
    private boolean paginainicial;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersaoAppAndroid() {
        return versaoAppAndroid;
    }

    public void setVersaoAppAndroid(Integer versaoAppAndroid) {
        this.versaoAppAndroid = versaoAppAndroid;
    }

    public Integer getVersaoAppIphone() {
        return versaoAppIphone;
    }

    public void setVersaoAppIphone(Integer versaoAppIphone) {
        this.versaoAppIphone = versaoAppIphone;
    }

    public String getTituloMensagem() {
        return tituloMensagem;
    }

    public void setTituloMensagem(String tituloMensagem) {
        this.tituloMensagem = tituloMensagem;
    }

    public String getMensagemMural() {
        return mensagemMural;
    }

    public void setMensagemMural(String mensagemMural) {
        this.mensagemMural = mensagemMural;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isPaginainicial() {
        return paginainicial;
    }

    public void setPaginainicial(boolean paginainicial) {
        this.paginainicial = paginainicial;
    }


    public Configuracao() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.versaoAppAndroid);
        dest.writeValue(this.versaoAppIphone);
        dest.writeString(this.tituloMensagem);
        dest.writeString(this.mensagemMural);
        dest.writeByte(this.status ? (byte) 1 : (byte) 0);
        dest.writeByte(this.paginainicial ? (byte) 1 : (byte) 0);
    }

    protected Configuracao(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.versaoAppAndroid = (Integer) in.readValue(Integer.class.getClassLoader());
        this.versaoAppIphone = (Integer) in.readValue(Integer.class.getClassLoader());
        this.tituloMensagem = in.readString();
        this.mensagemMural = in.readString();
        this.status = in.readByte() != 0;
        this.paginainicial = in.readByte() != 0;
    }

    public static final Creator<Configuracao> CREATOR = new Creator<Configuracao>() {
        @Override
        public Configuracao createFromParcel(Parcel source) {
            return new Configuracao(source);
        }

        @Override
        public Configuracao[] newArray(int size) {
            return new Configuracao[size];
        }
    };
}
