package com.example.controlevalidadeprodutos;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {



    Button btnCadastro, btnEntrar, btnSair;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //encontrar os botões pelo ID
        btnCadastro = (Button)findViewById(R.id.btnCadastro);
        btnEntrar = (Button)findViewById(R.id.btnEntrar);
        btnSair = (Button)findViewById(R.id.btnSair);


        //Código das ações dos botões

        //carrega a tela para cadastrar
        btnCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this, Cadastro.class);
                startActivity(it);
            }
        });

        //carrega a tela para logar
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this, Login.class);

                Bundle parametros = new Bundle();

                parametros.putInt("idUsuario", 47);
                it.putExtras(parametros);

                startActivity(it);
            }
        });

        //Fecha o aplicativo
        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            }
        });






}



}
