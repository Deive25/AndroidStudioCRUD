package com.example.myapplication;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;

import java.io.ByteArrayOutputStream;


public class MainActivity extends AppCompatActivity {
    private EditText nome;
    private EditText cpf;
    private EditText telefone;
    private Button btnTakePhoto;
    private Aluno aluno = null;
    private AlunoDao dao;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView imageView;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private CheckBox checkboxAtivo;
    private RadioGroup radioGroupCurso;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        nome = findViewById(R.id.editNome);
        cpf = findViewById(R.id.editCPF);
        telefone = findViewById(R.id.editTelefone);
        imageView = findViewById(R.id.imageView);
        btnTakePhoto = findViewById(R.id.btnTakePhoto);
        checkboxAtivo = findViewById(R.id.checkboxAtivo);
        radioGroupCurso = findViewById(R.id.radioGroupCurso);

        Intent it = getIntent(); //pega intenção

        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tirarFoto();
            }
        });

        if(it.hasExtra("aluno")){
            aluno = (Aluno) it.getSerializableExtra("aluno");
            nome.setText(aluno.getNome().toString());
            cpf.setText(aluno.getCpf());
            telefone.setText(aluno.getTelefone());
            checkboxAtivo.setChecked(aluno.isAtivo());
            String tipoCurso = aluno.getTipoCurso();
            if ("Graduação".equals(tipoCurso)) {
                radioGroupCurso.check(R.id.radioGraduacao);
            } else if ("Pós-graduação".equals(tipoCurso)) {
                radioGroupCurso.check(R.id.radioPosGraduacao);
            }

            byte[] fotoBytes = aluno.getFotoBytes();
            if (fotoBytes != null && fotoBytes.length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(fotoBytes, 0, fotoBytes.length);
                imageView.setImageBitmap(bitmap);
            }
        }

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imageView.setImageBitmap(imageBitmap);
            }
        });

        // Criando uma instância da classe AlunoDao para interagir com o banco de dados
        dao = new AlunoDao(this);
    }

    public void salvar(View view){
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();

        if(aluno==null) {
            aluno = new Aluno();
            aluno.setNome(nome.getText().toString());
            aluno.setCpf(cpf.getText().toString());
            aluno.setTelefone(telefone.getText().toString());

            boolean isAtivo = checkboxAtivo.isChecked();
            int selectedCursoId = radioGroupCurso.getCheckedRadioButtonId();
            String tipoCurso = "";

            if (selectedCursoId == R.id.radioGraduacao) {
                tipoCurso = "Graduação";
            } else if (selectedCursoId == R.id.radioPosGraduacao) {
                tipoCurso = "Pós-graduação";
            }

            aluno.setAtivo(isAtivo);
            aluno.setTipoCurso(tipoCurso);

            if (drawable != null) {
                Bitmap bitmap = drawable.getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] fotoBytes = stream.toByteArray();
                aluno.setFotoBytes(fotoBytes);
            }
            long id = dao.inserir(aluno); //inserir o aluno
            Toast.makeText(this,"Aluno Inserido com sucesso!! com id: ",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            aluno.setNome(nome.getText().toString());
            aluno.setCpf(cpf.getText().toString());
            aluno.setTelefone(telefone.getText().toString());

            boolean isAtivo = checkboxAtivo.isChecked();
            int selectedCursoId = radioGroupCurso.getCheckedRadioButtonId();
            String tipoCurso = "";

            if (selectedCursoId == R.id.radioGraduacao) {
                tipoCurso = "Graduação";
            } else if (selectedCursoId == R.id.radioPosGraduacao) {
                tipoCurso = "Pós-graduação";
            }

            aluno.setAtivo(isAtivo);
            aluno.setTipoCurso(tipoCurso);

            if (drawable != null) {
                Bitmap bitmap = drawable.getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] fotoBytes = stream.toByteArray();
                aluno.setFotoBytes(fotoBytes);
            }
            dao.atualizar(aluno);
            Toast.makeText(this,"Aluno Atualizado!! com id: ", Toast.LENGTH_SHORT).show();
        }
    }

    public void irParaListar(View view) {
        Intent intent = new Intent(this, ListarAlunosActivity.class);
        try {
            startActivity(intent);
        } catch (Exception e){
            Log.d("NumberGenerated", e.toString());
        }
    }

    private void startCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraLauncher.launch(takePictureIntent);

    }

    public void tirarFoto() {
        checkCameraPermissionAndStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }

    private void checkCameraPermissionAndStart() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            startCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                Toast.makeText(this, "A permissão da câmera é necessária para tirar fotos.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}