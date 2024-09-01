package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ListarPagamentosActivity extends AppCompatActivity {

    private ListView listView;
    private PagamentoDao dao;
    private List<Pagamento> pagamentos;
    private List<Pagamento> pagamentosFiltrados;
    private Button buttonVoltar;
    private Aluno aluno = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_pagamentos);

        listView = findViewById(R.id.listaPagamentos);
        buttonVoltar = findViewById(R.id.button);
        Intent it = getIntent();

        dao = new PagamentoDao(this);
        aluno = (Aluno) it.getSerializableExtra("aluno");

        pagamentos = dao.listarPorAluno(aluno.getId());
        pagamentosFiltrados = new ArrayList<>();
        pagamentosFiltrados.addAll(pagamentos);
        ArrayAdapter<Pagamento> adaptador = new ArrayAdapter<Pagamento>(this, android.R.layout.simple_list_item_1, pagamentos);

        listView.setAdapter(adaptador);
        registerForContextMenu(listView);

        buttonVoltar.setOnClickListener(v -> finish());
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater i = getMenuInflater();
        i.inflate(R.menu.menu_contexto_pagamentos, menu);
    }

    public void excluir(MenuItem item){
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo)
                item.getMenuInfo();
        final Pagamento pagamentoExcluir = pagamentosFiltrados.get(menuInfo.position);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Atenção")
                .setMessage("Realmente deseja excluir o pagamento?")
                .setNegativeButton("NÃO",null)
                .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pagamentosFiltrados.remove(pagamentoExcluir);
                        pagamentos.remove(pagamentoExcluir);
                        dao.excluir(pagamentoExcluir);
                        listView.invalidateViews();
                    }
                } ).create();
        dialog.show();
    }

    public void atualizar(MenuItem item){
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Pagamento pagamentoAtualizar = pagamentosFiltrados.get(menuInfo.position);
        Intent it = new Intent(this, InserirPagamentoActivity.class);
        it.putExtra("pagamento",pagamentoAtualizar);
        startActivity(it);
    }

}