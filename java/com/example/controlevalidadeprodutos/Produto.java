package com.example.controlevalidadeprodutos;

public class Produto {

    //Classe modelo responsável por gerenciar os objetos produtos

    String nome, marca, fabricacao, validade;
    int quantidade, notificacao, id, idUsuario;


    public Produto() {

    }
    //Método usado para alterar ou consultar o produto
    public Produto (int _id, String _nome, String _marca, int _quantidade, String _fabricacao, String _validade, int _notificacao , int idUsuario) {
        this.id = _id;
        this.nome = _nome;
        this.marca = _marca;
        this.quantidade = _quantidade;
        this.fabricacao = _fabricacao;
        this.validade = _validade;
        this.notificacao = _notificacao;
        this.idUsuario = idUsuario;
    }

    //Método usado para criar um produto
    public Produto (String _nome, String _marca, int _quantidade, String _fabricacao, String _validade, int _notificacao, int _idUsuario ) {
        this.nome = _nome;
        this.marca = _marca;
        this.quantidade = _quantidade;
        this.fabricacao = _fabricacao;
        this.validade = _validade;
        this.notificacao = _notificacao;
        this.idUsuario = _idUsuario;
    }


    //=============================================================//

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getNotificacao() {
        return notificacao;
    }

    public void setNotificacao(int notificacao) {
        this.notificacao = notificacao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFabricacao() {
        return fabricacao;
    }

    public void setFabricacao(String fabricacao) {
        this.fabricacao = fabricacao;
    }

    public String getValidade() {
        return validade;
    }

    public void setValidade(String validade) {
        this.validade = validade;
    }


    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}
