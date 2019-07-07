package com.example.controlevalidadeprodutos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class EditarPerfil extends AppCompatActivity {

    //Cria as variáveis e a instância do banco de dados
    int idUsuario;
    EditText editTextUsuario, editTextNovaSenha, editTextNovaSenha2;
    Button btnEditarPerfilSalvar;
    BancoDados bd = new BancoDados(this);
    RadioButton rdFemEdit, rdMascEdit;
    String generoEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);


        //Inicia as variáveis com os IDs dos itens da activity
        editTextUsuario = findViewById(R.id.editTextUsuario);
        editTextNovaSenha = findViewById(R.id.editTextNovaSenha);
        editTextNovaSenha2 = findViewById(R.id.editTextNovaSenha2);
        btnEditarPerfilSalvar = findViewById(R.id.btnEditarPerfilSalvar);
        rdFemEdit = findViewById(R.id.rdFemEdit);
        rdMascEdit = findViewById(R.id.rdMascEdit);


        Intent it = getIntent();
        Bundle parametros = it.getExtras();
        if(parametros != null) {
            //pega o ID do usuário que veio da tela anterior com o getIntent()
            this.idUsuario = parametros.getInt("idUsuario");

        }else if (parametros == null) {

        }

        //seleciona o usuário pelo ID
        Usuario usuario = bd.selecionarUsuarioPeloId(idUsuario);

        //coloca o nome do usuario no texto da tela.
        editTextUsuario.setText(usuario.getUsuario());

        //verifica qual gênero do usuario e marca no radio button.
        if (usuario.getGenero().intern() == "Feminino") {
            rdFemEdit.setChecked(true);

        }else if(usuario.getGenero().intern() == "Masculino") {
            rdMascEdit.setChecked(true);

        }



        //ações do botão Salvar
        btnEditarPerfilSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //verifica qual opção do gênero está selecionada e atribui o valor para a variável
                if(rdFemEdit.isChecked()){
                    generoEdit = "Feminino";
                }else if (rdMascEdit.isChecked()){
                    generoEdit = "Masculino";
                }

                //Pega os valores das caixas de texto e atribui nas variáveis
                int id = idUsuario;
                String usuario = editTextUsuario.getText().toString();
                String senha = editTextNovaSenha.getText().toString();
                String senha2 = editTextNovaSenha2.getText().toString();
                String genero = generoEdit;

                //verifica se o campo usuário está vazio, caso seja verdadeiro emite um erro
                if(usuario.isEmpty()) {
                    editTextUsuario.setError("Este campo é obrigatório!");
                }
                //Verifica se o campo senha está vazio, caso esteja, atualiza somente os dados usuario e gênero.
                //A senha é obtida do banco de dados.
                else if(senha.length() == 0 && senha2.length() == 0) {
                    Usuario usuarioEdit = bd.selecionarUsuarioPeloId(idUsuario);
                    usuarioEdit.setUsuario(usuario);
                    usuarioEdit.setSenha(usuarioEdit.getSenha());
                    usuarioEdit.setGenero(genero);
                    bd.alterarUsuario(usuarioEdit);
                    Toast.makeText(getApplicationContext(), "Dados alterados.", Toast.LENGTH_LONG).show();
                }
                //Se os campos de senha e nova senha estiverem preenchidos, verifica se as senhas são iguais.
                //Se as senhas forem diferentes, emite um erro.
                else if (senha.length() == 0 && senha2.length() > 0 || senha.length() > 0 && senha2.length() == 0 ) {

                    editTextNovaSenha.setError("Senhas Diferentes");
                    editTextNovaSenha2.setError("Senhas Diferentes");
                }
                //Se os campos de senha e nova senha estiverem preenchidos, verifica se as senhas são iguais.
                //Se tudo estiver OK, atualiza os dados e senha do usuario e emite uma mensagem
                else if (senha.length() > 0 && senha2.length() > 0 && senha.equals(senha2)  ){

                    Usuario usuarioEdit = bd.selecionarUsuarioPeloId(idUsuario);
                    usuarioEdit.setUsuario(usuario);
                    usuarioEdit.setSenha(senha);
                    usuarioEdit.setGenero(genero);
                    bd.alterarUsuario(usuarioEdit);
                    Toast.makeText(getApplicationContext(), "Dados e Senha alterados.", Toast.LENGTH_LONG).show();
                }



            }
        });


    }
}
