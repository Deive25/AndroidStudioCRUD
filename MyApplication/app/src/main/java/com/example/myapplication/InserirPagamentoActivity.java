package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class InserirPagamentoActivity extends AppCompatActivity {

    private EditText editTextValor;
    private EditText editTextData;
    private Button buttonInserir, buttonVoltar;
    private PagamentoDao pagamentoDao;
    private Aluno aluno;
    private Pagamento pagamento;
    private final Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inserir_pagamento);

        if(getIntent().hasExtra("aluno")){
            aluno = (Aluno) getIntent().getSerializableExtra("aluno");
            if (aluno.getId() == -1) {
                Toast.makeText(this, "Erro: Aluno não encontrado.", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        }

        if(getIntent().hasExtra("pagamento")){
            pagamento = (Pagamento) getIntent().getSerializableExtra("pagamento");
        }


        editTextValor = findViewById(R.id.editTextText);
        editTextData = findViewById(R.id.editTextDate);
        buttonInserir = findViewById(R.id.button2);
        buttonVoltar = findViewById(R.id.button5);

        pagamentoDao = new PagamentoDao(this);

        editTextData.setOnClickListener(v -> showDatePicker());

        if(getIntent().hasExtra("pagamento")){
            editTextValor.setText(pagamento.getValor().toString());
            editTextData.setText(pagamento.getData().toString());
        }

        buttonInserir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inserirPagamento();
            }
        });

        buttonVoltar.setOnClickListener(v -> finish());

        editTextData.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                v.clearFocus();
                showDatePicker();
            }
        });
        pagamentoDao = new PagamentoDao(this);
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String selectedDate = String.format("%02d/%02d/%d", dayOfMonth, monthOfYear + 1, year);
                        editTextData.setText(selectedDate);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    private void inserirPagamento() {
        String valorString = editTextValor.getText().toString();
        String data = editTextData.getText().toString();

        if (valorString.isEmpty() || data.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
            return;
        }
        long resultado;
        try {

            double valor = Double.parseDouble(valorString);
            if(pagamento == null) {
                Pagamento pagamentoNovo = new Pagamento();
                pagamentoNovo.setAlunoId(aluno.getId());
                pagamentoNovo.setValor(valor);
                pagamentoNovo.setData(data);
                resultado = pagamentoDao.inserir(pagamentoNovo);
                if (resultado != -1) {
                    Toast.makeText(this, "Pagamento inserido com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Erro ao inserir pagamento.", Toast.LENGTH_SHORT).show();
                }
            } else {
                pagamento.setAlunoId(pagamento.getAlunoId());
                pagamento.setValor(valor);
                pagamento.setData(data);
                resultado = pagamentoDao.atualizar(pagamento);
                if (resultado != -1) {
                    Toast.makeText(this, "Pagamento atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                    Intent it = new Intent(this, ListarPagamentosActivity.class);
                    aluno.setId(pagamento.getAlunoId());
                    it.putExtra("aluno",aluno);
                    try {
                        startActivity(it);
                    } catch (Exception e){
                        Log.d("NumberGenerated", e.toString());
                    }
                } else {
                    Toast.makeText(this, "Erro ao atualizar pagamento.", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Valor inválido.", Toast.LENGTH_SHORT).show();
        }
    }
}