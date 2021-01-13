package br.com.nicoletti.comeja.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Nicoletti on 18/04/2016.
 */
public class Pessoa implements Parcelable {

    private Long idPessoa;
    private String nomeFantasiaPessoa;
    private String imagem;
    private String razaoSocialPessoa;
    private String senhaPessoa;
    private String telefonePessoa;
    private Date dataNascPessoa;
    private String celularPessoa;
    private String emailPessoa;
    private String statusPessoa;
    private String tipoPessoa;
    private String cpfCnpjPessoa;
    private String rgIePessoa;
    private List<Endereco> enderecos;
    private String categoriaPessoa;
    private String tempoPreparo;
    private Double valorEntrega;
    private List<Categoria> categorias;
    private Cidade cidade;
    private String horarioFuncionamento;
    private String obs;
    private List<FormaPagamento> listaFormaPagamento;

    public Long getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(Long idPessoa) {
        this.idPessoa = idPessoa;
    }

    public String getNomeFantasiaPessoa() {
        return nomeFantasiaPessoa;
    }

    public void setNomeFantasiaPessoa(String nomeFantasiaPessoa) {
        this.nomeFantasiaPessoa = nomeFantasiaPessoa;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getRazaoSocialPessoa() {
        return razaoSocialPessoa;
    }

    public void setRazaoSocialPessoa(String razaoSocialPessoa) {
        this.razaoSocialPessoa = razaoSocialPessoa;
    }

    public String getSenhaPessoa() {
        return senhaPessoa;
    }

    public void setSenhaPessoa(String senhaPessoa) {
        this.senhaPessoa = senhaPessoa;
    }

    public String getTelefonePessoa() {
        return telefonePessoa;
    }

    public void setTelefonePessoa(String telefonePessoa) {
        this.telefonePessoa = telefonePessoa;
    }

    public Date getDataNascPessoa() {
        return dataNascPessoa;
    }

    public void setDataNascPessoa(Date dataNascPessoa) {
        this.dataNascPessoa = dataNascPessoa;
    }

    public String getCelularPessoa() {
        return celularPessoa;
    }

    public void setCelularPessoa(String celularPessoa) {
        this.celularPessoa = celularPessoa;
    }

    public String getEmailPessoa() {
        return emailPessoa;
    }

    public void setEmailPessoa(String emailPessoa) {
        this.emailPessoa = emailPessoa;
    }

    public String getStatusPessoa() {
        return statusPessoa;
    }

    public void setStatusPessoa(String statusPessoa) {
        this.statusPessoa = statusPessoa;
    }

    public String getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(String tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    public String getCpfCnpjPessoa() {
        return cpfCnpjPessoa;
    }

    public void setCpfCnpjPessoa(String cpfCnpjPessoa) {
        this.cpfCnpjPessoa = cpfCnpjPessoa;
    }

    public String getRgIePessoa() {
        return rgIePessoa;
    }

    public void setRgIePessoa(String rgIePessoa) {
        this.rgIePessoa = rgIePessoa;
    }

    public List<Endereco> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<Endereco> enderecos) {
        this.enderecos = enderecos;
    }

    public String getCategoriaPessoa() {
        return categoriaPessoa;
    }

    public void setCategoriaPessoa(String categoriaPessoa) {
        this.categoriaPessoa = categoriaPessoa;
    }

    public String getTempoPreparo() {
        return tempoPreparo;
    }

    public void setTempoPreparo(String tempoPreparo) {
        this.tempoPreparo = tempoPreparo;
    }

    public Double getValorEntrega() {
        return valorEntrega;
    }

    public void setValorEntrega(Double valorEntrega) {
        this.valorEntrega = valorEntrega;
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public String getHorarioFuncionamento() {
        return horarioFuncionamento;
    }

    public void setHorarioFuncionamento(String horarioFuncionamento) {
        this.horarioFuncionamento = horarioFuncionamento;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public List<FormaPagamento> getListaFormaPagamento() {
        return listaFormaPagamento;
    }

    public void setListaFormaPagamento(List<FormaPagamento> listaFormaPagamento) {
        this.listaFormaPagamento = listaFormaPagamento;
    }

    public Pessoa() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.idPessoa);
        dest.writeString(this.nomeFantasiaPessoa);
        dest.writeString(this.imagem);
        dest.writeString(this.razaoSocialPessoa);
        dest.writeString(this.senhaPessoa);
        dest.writeString(this.telefonePessoa);
        dest.writeLong(this.dataNascPessoa != null ? this.dataNascPessoa.getTime() : -1);
        dest.writeString(this.celularPessoa);
        dest.writeString(this.emailPessoa);
        dest.writeString(this.statusPessoa);
        dest.writeString(this.tipoPessoa);
        dest.writeString(this.cpfCnpjPessoa);
        dest.writeString(this.rgIePessoa);
        dest.writeList(this.enderecos);
        dest.writeString(this.categoriaPessoa);
        dest.writeString(this.tempoPreparo);
        dest.writeValue(this.valorEntrega);
        dest.writeTypedList(this.categorias);
        dest.writeParcelable(this.cidade, flags);
        dest.writeString(this.horarioFuncionamento);
        dest.writeString(this.obs);
        dest.writeList(this.listaFormaPagamento);
    }

    protected Pessoa(Parcel in) {
        this.idPessoa = (Long) in.readValue(Long.class.getClassLoader());
        this.nomeFantasiaPessoa = in.readString();
        this.imagem = in.readString();
        this.razaoSocialPessoa = in.readString();
        this.senhaPessoa = in.readString();
        this.telefonePessoa = in.readString();
        long tmpDataNascPessoa = in.readLong();
        this.dataNascPessoa = tmpDataNascPessoa == -1 ? null : new Date(tmpDataNascPessoa);
        this.celularPessoa = in.readString();
        this.emailPessoa = in.readString();
        this.statusPessoa = in.readString();
        this.tipoPessoa = in.readString();
        this.cpfCnpjPessoa = in.readString();
        this.rgIePessoa = in.readString();
        this.enderecos = new ArrayList<Endereco>();
        in.readList(this.enderecos, Endereco.class.getClassLoader());
        this.categoriaPessoa = in.readString();
        this.tempoPreparo = in.readString();
        this.valorEntrega = (Double) in.readValue(Double.class.getClassLoader());
        this.categorias = in.createTypedArrayList(Categoria.CREATOR);
        this.cidade = in.readParcelable(Cidade.class.getClassLoader());
        this.horarioFuncionamento = in.readString();
        this.obs = in.readString();
        this.listaFormaPagamento = new ArrayList<FormaPagamento>();
        in.readList(this.listaFormaPagamento, FormaPagamento.class.getClassLoader());
    }

    public static final Parcelable.Creator<Pessoa> CREATOR = new Parcelable.Creator<Pessoa>() {
        @Override
        public Pessoa createFromParcel(Parcel source) {
            return new Pessoa(source);
        }

        @Override
        public Pessoa[] newArray(int size) {
            return new Pessoa[size];
        }
    };
}
