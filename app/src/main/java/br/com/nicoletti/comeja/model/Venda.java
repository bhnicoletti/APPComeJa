package br.com.nicoletti.comeja.model;

import java.util.Date;
import java.util.List;

/**
 * Created by Nicoletti on 18/04/2016.
 */
public class Venda  {
    private Long idVenda;
    private Date dataVenda;
    private String statusVenda;
    private Double vlrTotalVenda;
    private Pessoa empresa;
    private Pessoa cliente;
    private Double trocoParaVenda;
    private Endereco endereco;
    private Boolean retirada;
    private List<ItemVenda> carrinho;
    private String formPagamento;
    private String dispositivo;
    private String hora;

    public Long getIdVenda() {
        return idVenda;
    }

    public void setIdVenda(Long idVenda) {
        this.idVenda = idVenda;
    }

    public Date getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(Date dataVenda) {
        this.dataVenda = dataVenda;
    }

    public String getStatusVenda() {
        return statusVenda;
    }

    public void setStatusVenda(String statusVenda) {
        this.statusVenda = statusVenda;
    }

    public Double getVlrTotalVenda() {
        return vlrTotalVenda;
    }

    public void setVlrTotalVenda(Double vlrTotalVenda) {
        this.vlrTotalVenda = vlrTotalVenda;
    }

    public Pessoa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Pessoa empresa) {
        this.empresa = empresa;
    }

    public Pessoa getCliente() {
        return cliente;
    }

    public void setCliente(Pessoa cliente) {
        this.cliente = cliente;
    }

    public Double getTrocoParaVenda() {
        return trocoParaVenda;
    }

    public void setTrocoParaVenda(Double trocoParaVenda) {
        this.trocoParaVenda = trocoParaVenda;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Boolean getRetirada() {
        return retirada;
    }

    public void setRetirada(Boolean retirada) {
        this.retirada = retirada;
    }

    public List<ItemVenda> getCarrinho() {
        return carrinho;
    }

    public void setCarrinho(List<ItemVenda> carrinho) {
        this.carrinho = carrinho;
    }

    public String getFormPagamento() {
        return formPagamento;
    }

    public void setFormPagamento(String formPagamento) {
        this.formPagamento = formPagamento;
    }

    public String getDispositivo() {
        return dispositivo;
    }

    public void setDispositivo(String dispositivo) {
        this.dispositivo = dispositivo;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Venda() {
    }


}
