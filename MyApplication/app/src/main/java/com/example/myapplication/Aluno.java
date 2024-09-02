package com.example.myapplication;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Aluno implements Serializable {
    private Integer id;
    private String nome;
    private String cpf;
    private String telefone;
    private byte[] fotoBytes;

    private boolean ativo;
    private String tipoCurso;

    public boolean isAtivo() { return ativo; }

    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public String getTipoCurso() { return tipoCurso; }

    public void setTipoCurso(String tipoCurso) { this.tipoCurso = tipoCurso; }

    public byte[] getFotoBytes() { return fotoBytes; }

    public void setFotoBytes(byte[] fotoBytes) { this.fotoBytes = fotoBytes; }

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }

    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }

    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getTelefone() { return telefone; }

    public void setTelefone(String telefone) { this.telefone = telefone; }

    @Override
    public String toString() {
        return "CPF: " + cpf + "\nTelefone: " + telefone;
    }
}
