package com.example.controlevalidadeprodutos;


import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import java.util.Date;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import net.danlew.android.joda.JodaTimeAndroid;

public class ItemProduto extends AppCompatActivity {

    //Cria as variáveis e a instância do banco de dados
    private int codigo;
    BancoDados bd = new BancoDados(this);
    EditText editNome;
    EditText editMarca;
    EditText editQuantidade;
    EditText editFabricacao;
    EditText editValidade;
    int editNotificacao;
    Button   btnSalvar, btnExcluir;
    RadioButton rdEditQuinze, rdEditTrinta;
    InputMethodManager imm;

   // @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_produto);
        JodaTimeAndroid.init(this);

        //Inicia as variáveis
        imm = (InputMethodManager) this.getSystemService(Service.INPUT_METHOD_SERVICE);
        editNome = (EditText)findViewById(R.id.editNome);
        editMarca = (EditText)findViewById(R.id.editMarca);
        editQuantidade = (EditText)findViewById(R.id.editQuantidade);
        editFabricacao = (EditText)findViewById(R.id.editFabricacao);
        editValidade = (EditText)findViewById(R.id.editValidade);
        rdEditQuinze = findViewById(R.id.rdEditQuinze);
        rdEditTrinta = findViewById(R.id.rdEditTrinta);
        btnSalvar = (Button)findViewById(R.id.btnSalvar);
        btnExcluir = (Button)findViewById(R.id.btnExcluir);

        if (rdEditQuinze.isChecked()){
            editNotificacao = 15;
        } else if (rdEditTrinta.isChecked()) {
            editNotificacao = 90;
        }

        //Atribui uma máscara de data para os campos fabricação e validade, impedindo que o usuário
        //coloque letras e mais caracteres que uma data tem.
        //SimpleDateFormat smfFab = new SimpleDateFormat("NN/NN/NNNN");
        SimpleMaskFormatter smfFab = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher mtwFab = new MaskTextWatcher(editFabricacao, smfFab);
        editFabricacao.addTextChangedListener(mtwFab);
        SimpleMaskFormatter smfVal = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher mtwVal = new MaskTextWatcher(editValidade, smfVal);
        editValidade.addTextChangedListener(mtwVal);
        Intent it = getIntent();
        Bundle parametros = it.getExtras();

        //Verifica se a variável parametros não está vazia, caso verdadeiro, preenche os dados de acordo com o ID do produto.
        if (parametros != null) {
            this.codigo = parametros.getInt("idProduto");
            Produto produto = bd.selecionarProduto(codigo);

            editNome.setText(produto.getNome());
            editMarca.setText(produto.getMarca());
            editQuantidade.setText(String.valueOf(produto.getQuantidade()));
            editFabricacao.setText(produto.getFabricacao());
            editValidade.setText(produto.getValidade());

            if (produto.getNotificacao() == 15) {
                rdEditQuinze.setChecked(true);
                Log.d("RD SET CHECK", "Radio = 15");
            }else if (produto.getNotificacao() == 90) {
                rdEditTrinta.setChecked(true);
                Log.d("RD SET CHECK", "Radio = 30");
            }
        }

        //Ações do botão salvar. Verifica se os dados não estão vazios e atualiza o produto no banco de dados.
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            int id = codigo;
            String nome = editNome.getText().toString();
            String marca = editMarca.getText().toString();
            String quantidade = editQuantidade.getText().toString();
            String fabricacao = editFabricacao.getText().toString();
            String validade = editValidade.getText().toString();

            int notificacao = 0;
            if (rdEditQuinze.isChecked()){
                notificacao = 15;

            } else if (rdEditTrinta.isChecked()) {
                notificacao = 90;
            }

            if(nome.isEmpty()) {
                editNome.setError("Este campo é obrigatório!");
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
            }else{
                if(fabricacao.length() == 10 && validade.length() == 10){
                    Date fabri = new Date(fabricacao);
                    Date valid = new Date(validade);

                    if (fabri.before(valid)) {
                        try {
                            Produto produto = bd.selecionarProduto(codigo);
                            produto.setNome(nome);
                            produto.setMarca(marca);
                            produto.setQuantidade(Integer.parseInt(quantidade));
                            produto.setFabricacao(fabricacao);
                            produto.setValidade(validade);
                            produto.setNotificacao(notificacao);
                            bd.atualizaProduto(produto);
                            Toast.makeText(ItemProduto.this, "Produto Alterado com Sucesso!", Toast.LENGTH_LONG).show();

                            esconderTeclado();
                        } catch (Exception e) {
                            Toast.makeText(ItemProduto.this, "Ocorreu um erro ao alterar o produto!", Toast.LENGTH_LONG).show();
                        }
                    } else if (fabri.after(valid)) {
                        Toast.makeText(ItemProduto.this, "Data de fabricação deve ser menor que a de validade!", Toast.LENGTH_LONG).show();
                    } else if (fabri.equals(valid)) {
                        Toast.makeText(ItemProduto.this, "Data de fabricação e de validade devem ser diferentes!", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Preencha os campos corretamente!", Toast.LENGTH_LONG).show();
                }
            }
            }
        });
        //Ações do botão excluir.
        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Seleciona o produto no banco de dados e coloca no Objeto produto
                Produto produto = bd.selecionarProduto(codigo);

                //cria um novo alerta
                AlertDialog.Builder alerta = new AlertDialog.Builder(ItemProduto.this);
                alerta.setTitle("Excluindo...");
                alerta.setIcon(android.R.drawable.ic_menu_delete);
                alerta.setMessage("Deseja realmente excluir o produto "+produto.getNome()+" "+produto.getMarca()+"?");
                alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Caso o usuario clique em SIM, o produto é apagado e a activity encerrada.
                        Produto produto = bd.selecionarProduto(codigo);
                        bd.apagarProduto(produto);
                        finish();
                    }
                });
                alerta.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                //mostra o alerta
                alerta.show();
            }
        });
    }
    //método para esconder o teclado quando o usuario clicar em salvar.
    void esconderTeclado(){
        imm.hideSoftInputFromWindow(editNome.getWindowToken(), 0);
    }
}
