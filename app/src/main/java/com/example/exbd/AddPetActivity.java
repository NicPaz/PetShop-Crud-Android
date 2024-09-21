package com.example.exbd;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.exbd.databinding.ActivityAddPetBinding;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddPetActivity extends AppCompatActivity {

    private AppDatabase db;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        ActivityAddPetBinding binding = ActivityAddPetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = AppDatabase.getDatabase(getApplicationContext());


        executorService = Executors.newSingleThreadExecutor();


       binding.btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String nome = binding.editNome.getText().toString();
                final String raca = binding.editRaca.getText().toString();
                final double peso;
                final int idade;

                try {
                    peso = Double.parseDouble(binding.editPeso.getText().toString());
                    idade = Integer.parseInt(binding.editIdade.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(AddPetActivity.this, "Valor inválido!", Toast.LENGTH_SHORT).show();
                    return;
                }

                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            db.petDao().inserir(new Pet(nome, raca, peso, idade));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(AddPetActivity.this, "Pet salvo!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(AddPetActivity.this, "Erro ao salvar o pet!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });


            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}