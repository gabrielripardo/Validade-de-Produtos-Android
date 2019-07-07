package com.example.controlevalidadeprodutos;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class Produtos extends AppCompatActivity {

    //Cria as variáveis e a instância do banco de dados
    ListView listProdutos;
    ArrayAdapter<String> adapter;
    ArrayList<String> arrayList;
    BancoDados bd = new BancoDados(this);
    Button   btnPerfil;
    int idUsuario;
    EditText sPesquisa;
    FloatingActionButton btnNovo;


    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_produtos);

        //Inicia as variáveis
        sPesquisa = (EditText)findViewById(R.id.sPesquisaVencer);

        listProdutos = (ListView)findViewById(R.id.listaProdutos);
        btnNovo = (FloatingActionButton) findViewById(R.id.btnNovo);
        btnPerfil = (Button)findViewById(R.id.btnPerfil);



        //Esse método é usado para ficar observando o campo
        //de texto para aplicar um filtro na lista de produtos de acordo com o que está escrito no campo.
        sPesquisa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                (Produtos.this).adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //ações do botão novo, inicia a tela para cadastrar um novo produto
        btnNovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle parametros = new Bundle();
                parametros.putInt("idUsuario", idUsuario);
                Intent it = new Intent(getApplicationContext(), novoProduto.class);
                it.putExtras(parametros);
                startActivity(it);
            }
        });


        //ações do botão perfil, inicia a tela para visualizar o perfil
        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle parametros = new Bundle();
                parametros.putInt("idUsuario", idUsuario);
                Intent it = new Intent(Produtos.this, Perfil.class);
                it.putExtras(parametros);
                startActivity(it);

            }
        });



        //Método usado para abrir a página com as informações do produto quando houver um clique sobre ele.
        listProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String conteudo = (String) listProdutos.getItemAtPosition(i);
                // Toast.makeText(MainActivity.this, "Selecionado: " + conteudo, Toast.LENGTH_LONG).show();

                String codigo = conteudo.substring(0, conteudo.indexOf("-"));
                Produto produto = bd.selecionarProduto(Integer.parseInt(codigo));





                Intent it = new Intent(Produtos.this, ItemProduto.class);
                Bundle parametros = new Bundle();

                parametros.putInt("idProduto", Integer.parseInt(codigo));
                it.putExtras(parametros);


                startActivity(it);
            }
        });


        //Quando o usuário pressionar o produto irá ser perguntado se deseja excluir o mesmo.
        listProdutos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                String conteudo = (String) listProdutos.getItemAtPosition(i);

                final String idProd = conteudo.substring(0, conteudo.indexOf("-"));

                Produto produto = bd.selecionarProduto(Integer.parseInt(idProd));


                AlertDialog.Builder alerta = new AlertDialog.Builder(Produtos.this);
                alerta.setTitle("Excluindo...");
                alerta.setIcon(android.R.drawable.ic_menu_delete);
                alerta.setMessage("Deseja realmente excluir o produto "+produto.getNome()+" "+produto.getMarca()+"?");
                alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Produto produto2 = bd.selecionarProduto(Integer.parseInt(idProd));
                        bd.apagarProduto(produto2);
                       // finish();
                        listarProdutos(idUsuario);
                    }
                });
                alerta.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alerta.show();

//                Toast.makeText(getApplicationContext(), "LONG CLICK:   "+codigo, Toast.LENGTH_LONG).show();

                return true;
            }
        });



    }




    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onResume(){
        super.onResume();

        Intent it = getIntent();
        Bundle parametros = it.getExtras();


        this.idUsuario = parametros.getInt("idUsuario");
        boolean ex = parametros.getBoolean("excluir");
        //notificacao(idUsuario);
        listarProdutos(idUsuario);
        verificarValidade();



        //verifica se o usuário deletou a propria conta. Se sim, carrega a página inicial da aplicação
        if(ex == true) {

            Intent it2 = new Intent(getApplicationContext(), MainActivity.class);
            finish();
            startActivity(it2);
        }else {

        }

    }


    //cria a notificação com os produtos que estão próximos do vencimento
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void notificacao(int i) {



        Intent intent = new Intent(this, AVencer.class);
        Bundle parametros = new Bundle();
        parametros.putInt("idUsuario", i);

        intent.putExtras(parametros);

        PendingIntent pi = PendingIntent.getActivity(getBaseContext(), 32, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(getBaseContext())
                .setContentTitle("Existem Produtos Próximo do Vencimento")
                .setContentText("Um ou mais produtos estão próximos de vencer a validade.")
                .setSmallIcon(R.drawable.ic_alerta)
                .setContentIntent(pi).build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(32, notification);




    }


    //lista todos os produtos to usuário
    public void listarProdutos(int idUsuario){
        List<Produto> produtos = bd.listarTodosProdutos(idUsuario);

        arrayList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(Produtos.this, android.R.layout.simple_list_item_1, arrayList);



        listProdutos.setAdapter(adapter);

        for(Produto p : produtos) {


            arrayList.add(p.getId() + "- "+p.getNome() + " "+ p.getMarca()  );

            adapter.notifyDataSetChanged();


        }


    }


    //verifica a validade dos produtos
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void verificarValidade(){


        List<Produto> produtos = bd.listarTodosProdutos(idUsuario);

        arrayList = new ArrayList<String>();
        int quantidadeProdutos = 0;



        for(Produto p : produtos) {

            String strDate = p.getValidade();


            String[] split = strDate.split("/");


            LocalDate ld = new LocalDate(Integer.parseInt(split[2]), Integer.parseInt(split[1]), Integer.parseInt(split[0]));
            LocalDate ld2 = new LocalDate();



            int dias = Days.daysBetween(ld2, ld).getDays();


            if (dias <= p.getNotificacao() && dias >= 0) {
                quantidadeProdutos++;
            }
            //testes
            else if (dias > p.getNotificacao()) {

            }else if (dias < 0) {
                dias = dias*-1;

            }


        }
        if (quantidadeProdutos > 0) {
            notificacao(idUsuario);
        }



    }



}
