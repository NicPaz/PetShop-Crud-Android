package com.example.exbd;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.exbd.databinding.ActivityEditPetBinding;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EditPetActivity extends AppCompatActivity {

    private AppDatabase db;
    private ExecutorService executorService;
    private Pet pet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        ActivityEditPetBinding binding = ActivityEditPetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = AppDatabase.getDatabase(getApplicationContext());

        executorService = Executors.newSingleThreadExecutor();

        int petId = getIntent().getIntExtra("pet_id", -1);

        if (petId != -1) {
            executorService.execute(() -> {
                pet = db.petDao().SelectId(petId);
                runOnUiThread(() -> {
                    if (pet != null) {
                        binding.editNome.setText(pet.getNome());
                        binding.editRaca.setText(pet.getRaca());
                        binding.editPeso.setText(String.valueOf(pet.getPeso()));
                        binding.editIdade.setText(String.valueOf(pet.getIdade()));
                    }
                });
            });
        }

        binding.btnAtualizar.setOnClickListener(new View.OnClickListener() {
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
                    Toast.makeText(EditPetActivity.this, "Valor inválido!", Toast.LENGTH_SHORT).show();
                    return;
                }

                executorService.execute(() -> {
                    pet.setNome(nome);
                    pet.setRaca(raca);
                    pet.setPeso(peso);
                    db.petDao().atualizar(pet);

                    // Voltar à thread principal para atualizar a UI
                    runOnUiThread(() -> {
                        Toast.makeText(EditPetActivity.this, "Pet atualizado!", Toast.LENGTH_SHORT).show();
                        finish();
                    });
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