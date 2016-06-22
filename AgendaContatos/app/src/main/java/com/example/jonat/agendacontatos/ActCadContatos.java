package com.example.jonat.agendacontatos;

import android.app.DatePickerDialog;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import com.example.jonat.agendacontatos.database.DataBase;
import com.example.jonat.agendacontatos.dominio.RepositorioContato;
import com.example.jonat.agendacontatos.dominio.entidades.Contato;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;


public class ActCadContatos extends AppCompatActivity {


    //componentes de edição
    private EditText edtNome;
    private EditText edtEmail;
    private EditText edtTelefone;
    private EditText edtEndereco;
    private EditText edtDatasEspeciais;
    private EditText edtGrupos;

    private Spinner spnTipoEmail;
    private Spinner spnTipoTelefone;
    private Spinner spnTipoEndereco;
    private Spinner spnTipoDatasEspeciais;

    private ArrayAdapter<String> adpTipoEmail;
    private ArrayAdapter<String> adpTipoTelefone;
    private ArrayAdapter<String> adpTipoEndereco;
    private ArrayAdapter<String> adpTipoDatasEspeciais;

    //objeto da classe DataBase
    private DataBase dataBase;

    //objeto de conexão para o banco
    private SQLiteDatabase connect;

    private RepositorioContato repositorioContato;

    private Contato contato;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_cad_contatos);

        //Recuperação de objetos
        edtNome = (EditText) findViewById(R.id.edtNome);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtTelefone = (EditText) findViewById(R.id.edtTelefone);
        edtEndereco = (EditText) findViewById(R.id.edtEndereco);
        edtDatasEspeciais = (EditText) findViewById(R.id.edtDatasEspeciais);
        edtGrupos = (EditText) findViewById(R.id.edtGrupos);

        spnTipoEmail = (Spinner) findViewById(R.id.spnTipoEmail);
        spnTipoTelefone = (Spinner) findViewById(R.id.spnTipoTelefone);
        spnTipoEndereco = (Spinner) findViewById(R.id.spnTipoEndereco);
        spnTipoDatasEspeciais = (Spinner) findViewById(R.id.spnDatasEspeciais);

        //criando referências dos adapters
        //passado referência do activity e itens do spinner - utilizado layout padrão android
        //Exibição de itens por dropdown
        adpTipoEmail = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adpTipoEmail.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adpTipoTelefone = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adpTipoTelefone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adpTipoEndereco = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adpTipoEndereco.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adpTipoDatasEspeciais = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adpTipoDatasEspeciais.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //vinculando componente spinner ao arrayadpter
        spnTipoEmail.setAdapter(adpTipoEmail);
        spnTipoTelefone.setAdapter(adpTipoTelefone);
        spnTipoEndereco.setAdapter(adpTipoEndereco);
        spnTipoDatasEspeciais.setAdapter(adpTipoDatasEspeciais);


        //itens para o campo
        adpTipoEmail.add("Casa");
        adpTipoEmail.add("Trabalho");
        adpTipoEmail.add("Outros");

        adpTipoTelefone.add("Celular");
        adpTipoTelefone.add("Trabalho");
        adpTipoTelefone.add("Casa");
        adpTipoTelefone.add("Principal");
        adpTipoTelefone.add("Outros");

        adpTipoEndereco.add("Casa");
        adpTipoEndereco.add("Trabalho");
        adpTipoEndereco.add("Outros");

        adpTipoDatasEspeciais.add("Aniversário");
        adpTipoDatasEspeciais.add("Data Comemorativa");
        adpTipoDatasEspeciais.add("Outros");

        ExibeDataListener listener = new ExibeDataListener();

        edtDatasEspeciais.setOnClickListener(listener);
        edtDatasEspeciais.setOnFocusChangeListener(listener);



        Bundle bundle = getIntent().getExtras();

        //item já criado
        if((bundle != null) && (bundle.containsKey("CONTATO"))){

            contato = (Contato)bundle.getSerializable("CONTATO"); //de objeto serializable para Contato
            preencheDados();

        }
        else{                   //item a ser criado
            contato = new Contato();
        }

        contato = new Contato();

        try {

            dataBase = new DataBase(this);

            connect = dataBase.getWritableDatabase();

            repositorioContato = new RepositorioContato(connect);

        }catch (SQLException ex){
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setMessage("Erro ao criar o banco: " + ex.getMessage());
            dlg.setNeutralButton("OK",null);
            dlg.show();
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_act_cad_contatos, menu);


        return super.onCreateOptionsMenu(menu);
    }


    //opções de menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.mni_acao1:

                if(contato.getId() == 0){
                    inserir();
                }
                finish();

                break;

            case R.id.mni_acao2:

                break;

        }

        return super.onOptionsItemSelected(item);
    }


    private void preencheDados(){

        edtNome.setText(contato.getNome());
        edtTelefone.setText(contato.getTelefone());
        spnTipoTelefone.setSelection(Integer.parseInt(contato.getTipoTelefone()));
        edtEmail.setText(contato.getEmail());
        spnTipoEmail.setSelection(Integer.parseInt(contato.getTipoEmail()));
        edtEndereco.setText(contato.getEndereco());
        spnTipoEndereco.setSelection(Integer.parseInt(contato.getEndereco()));

        DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM);
        String dt = format.format(contato.getDatasEspeciais());

        edtDatasEspeciais.setText(dt);
        spnTipoDatasEspeciais.setSelection(Integer.parseInt(contato.getTipoDatasEspeciais()));
        edtGrupos.setText(contato.getGrupos());

    }


    private void inserir(){


        try {


            contato.setNome(edtNome.getText().toString());
            contato.setTelefone(edtTelefone.getText().toString());
            contato.setEmail(edtEmail.getText().toString());
            contato.setEndereco(edtEndereco.getText().toString());



            contato.setGrupos(edtGrupos.getText().toString());

            contato.setTipoTelefone( String.valueOf(spnTipoTelefone.getSelectedItemPosition()) );
            contato.setTipoEmail(String.valueOf(spnTipoEmail.getSelectedItemPosition()));
            contato.setTipoEndereco(String.valueOf(spnTipoEndereco.getSelectedItemPosition()));
            contato.setTipoDatasEspeciais(String.valueOf(spnTipoDatasEspeciais.getSelectedItemPosition()));

            repositorioContato.inserir(contato);

        }catch (Exception ex)
        {
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setMessage("Erro ao inserir os dados: " + ex.getMessage());
            dlg.setNeutralButton("OK",null);
            dlg.show();
         }
    }


    private void exibeData(){

        //para exibir data atual do sistema
        Calendar calendar = Calendar.getInstance();

        int ano = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        //classe responsável por exibir a data para o usuário
        DatePickerDialog dlg = new DatePickerDialog(this,new SelecionaDataListener(),ano,mes,dia);
        dlg.show();

    }

    private class ExibeDataListener implements View.OnClickListener, View.OnFocusChangeListener{

        @Override
        public void onClick(View v) {
            exibeData();
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus) {
                exibeData();
            }
        }
    }


    private class SelecionaDataListener implements DatePickerDialog.OnDateSetListener{


        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            Calendar calendar = Calendar.getInstance();

            calendar.set(year,monthOfYear,dayOfMonth);

            Date data = calendar.getTime();

            DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM); //para formato numério usar SHORT
            String dt = format.format(data);


            edtDatasEspeciais.setText(dt);

            contato.setDatasEspeciais(data);




        }
    }

}
