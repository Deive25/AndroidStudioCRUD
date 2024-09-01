package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class PagamentoDao {
    private Conexao conexao;
    private SQLiteDatabase banco;

    public PagamentoDao(Context context) {
        conexao = new Conexao(context);
        banco = conexao.getWritableDatabase();
    }

    public long inserir(Pagamento pagamento) {
        ContentValues values = new ContentValues();
        values.put("alunoId", pagamento.getAlunoId());
        values.put("valor", pagamento.getValor());
        values.put("data", pagamento.getData());
        return banco.insert("pagamento", null, values);
    }

    public List<Pagamento> listarPorAluno(int alunoId) {
        List<Pagamento> pagamentos = new ArrayList<>();
        Cursor cursor = banco.query("pagamento", new String[]{"id", "alunoId", "valor", "data"},
                "alunoId = ?", new String[]{String.valueOf(alunoId)}, null, null, null);

        while (cursor.moveToNext()) {
            Pagamento pagamento = new Pagamento();
            pagamento.setId(cursor.getInt(0));
            pagamento.setAlunoId(cursor.getInt(1));
            pagamento.setValor(cursor.getDouble(2));
            pagamento.setData(cursor.getString(3));

            pagamentos.add(pagamento);
        }
        return pagamentos;
    }

    public long atualizar(Pagamento pagamento) {
        ContentValues values = new ContentValues();
        values.put("valor", pagamento.getValor());
        values.put("data", pagamento.getData());

        return banco.update("pagamento", values, "id = ?", new String[]{pagamento.getId().toString()});
    }

    public void excluir(Pagamento pagamento) {
        banco.delete("pagamento", "id = ?", new String[]{pagamento.getId().toString()});
    }
}
