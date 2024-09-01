package com.example.myapplication;

import java.io.Serializable;

public class Pagamento implements Serializable {
    private Integer id;
    private Integer alunoId;
    private Double valor;
    private String data;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Integer alunoId) {
        this.alunoId = alunoId;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ID: " + id + " Data: " + data + " Valor: " + valor ;
    }
}
