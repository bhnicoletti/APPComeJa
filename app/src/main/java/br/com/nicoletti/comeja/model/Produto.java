package br.com.nicoletti.comeja.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicoletti on 18/04/2016.
 */
public class Produto implements Parcelable {
    private Long idProduto;
    private String nomeProduto;
    private String descricaoProduto;
    private Double valorProduto;
    private String imagemProduto;
    private Pessoa empresaProduto;
    private Categoria categoriaProduto;
    private List<Ingrediente> ingredientesProduto;
    private String statusProduto;
    private List<Tamanho> tamanhos;
    private Boolean alcoolico;

    public Long getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Long idProduto) {
        this.idProduto = idProduto;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getDescricaoProduto() {
        return descricaoProduto;
    }

    public void setDescricaoProduto(String descricaoProduto) {
        this.descricaoProduto = descricaoProduto;
    }

    public Double getValorProduto() {
        return valorProduto;
    }

    public void setValorProduto(Double valorProduto) {
        this.valorProduto = valorProduto;
    }

    public String getImagemProduto() {
        return imagemProduto;
    }

    public void setImagemProduto(String imagemProduto) {
        this.imagemProduto = imagemProduto;
    }

    public Pessoa getEmpresaProduto() {
        return empresaProduto;
    }

    public void setEmpresaProduto(Pessoa empresaProduto) {
        this.empresaProduto = empresaProduto;
    }

    public Categoria getCategoriaProduto() {
        return categoriaProduto;
    }

    public void setCategoriaProduto(Categoria categoriaProduto) {
        this.categoriaProduto = categoriaProduto;
    }

    public List<Ingrediente> getIngredientesProduto() {
        return ingredientesProduto;
    }

    public void setIngredientesProduto(List<Ingrediente> ingredientesProduto) {
        this.ingredientesProduto = ingredientesProduto;
    }

    public String getStatusProduto() {
        return statusProduto;
    }

    public void setStatusProduto(String statusProduto) {
        this.statusProduto = statusProduto;
    }

    public List<Tamanho> getTamanhos() {
        return tamanhos;
    }

    public void setTamanhos(List<Tamanho> tamanhos) {
        this.tamanhos = tamanhos;
    }

    public Boolean getAlcoolico() {
        return alcoolico;
    }

    public void setAlcoolico(Boolean alcoolico) {
        this.alcoolico = alcoolico;
    }

    public Produto() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.idProduto);
        dest.writeString(this.nomeProduto);
        dest.writeString(this.descricaoProduto);
        dest.writeValue(this.valorProduto);
        dest.writeString(this.imagemProduto);
        dest.writeParcelable(this.empresaProduto, flags);
        dest.writeParcelable(this.categoriaProduto, flags);
        dest.writeList(this.ingredientesProduto);
        dest.writeString(this.statusProduto);
        dest.writeList(this.tamanhos);
        dest.writeValue(this.alcoolico);
    }

    protected Produto(Parcel in) {
        this.idProduto = (Long) in.readValue(Long.class.getClassLoader());
        this.nomeProduto = in.readString();
        this.descricaoProduto = in.readString();
        this.valorProduto = (Double) in.readValue(Double.class.getClassLoader());
        this.imagemProduto = in.readString();
        this.empresaProduto = in.readParcelable(Pessoa.class.getClassLoader());
        this.categoriaProduto = in.readParcelable(Categoria.class.getClassLoader());
        this.ingredientesProduto = new ArrayList<Ingrediente>();
        in.readList(this.ingredientesProduto, Ingrediente.class.getClassLoader());
        this.statusProduto = in.readString();
        this.tamanhos = new ArrayList<Tamanho>();
        in.readList(this.tamanhos, Tamanho.class.getClassLoader());
        this.alcoolico = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Parcelable.Creator<Produto> CREATOR = new Parcelable.Creator<Produto>() {
        @Override
        public Produto createFromParcel(Parcel source) {
            return new Produto(source);
        }

        @Override
        public Produto[] newArray(int size) {
            return new Produto[size];
        }
    };
}
