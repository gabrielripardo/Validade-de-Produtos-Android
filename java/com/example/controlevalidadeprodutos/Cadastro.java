package com.example.controlevalidadeprodutos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cadastro extends AppCompatActivity {


    //Cria as variáveis e a instância do banco de dados
    BancoDados bd = new BancoDados(this);
    EditText txtUsuarioCadastro, txtSenhaCadastro;
    Button btnCadastrar;
    RadioButton genMasc, genFem;
    String genero = null;
    RadioGroup rdGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        //Inicia as variáveis com os IDs dos itens da activity
        txtUsuarioCadastro = (EditText)findViewById(R.id.txtUsuarioCadastro);
        txtSenhaCadastro = (EditText)findViewById(R.id.txtSenhaCadastro);
        btnCadastrar = (Button)findViewById(R.id.btnCadastro);
        genMasc = (RadioButton)findViewById(R.id.rdMasc);
        genFem = (RadioButton)findViewById(R.id.rdFem);
        rdGroup = (RadioGroup)findViewById(R.id.rdGenero);

        //Cria a ação do botão cadastrar
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Cria as variáveis usuario e senha e pega os dados de dentro do campo de texto
                String usuario = txtUsuarioCadastro.getText().toString();
                String senha = txtSenhaCadastro.getText().toString();

                //verifica se o radio button masculino ou feminino está selecionado e atribui o valor referente para a variáveç genero
                if (genMasc.isChecked()){
                    genero = "Masculino";
                } else if (genFem.isChecked()) {
                    genero = "Feminino";
                }

                //verifica se os campos estão preenchidos, caso contrário emite um erro.
                if(usuario.isEmpty()) {
                    txtUsuarioCadastro.setError("Este campo é obrigatório!");
                }else if(!genMasc.isChecked() && !genFem.isChecked()){
                    genMasc.setError("Este campo é obrigatório");
                    genFem.setError("Este campo é obrigatório");
                }   else if (senha.isEmpty()) {
                    txtSenhaCadastro.setError("Este campo é obrigatório!");
                }else if(bd.consultaUsuario(usuario)) {
                    Log.d("TESTE_BOOLEAN_USUARIO", "EXISTE");

                    //cria uma notificação na tela dizendo que o usuário já existe.
                    Toast.makeText(Cadastro.this, "O usuário "+usuario+" já existe!", Toast.LENGTH_LONG).show();

                }else {
                    //Se os campos estiverem todos preenchidos e o usuário não existir, é criado um novo usuário
                    if(verificarSenha(senha)){
                        bd.addUsuario(new Usuario(usuario, senha, genero));
                        //emite uma notificação na tela
                        Toast.makeText(Cadastro.this, "Usuário "+usuario+" cadastrado com sucesso!", Toast.LENGTH_LONG).show();

                        //limpa todos os campos do formulário
                        txtSenhaCadastro.setText("");
                        txtUsuarioCadastro.setText("");
                        genMasc.setChecked(false);
                        genFem.setChecked(false);
                    }else{
                        Toast.makeText(Cadastro.this, "A Senha fornecida contém caracter especial!", Toast.LENGTH_LONG).show();
                        Toast.makeText(Cadastro.this, "Utilize somente letras e números.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    boolean verificarSenha(String senha) {
        String regex = "[$&+,:;=?@#|'<>.^*()%!-]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(senha);

        if (!matcher.find()){
            return true;
        }
        return false;
    }
}
