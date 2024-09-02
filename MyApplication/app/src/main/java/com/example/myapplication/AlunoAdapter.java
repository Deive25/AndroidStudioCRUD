package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class AlunoAdapter extends ArrayAdapter<Aluno> {
    private Context context;
    private List<Aluno> alunos;

    public AlunoAdapter(Context context, List<Aluno> alunos) {
        super(context, R.layout.list_item, alunos);
        this.context = context;
        this.alunos = alunos;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_item, parent, false);
        }

        TextView itemText = convertView.findViewById(R.id.text_item);
        TextView subitemText = convertView.findViewById(R.id.text_subitem);
        ImageView imageView = convertView.findViewById(R.id.image_view);

        Aluno aluno = alunos.get(position);
        itemText.setText(aluno.getNome());
        subitemText.setText(aluno.toString());

        byte[] fotoBytes = aluno.getFotoBytes();
        if (fotoBytes != null && fotoBytes.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(fotoBytes, 0, fotoBytes.length);
            imageView.setImageBitmap(bitmap);
        }

        return convertView;
    }
}


