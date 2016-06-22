package com.example.jonat.agendacontatos;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.database.sqlite.*;
import android.database.*;

import com.example.jonat.agendacontatos.database.DataBase;
import com.example.jonat.agendacontatos.dominio.*;
import com.example.jonat.agendacontatos.dominio.entidades.Contato;


public class ActContato extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{

    private ImageButton btAdd;
    private EditText edtPesquisa;
    private ListView lstContatos;
    private ArrayAdapter<Contato> adpcontatos;

    //objeto da classe DataBase
    private DataBase dataBase;

    //objeto de conexão para o banco
    private SQLiteDatabase connect;

    private RepositorioContato repositorioContato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_contato);

        btAdd = (ImageButton) findViewById(R.id.btAdd);
        edtPesquisa = (EditText) findViewById(R.id.edtPesquisa);
        lstContatos = (ListView) findViewById(R.id.lstContatos);


        btAdd.setOnClickListener(this); //registrando evento clicar botão add

        lstContatos.setOnItemClickListener(this);//registo

        //criação do banco de dados para agenda
        try {

            dataBase = new DataBase(this);

            connect = dataBase.getWritableDatabase();

            repositorioContato = new RepositorioContato(connect);

            adpcontatos = repositorioContato.buscaContatos(this);

            lstContatos.setAdapter(adpcontatos);


            /*AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setMessage("Conexão criada com sucesso!");
            dlg.setNeutralButton("OK",null);
            dlg.show();
            */

        }catch (SQLException ex){
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setMessage("Erro ao criar o banco: " + ex.getMessage());
            dlg.setNeutralButton("OK",null);
            dlg.show();
        }

    }

    @Override
    public void onClick(View v) {

        Intent it = new Intent(this, ActCadContatos.class);
        startActivityForResult(it,0);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        adpcontatos = repositorioContato.buscaContatos(this);

        lstContatos.setAdapter(adpcontatos);


    }


    //evento chamado sempre que houve click no listview de contatos.
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Contato contato = adpcontatos.getItem(position);


        Intent it = new Intent(this, ActCadContatos.class);

        it.putExtra("CONTATO",contato);

        startActivityForResult(it,0);







    }
}
