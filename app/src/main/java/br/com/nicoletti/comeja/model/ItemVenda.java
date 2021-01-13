package br.com.nicoletti.comeja.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicoletti on 18/04/2016.
 */
public class ItemVenda implements Parcelable {
    private Long idItemVenda;
    private Double quantItemVenda;
    private Produto produtoItemVenda;
    private Double vlrItemVenda;
    private Double vlrUnitarioProduto;
    private Long vendaItemVenda;
    private List<Ingrediente> ingredientesProduto;
    private List<Ingrediente> ingredientesAdicionais;
    private List<Ingrediente> ingredientesRetirados;
    private Boolean metade;
    private String tamanho;

    public Long getIdItemVenda() {
        return idItemVenda;
    }

    public void setIdItemVenda(Long idItemVenda) {
        this.idItemVenda = idItemVenda;
    }

    public Double getQuantItemVenda() {
        return quantItemVenda;
    }

    public void setQuantItemVenda(Double quantItemVenda) {
        this.quantItemVenda = quantItemVenda;
    }

    public Produto getProdutoItemVenda() {
        return produtoItemVenda;
    }

    public void setProdutoItemVenda(Produto produtoItemVenda) {
        this.produtoItemVenda = produtoItemVenda;
    }

    public Double getVlrItemVenda() {
        return vlrItemVenda;
    }

    public void setVlrItemVenda(Double valor) {
        this.vlrItemVenda = valor;

    }

    public Long getVendaItemVenda() {
        return vendaItemVenda;
    }

    public void setVendaItemVenda(Long vendaItemVenda) {
        this.vendaItemVenda = vendaItemVenda;
    }

    public List<Ingrediente> getIngredientesProduto() {
        return ingredientesProduto;
    }

    public void setIngredientesProduto(List<Ingrediente> ingredientesProduto) {
        this.ingredientesProduto = ingredientesProduto;
    }

    public List<Ingrediente> getIngredientesAdicionais() {
        return ingredientesAdicionais;
    }

    public void setIngredientesAdicionais(List<Ingrediente> ingredientesAdicionais) {
        this.ingredientesAdicionais = ingredientesAdicionais;
    }

    public Double getVlrUnitarioProduto() {
        return vlrUnitarioProduto;
    }

    public void setVlrUnitarioProduto(Double vlrUnitarioProduto) {
        this.vlrUnitarioProduto = vlrUnitarioProduto;
    }

    public Boolean getMetade() {
        return metade;
    }

    public void setMetade(Boolean metade) {
        this.metade = metade;
    }

    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public List<Ingrediente> getIngredientesRetirados() {
        return ingredientesRetirados;
    }

    public void setIngredientesRetirados(List<Ingrediente> ingredientesRetirados) {
        this.ingredientesRetirados = ingredientesRetirados;
    }

    public ItemVenda() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.idItemVenda);
        dest.writeValue(this.quantItemVenda);
        dest.writeParcelable(this.produtoItemVenda, flags);
        dest.writeValue(this.vlrItemVenda);
        dest.writeValue(this.vlrUnitarioProduto);
        dest.writeValue(this.vendaItemVenda);
        dest.writeList(this.ingredientesProduto);
        dest.writeList(this.ingredientesAdicionais);
        dest.writeList(this.ingredientesRetirados);
        dest.writeValue(this.metade);
        dest.writeString(this.tamanho);
    }

    protected ItemVenda(Parcel in) {
        this.idItemVenda = (Long) in.readValue(Long.class.getClassLoader());
        this.quantItemVenda = (Double) in.readValue(Double.class.getClassLoader());
        this.produtoItemVenda = in.readParcelable(Produto.class.getClassLoader());
        this.vlrItemVenda = (Double) in.readValue(Double.class.getClassLoader());
        this.vlrUnitarioProduto = (Double) in.readValue(Double.class.getClassLoader());
        this.vendaItemVenda = (Long) in.readValue(Long.class.getClassLoader());
        this.ingredientesProduto = new ArrayList<Ingrediente>();
        in.readList(this.ingredientesProduto, Ingrediente.class.getClassLoader());
        this.ingredientesAdicionais = new ArrayList<Ingrediente>();
        in.readList(this.ingredientesAdicionais, Ingrediente.class.getClassLoader());
        this.ingredientesRetirados = new ArrayList<Ingrediente>();
        in.readList(this.ingredientesRetirados, Ingrediente.class.getClassLoader());
        this.metade = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.tamanho = in.readString();
    }

    public static final Parcelable.Creator<ItemVenda> CREATOR = new Parcelable.Creator<ItemVenda>() {
        @Override
        public ItemVenda createFromParcel(Parcel source) {
            return new ItemVenda(source);
        }

        @Override
        public ItemVenda[] newArray(int size) {
            return new ItemVenda[size];
        }
    };
}
