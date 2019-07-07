package com.example.controlevalidadeprodutos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Perfil extends AppCompatActivity {

    //Cria as variáveis e a instância do banco de dados
    TextView txtUsuario, txtGenero;
    int idUsuario;
    BancoDados bd = new BancoDados(this);
    Button btnEditarPerfil, btnExcluirPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);


        //Inicia as variáveis
        txtUsuario = (TextView) findViewById(R.id.txtUsuario);
        txtGenero = (TextView) findViewById(R.id.txtGenero);
        btnEditarPerfil = findViewById(R.id.btnEditarPerfil);
        btnExcluirPerfil = findViewById(R.id.btnExcluirPerfil);

        Intent it = getIntent();
        Bundle parametros = it.getExtras();
            if(parametros != null) {
                this.idUsuario = parametros.getInt("idUsuario");

            }else if (parametros == null) {

            }



        Usuario usuario = bd.selecionarUsuarioPeloId(idUsuario);

        txtUsuario.setText(usuario.getUsuario());
        txtGenero.setText(usuario.getGenero());



        //ações do botão editar. Inicia uma nova tela para editar o usuario
        btnEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle parametros = new Bundle();
                parametros.putInt("idUsuario", idUsuario);
                Intent it = new Intent(getApplicationContext(), EditarPerfil.class);
                it.putExtras(parametros);
                startActivity(it);

            }
        });


        //ações do botão excluir.
        btnExcluirPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //emite um alerta perguntando se quer realmente excluir
                AlertDialog.Builder alerta = new AlertDialog.Builder(Perfil.this);
                alerta.setTitle("Excluindo...");
                alerta.setIcon(android.R.drawable.ic_menu_delete);
                alerta.setMessage("Deseja realmente excluir a conta?\nEsta ação não pode ser desfeita. ");
                alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       bd.apagarUsuario(idUsuario);


                        Intent it = new Intent(getApplicationContext(), Produtos.class);


                        Bundle parametros = new Bundle();


                        parametros.putInt("idUsuario", 99999);
                        parametros.putBoolean("excluir", true);



                        it.putExtras(parametros);
                        finish();
                        startActivity(it);


                    }
                });
                alerta.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alerta.show();




            }
        });




    }


    @Override
    public void onResume(){
        super.onResume();
        Usuario usuario = bd.selecionarUsuarioPeloId(idUsuario);
        txtUsuario.setText(usuario.getUsuario());
        txtGenero.setText(usuario.getGenero());

    }



}
