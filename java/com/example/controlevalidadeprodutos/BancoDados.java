package com.example.controlevalidadeprodutos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class BancoDados extends SQLiteOpenHelper  {



    //Variáveis de versão do banco e do nome do banco.
    private static final int VERSAO_BANCO = 2;
    private static final String NOME_BANCO = "bd_produtos3";




    public BancoDados(Context context) {
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


        //Strings com instruções SQL para criar as tabelas td_produtos e tb_usuarios no banco de dados.
        String QUERY_CRIAR = "CREATE TABLE tb_produtos (id INTEGER PRIMARY KEY, nome TEXT, marca TEXT, quantidade INTEGER, fabricacao TEXT, validade TEXT, notificacao INTEGER, idUsuario INTEGER NOT NULL)";
        //O campo usuario está como UNIQUE, ou seja, não será aceito 2 registros com o mesmo usuário.
        String QUERY_CRIAR_USUARIOS = "CREATE TABLE tb_usuarios (id INTEGER PRIMARY KEY, usuario TEXT NOT NULL, senha TEXT NOT NULL, genero TEXT, UNIQUE(usuario) )";


        //Executa as querys.
        sqLiteDatabase.execSQL(QUERY_CRIAR);
        sqLiteDatabase.execSQL(QUERY_CRIAR_USUARIOS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }



    //Método usado para adicionar um novo produto. Recebe um objeto Produto como parâmentro.
    void addProduto (Produto produto) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        //Cria um ContentValues para inserir os valores no banco
        ContentValues values = new ContentValues();

        //Insere os valores no ContentValues pegando os dados do objeto
        values.put("nome", produto.getNome());
        values.put("marca", produto.getMarca());
        values.put("quantidade", produto.getQuantidade());
        values.put("fabricacao", produto.getFabricacao());
        values.put("validade", produto.getValidade());
        values.put("notificacao", produto.getNotificacao());
        values.put("idUsuario", produto.getIdUsuario());

        //executa o insert
        sqLiteDatabase.insert("tb_produtos", null, values);

    }

    //Método para apagar um produto de acordo com o ID do mesmo
    void apagarProduto(Produto produto) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete("tb_produtos", "id = ?", new String[] {String.valueOf(produto.getId())});
        sqLiteDatabase.close();
    }

    //Seleciona um produto a partir do ID e retorna um objeto Produto
    Produto selecionarProduto(int id) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query("tb_produtos", new String[] {"id", "nome", "marca", "quantidade","fabricacao", "validade", "notificacao", "idUsuario"}, "id = ?",
                new String[] {String.valueOf(id)}, null, null, null, null);

        if(cursor != null) {
            cursor.moveToFirst();
        }

        Produto produto1 = new Produto(Integer.parseInt((cursor.getString(0))),
                cursor.getString(1),cursor.getString(2), Integer.parseInt(cursor.getString(3)),
                        cursor.getString(4), cursor.getString(5), Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7)));

        return produto1;

    }

    //Atualiza os produtos. Semelhante ao método de criar produto.
    void atualizaProduto(Produto produto) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("nome", produto.getNome());
        values.put("marca", produto.getMarca());
        values.put("quantidade", produto.getQuantidade());
        values.put("fabricacao", produto.getFabricacao());
        values.put("validade", produto.getValidade());
        values.put("notificacao", produto.getNotificacao());

        sqLiteDatabase.update("tb_produtos", values, "id = ?",
                new String[] {String.valueOf(produto.getId())});


    }

    //Método usado para listar todos os produtos do usuário. Retona uma Lista
    public List<Produto> listarTodosProdutos(int idUsuario){

        List<Produto> listaProdutos = new ArrayList<Produto>();
        String query = "SELECT * FROM tb_produtos WHERE idUsuario = " +idUsuario+"";


        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor c = sqLiteDatabase.rawQuery(query, null);
        if(c.moveToFirst()) {
            do {
                Produto produto = new Produto();
                produto.setId(Integer.parseInt(c.getString(0)));
                produto.setNome(c.getString(1));
                produto.setMarca(c.getString(2));
                produto.setQuantidade(Integer.parseInt(c.getString(3)));
                produto.setFabricacao(c.getString(4));
                produto.setValidade(c.getString(5));
                produto.setNotificacao(Integer.parseInt(c.getString(6)));
                produto.setIdUsuario(Integer.parseInt(c.getString(7)));



                listaProdutos.add(produto);
            }while (c.moveToNext());
        }

        return listaProdutos;
    }


    //Método para adicionar um novo Usuário. Semelhante ao addProduto()
    public void addUsuario(Usuario usuario){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("usuario", usuario.getUsuario());
        values.put("senha", usuario.getSenha());
        values.put("genero", usuario.getGenero());

            sqLiteDatabase.insert("tb_usuarios", null, values);

    }

    //Método para consultar se o usuário já existe. Pode retornar false ou true.
    public boolean consultaUsuario(String usuario) {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query("tb_usuarios", new String[] {"id", "usuario", "senha", "genero"}, "usuario = ?",
                new String[] {String.valueOf(usuario)}, null, null, null, null);

        if(cursor != null) {
            cursor.moveToFirst();
        }

        if(cursor.getCount() == 0) {

            return false;

        }else {

            return true;
        }


    }


    //Método usado para fazer login
    public boolean loginUsuario(String usuario, String senha) {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query("tb_usuarios", new String[]{"id", "usuario", "senha"}, "usuario = ?",
                new String[]{String.valueOf(usuario)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        if (cursor.getCount() == 0) {

            return false;

        } else {

            if (cursor.getString(2).equals(senha)) {
                return true;
            } else{

                String msg = cursor.getString(2).toString();

                return false;
            }
        }


    }//fim login usuario


    //Método para criar um Objeto Usuário a partir do nome de usuário.
    Usuario selecionarUsuario(String usuario) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query("tb_usuarios", new String[]{"id", "usuario", "senha", "genero"}, "usuario = ?",
                new String[]{String.valueOf(usuario)}, null, null, null, null);

        if(cursor != null) {
            cursor.moveToFirst();
        }


        Usuario usuario1 = new Usuario();
        usuario1.setId(Integer.parseInt((cursor.getString(0))));
        usuario1.setUsuario(cursor.getString(1));
        usuario1.setSenha(cursor.getString(2));
        usuario1.setGenero(cursor.getString(3));

        return usuario1;

    }




    //Melehante ao método acima, porém usando o ID
    Usuario selecionarUsuarioPeloId(int idUsuario) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query("tb_usuarios", new String[]{"id", "usuario", "senha", "genero"}, "id = ?",
                new String[]{String.valueOf(idUsuario)}, null, null, null, null);

        if(cursor != null) {
            cursor.moveToFirst();
        }


        Usuario usuario1 = new Usuario();
        usuario1.setId(Integer.parseInt((cursor.getString(0))));
        usuario1.setUsuario(cursor.getString(1));
        usuario1.setSenha(cursor.getString(2));
        usuario1.setGenero(cursor.getString(3));

        return usuario1;

    }

    //Método para apagar o usuário e todos os produtos atrelados a ele.
    void apagarUsuario(int id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete("tb_usuarios", "id = ?", new String[] {String.valueOf(id)});
        sqLiteDatabase.delete("tb_produtos", "idUsuario = ?", new String[] {String.valueOf(id)});
        sqLiteDatabase.close();
    }


    //Método para modificar os dados do usuário
    void alterarUsuario(Usuario user) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Usuario usuario = user;

        values.put("usuario", usuario.getUsuario());
        values.put("senha", usuario.getSenha());
        values.put("genero", usuario.getGenero());


        sqLiteDatabase.update("tb_usuarios", values, "id = ?",
                new String[] {String.valueOf(usuario.getId())});

    }


}
