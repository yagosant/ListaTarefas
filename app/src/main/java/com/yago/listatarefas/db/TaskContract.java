package com.yago.listatarefas.db;

import android.provider.BaseColumns;

public class TaskContract {

    //classe com as informações da tabela

    //informações do DB
    public static final String DB_NAME = "br.com.yago.listadetarefas";
    public static final int DB_VERSION = 1;
    public static final String TABLE = "tarefas";

    //info das colunas da tabela
    public class Columns{
        public static final String TAREFA = "tarefa";
        public static final String PRAZO = "prazo";
        public static final String _ID = BaseColumns._ID;
    }

}
