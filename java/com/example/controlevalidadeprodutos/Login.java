package com.example.controlevalidadeprodutos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Login extends AppCompatActivity {

    //Cria as variáveis e a instância do banco de dados
    BancoDados bd = new BancoDados(this);

    EditText txtUsuario, txtSenha;
    Button btnLogar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent it2 = getIntent();
        Bundle parametros = it2.getExtras();


        //Inicia as variáveis
        txtUsuario = findViewById(R.id.txtUsuario);
        txtSenha = findViewById(R.id.txtSenha);
        btnLogar = findViewById(R.id.btnLogar);


        //ações do botão logar
        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Bundle parametros = new Bundle();

                parametros.clear();

                String usuario = txtUsuario.getText().toString();
                String senha = txtSenha.getText().toString();

                //verifica se os campos estão preenchidos
                if(usuario.isEmpty()) {
                    txtUsuario.setError("Este campo é obrigatório!");
                }else if (senha.isEmpty()) {
                    txtSenha.setError("Este campo é obrigatório!");
                }
                //Caso o login e senha estiverem corretos, é iniciada uma nova activity
                else if(bd.loginUsuario(usuario, senha)) {


                    Toast.makeText(Login.this, "Bem vindo "+usuario, Toast.LENGTH_LONG).show();
                    //Cria um intent chamando a página de produtos
                    Intent it = new Intent(Login.this, Produtos.class);

                    //seleciona o usuário pelo nome de usuario
                    Usuario user = bd.selecionarUsuario(usuario);

                    //cria a variável idUsuario que será recuperada na tela dos produtos
                    parametros.putInt("idUsuario", user.getId());
                    parametros.putBoolean("excluir", false);

                    it.putExtras(parametros);

                    //limpa os campos
                    txtUsuario.setText("");
                    txtSenha.setText("");


                    startActivity(it);

                }else {

                    Toast.makeText(Login.this, "Usuário não cadastrado ou senha inválida!", Toast.LENGTH_LONG).show();


                }






            }
        });


    }
}
