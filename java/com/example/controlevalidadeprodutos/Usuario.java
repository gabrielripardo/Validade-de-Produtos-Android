package com.example.controlevalidadeprodutos;

public class Usuario {

    //Classe modelo responsável por gerenciar os objetos usuários
    String usuario, senha, genero;
    int id;


    public Usuario() {

    }

    public Usuario (int _id, String _usuario, String _senha, String _genero) {
        this.id = _id;
        this.usuario = _usuario;
        this.senha = _senha;
        this.genero = _genero;
    }

    public Usuario (String _usuario, String _senha, String _genero){
        this.usuario = _usuario;
        this.senha = _senha;
        this.genero = _genero;
    }




    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }
}
