package com.yago.listatarefas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.yago.listatarefas.db.TaskContract;
import com.yago.listatarefas.db.TaskDBHelper;

public class MainActivity extends AppCompatActivity {
    private TaskDBHelper helper;
    private ListView listaTarefas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper = new TaskDBHelper(this);
        listaTarefas = findViewById(R.id.listaTarefas);

        //apagar um item após clicar por um tempo
        listaTarefas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                TextView textoTarefa = view.findViewById(R.id.textoTarefa);
                String tarefa = textoTarefa.getText().toString();

                //procura no bd para apagar
                String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
                        TaskContract.TABLE, TaskContract.Columns.TAREFA, tarefa);

                SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
                sqLiteDatabase.execSQL(sql);

                //exibe a msg
                Toast.makeText(MainActivity.this, "Tarefa excluida com sucesso!", Toast.LENGTH_SHORT).show();
                //atualiza a lista
                updateUI();
                return false;
            }
        });

        Button btnAdd = findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTarefa();
            }
        });

        updateUI();

    }


    //metodos abaixo
    private void updateUI() {
        //pega os dados do bd e exibe no listview
        SQLiteDatabase sqLiteDatabase = helper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(TaskContract.TABLE,
                new String[]{TaskContract.Columns._ID, TaskContract.Columns.TAREFA,TaskContract.Columns.PRAZO},
                null,null,null,null,null);

        //criar um adaptador para a lista
        //ele passa os itens do cursos e coloca na lista
        SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(
                this, R.layout.celula_tarefa, cursor,
                new String[]{TaskContract.Columns.TAREFA,TaskContract.Columns.PRAZO},
                new int[]{R.id.textoTarefa, R.id.textoPrazo}, 0
        );

        listaTarefas.setAdapter(listAdapter);
    }

    private void addTarefa() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //setando o layout novo para o alerta
        View alertView = getLayoutInflater().inflate(R.layout.alert_tarefa, null);

        //setando no alert os textos preenchidos, dentro de listner sempre usa o final
        final EditText textoTarefa = alertView.findViewById(R.id.textoTarefa);
        final EditText textoPrazo = alertView.findViewById(R.id.textoPrazo);

        //setando como layout o novo alert
        builder.setView(alertView);

        //criando os botoes
        builder.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
           //salvar a tarefa
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues values = new ContentValues();

                //lista de conteudo
                values.clear();
                values.put(TaskContract.Columns.TAREFA, textoTarefa.getText().toString());
                values.put(TaskContract.Columns.PRAZO, textoPrazo.getText().toString());

                //insere no vd
                db.insertWithOnConflict(TaskContract.TABLE,null, values, SQLiteDatabase.CONFLICT_IGNORE);

                updateUI();
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //cancelar
                dialog.dismiss();
            }
        });

        builder.create().show();


    }
}
