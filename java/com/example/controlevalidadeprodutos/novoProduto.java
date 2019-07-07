package com.example.controlevalidadeprodutos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

public class novoProduto extends AppCompatActivity {

    //Cria as variáveis e a instância do banco de dados
    BancoDados bd = new BancoDados(this);

    EditText editNome;
    EditText editMarca;
    EditText editQuantidade;
    EditText editFabricacao;
    EditText editValidade;
    int editNotificacao;
    Button btnLimpar, btnSalvar;
    InputMethodManager imm;
    int idUsuario;
    RadioButton rdQuinze, rdTrinta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_produto);

        //Inicia as variáveis
        editNome = (EditText)findViewById(R.id.editNome);
        editMarca = (EditText)findViewById(R.id.editMarca);
        editQuantidade = (EditText)findViewById(R.id.editQuantidade);
        editFabricacao = (EditText)findViewById(R.id.editFabricacao);
        editValidade = (EditText)findViewById(R.id.editValidade);
        rdQuinze = findViewById(R.id.rdQuinze);
        rdTrinta = findViewById(R.id.rdTrinta);


        btnLimpar = (Button)findViewById(R.id.btnLimpar);
        btnSalvar = (Button)findViewById(R.id.btnSalvar);



        //Atribui uma máscara de data para os campos fabricação e validade, impedindo que o usuário
        //coloque letras e mais caracteres que uma data tem.
        SimpleMaskFormatter smfFab = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher mtwFab = new MaskTextWatcher(editFabricacao, smfFab);
        editFabricacao.addTextChangedListener(mtwFab);

        SimpleMaskFormatter smfVal = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher mtwVal = new MaskTextWatcher(editValidade, smfVal);
        editValidade.addTextChangedListener(mtwVal);






        Intent it = getIntent();
        Bundle parametros = it.getExtras();

        if (parametros != null) {
            this.idUsuario = parametros.getInt("idUsuario");

        }



        imm = (InputMethodManager) this.getSystemService(Service.INPUT_METHOD_SERVICE);






        btnLimpar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                limparCampos();
            }
        });


        //verifica se todas as informações foram preenchidas. Caso verdadeiro cria um novo produto no banco de dados
        //Caso falso, emite um erro.
        btnSalvar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if (rdQuinze.isChecked()){
                    editNotificacao = 15;
                } else if (rdTrinta.isChecked()) {

                    editNotificacao = 90;
                }

                String nome = editNome.getText().toString();
                String marca = editMarca.getText().toString();
                String quantidade = editQuantidade.getText().toString();
                String fabricacao = editFabricacao.getText().toString();
                String validade = editValidade.getText().toString();
                String notificacao = String.valueOf(editNotificacao);


                if(nome.isEmpty()) {
                    editNome.setError("Este campo é obrigatório!");
                }else if(!rdQuinze.isChecked() && !rdTrinta.isChecked()){
                    rdQuinze.setError("Este campo é obrigatório");
                    rdTrinta.setError("Este campo é obrigatório");
                }
                else if(marca.isEmpty()) {
                    editMarca.setError("Este campo é obrigatório!");
                }
                else if(quantidade.isEmpty()) {
                    editQuantidade.setError("Este campo é obrigatório!");
                }
                else if(fabricacao.isEmpty()) {
                    editFabricacao.setError("Este campo é obrigatório!");
                }
                else if(validade.isEmpty()) {
                    editValidade.setError("Este campo é obrigatório!");
                }

                else {
                    Date fabri = new Date(fabricacao);
                    Date valid = new Date(validade);

                    if(fabri.before(valid)){
                        Toast.makeText(novoProduto.this, "Ok!"+ validade, Toast.LENGTH_LONG).show();
                    }else if(fabri.after(valid)){
                        Toast.makeText(novoProduto.this, "Data de validade deve ser superior a de fabricação!"+ validade, Toast.LENGTH_LONG).show();
                    }


                    //Toast.makeText(novoProduto.this, "Fab: "+ fabricacao + "Val: "+ validade, Toast.LENGTH_LONG).show();


                    /*
                    //Converte a string em Date
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    Date date1 = null;
                    try {
                        date1 = sdf.parse(fabricacao);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Date date2 = null;
                    try {
                        date2 = sdf.parse(validade);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if(date1.compareTo(date2) < 0){                        
                        bd.addProduto(new Produto(nome, marca, Integer.parseInt(quantidade), fabricacao, validade, Integer.parseInt(notificacao), idUsuario));
                        limparCampos();
                        Toast.makeText(getApplicationContext(), "Produto Adicionado com Sucesso!", Toast.LENGTH_LONG).show();
                                                
                    }else if (date1.compareTo(date2) > 0) {
                        Toast.makeText(novoProduto.this, "Data de fabricação deve ser menor que a de validade!", Toast.LENGTH_LONG).show();
                        //}else if (date1.compareTo(date2) == 0) {
                    }else{
                            Toast.makeText(novoProduto.this, "Data de fabricação e de validade devem ser diferentes!", Toast.LENGTH_LONG).show();
                        }
*/
                    }
                }
        });









    }
    //esconde o teclado
    void esconderTeclado() {
        imm.hideSoftInputFromWindow(editNome.getWindowToken(), 0);
    }


    //limpa todos os campos do formulário
    void limparCampos() {

        editNome.setText("");
        editMarca.setText("");
        editQuantidade.setText("");
        editFabricacao.setText("");
        editValidade.setText("");
        rdTrinta.setChecked(false);
        rdQuinze.setChecked(false);

        editNome.requestFocus();
    }
}
