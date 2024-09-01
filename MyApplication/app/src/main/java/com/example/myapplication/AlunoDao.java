package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class AlunoDao {
    private Conexao conexao;
    private SQLiteDatabase banco;

    public AlunoDao(Context context) {
        conexao = new Conexao(context); //criei uma conexão
        banco = conexao.getWritableDatabase(); //iniciem um banco de dados para escrita
    }

    public long inserir(Aluno aluno) {
        ContentValues values = new ContentValues();
        values.put("nome", aluno.getNome());
        values.put("cpf", aluno.getCpf());
        values.put("telefone", aluno.getTelefone());
        values.put("foto_bytes", aluno.getFotoBytes());
        values.put("ativo", aluno.isAtivo() ? 1 : 0);
        values.put("tipo_curso", aluno.getTipoCurso());

        return banco.insert("aluno", null, values);
    }

    public List<Aluno> listar() {
        List<Aluno> alunos = new ArrayList<>();
        Cursor cursor = banco.query("aluno", new String[]{"id", "nome", "cpf", "telefone", "foto_bytes", "ativo","tipo_curso"},
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            Aluno aluno = new Aluno();
            aluno.setId(cursor.getInt(0));
            aluno.setNome(cursor.getString(1));
            aluno.setCpf(cursor.getString(2));
            aluno.setTelefone(cursor.getString(3));
            aluno.setFotoBytes(cursor.getBlob(4));
            aluno.setAtivo(cursor.getInt(5) == 1);
            aluno.setTipoCurso(cursor.getString(6));

            alunos.add(aluno);
        }

        return alunos;
    }

    public void excluir(Aluno a){
        banco.delete("pagamento", "alunoId = ?", new String[]{a.getId().toString()});
        banco.delete("aluno", "id = ?", new String[]{a.getId().toString()});
    }

    public void atualizar(Aluno aluno){
        ContentValues values = new ContentValues(); //valores que irei inserir
        values.put("nome", aluno.getNome());
        values.put("cpf", aluno.getCpf());
        values.put("telefone", aluno.getTelefone());
        values.put("foto_bytes", aluno.getFotoBytes());
        values.put("ativo", aluno.isAtivo() ? 1 : 0);
        values.put("tipo_curso", aluno.getTipoCurso());
        banco.update("aluno", values, "id = ?", new String[]{aluno.getId().toString()});
    }
}