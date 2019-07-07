package com.example.controlevalidadeprodutos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class AVencer extends AppCompatActivity {

    //Declara as variáveis
    int idUsuario;
    BancoDados bd = new BancoDados(this);
    ArrayAdapter<String> adapter;
    ArrayList<String> arrayList;
    ListView listProdutos;
    EditText sPesquisaVencer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avencer);

        //inicia as variáveis procurando os itens nos layouts através do ID
        listProdutos = (ListView)findViewById(R.id.listaProdutos);
        sPesquisaVencer = findViewById(R.id.sPesquisaVencer);


        //Inicia o Intent e o Bundle para pegar variáveis vindas de outras activities
        Intent it = getIntent();
        Bundle parametros = it.getExtras();

        //pega o ID do usuário vindo que outra activitye através do Intent
        this.idUsuario = parametros.getInt("idUsuario");



        //Esse método é usado para ficar observando o campo
        //de texto para aplicar um filtro na lista de produtos de acordo com o que está escrito no campo.
        sPesquisaVencer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Aqui é onde o filtro acontece.
                (AVencer.this).adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        //Método usado para abrir a página com as informações do produto quando houver um clique sobre ele.
        listProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //Cria uma String com o conteúdo do texto do produto que foi clicado
                String conteudo = (String) listProdutos.getItemAtPosition(i);

                //pega somente o início do texto e coloca na variável código
                String codigo = conteudo.substring(0, conteudo.indexOf("-"));

                //Cria um novo Intent para chamar a página do produto
                Intent it = new Intent(AVencer.this, ItemProduto.class);
                Bundle parametros = new Bundle();

                //envia o ID do produto para a nova página
                parametros.putInt("idProduto", Integer.parseInt(codigo));
                it.putExtras(parametros);

                //inicia a nova página
                startActivity(it);
            }
        });



    }

    //Este método onResume() é chamado sempre que a página é visualizada.
    @Override
    public void onResume(){
        super.onResume();

        //Método para listar todos os produtos do usuário atual
        listarProdutos(idUsuario);



    }


    //Método para listar todos os produtos do usuário atual
    public void listarProdutos(int idUsuario){
        //Cria uma lista com o método listarTodosProdutos() da classe BancoDados
        List<Produto> produtos = bd.listarTodosProdutos(idUsuario);

        //Inicia o arrayList para montar a Listview com os dados
        arrayList = new ArrayList<String>();

        //Inicia o adapter que irá definir o tipo de lista que será criada e quais dados estarão nessa lista
        adapter = new ArrayAdapter<String>(AVencer.this, android.R.layout.simple_list_item_1, arrayList);


        //Atrela o adapter a ListView listProdutos
        listProdutos.setAdapter(adapter);


        //esse for irá percorrer todos os itens que vieram do banco de dados  e adiciona-los a lista.
        for(Produto p : produtos) {

            //pega a validade do produto
            String strDate = p.getValidade();

            //converte a validade de string para array, separando os dados pela barra ( / ).
            String[] split = strDate.split("/");

            //Cria a variável ld com a data de validade do produto
            LocalDate ld = new LocalDate(Integer.parseInt(split[2]), Integer.parseInt(split[1]), Integer.parseInt(split[0]));

            //cria a variável ld2 com a data de hoje
            LocalDate ld2 = new LocalDate();





            //A variável dias terá a quantidade de dias de hoje até a data de validade do produto
            int dias = Days.daysBetween(ld2, ld).getDays();


            //Se a quantidade de dias for inferior aos dias de notificação, o produto é adicionado a lista.
            if (dias <= p.getNotificacao() && dias >= 0) {

                arrayList.add(p.getId() + "- "+p.getNome() + " "+ p.getMarca() +" |    validade: " + p.getValidade());

            //testes
            }else if (dias > p.getNotificacao()) {

            }else if (dias < 0) {
                dias = dias*-1;

            }


            adapter.notifyDataSetChanged();


        }


    }



}
