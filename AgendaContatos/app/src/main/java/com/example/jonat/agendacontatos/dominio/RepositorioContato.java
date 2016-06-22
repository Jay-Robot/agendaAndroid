package com.example.jonat.agendacontatos.dominio;

import android.content.ContentValues;
import android.content.Context;
import android.database.*;
import android.database.sqlite.*;
import android.widget.ArrayAdapter;

import com.example.jonat.agendacontatos.dominio.entidades.Contato;

import java.util.Date;

/**
 * Created by jonat on 18/06/2016.
 * Toda regra de negócio da aplicação
 * métodos para consulta e inserção na base de dados
 */
public class RepositorioContato {


    private SQLiteDatabase connect;


    public RepositorioContato(SQLiteDatabase connect){

        this.connect = connect;
    }


    public void inserir(Contato contato){

        ContentValues values = new ContentValues();

        values.put("NOME", contato.getNome());
        values.put("TELEFONE", contato.getTelefone());
        values.put("TIPOTELEFONE", contato.getTipoTelefone());
        values.put("EMAIL", contato.getEmail());
        values.put("TIPOEMAIL", contato.getTipoEmail());
        values.put("ENDERECO", contato.getEndereco());
        values.put("TIPOENDERECO", contato.getTipoEndereco());
        values.put("DATASESPECIAIS", contato.getDatasEspeciais().getTime());
        values.put("TIPODATASESPECIAIS", contato.getTipoDatasEspeciais());
        values.put("GRUPOS", contato.getGrupos());


        connect.insertOrThrow("CONTATO", null, values);
    }


    //teste
    /*
    public void testeInserirContatos() {

        for (int i = 0; i < 10; i++) {
            ContentValues values = new ContentValues();
            values.put("TELEFONE", "98706950");

            connect.insertOrThrow("CONTATO", null, values);

        }
    }
*/

    //passando os activitys
    public ArrayAdapter<Contato> buscaContatos(Context context){

        //exibe em cada linha do ListView um único texto
        ArrayAdapter<Contato> adpContatos = new ArrayAdapter<Contato>(context,android.R.layout.simple_list_item_1);

        //método query monta uma consulta sql de forma automática
        //Cursor armazena todos os registros consultados
        Cursor cursor = connect.query("CONTATO", null, null, null, null, null, null);


        if (cursor.getCount()>0){

            cursor.moveToFirst();

            do{

                Contato contato = new Contato();
                contato.setId(cursor.getLong(0));
                contato.setNome(cursor.getString(1));
                contato.setTelefone(cursor.getString(2));
                contato.setTipoTelefone(cursor.getString(3));
                contato.setEmail(cursor.getString(4));
                contato.setTipoEmail(cursor.getString(5));
                contato.setEndereco(cursor.getString(6));
                contato.setTipoEndereco(cursor.getString(7));
                contato.setDatasEspeciais(new Date(cursor.getLong(8)));
                contato.setTipoDatasEspeciais(cursor.getString(9));
                contato.setGrupos(cursor.getString(10));
                adpContatos.add(contato);

            }while (cursor.moveToNext());

        }

        return adpContatos;

    }

}
