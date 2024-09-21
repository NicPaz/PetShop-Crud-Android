package com.example.exbd;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.exbd.databinding.ActivityMainBinding;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements PetAdapter.OnItemClickListener {

    private AppDatabase db;
    private PetAdapter petAdapter;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(android.R.drawable.ic_menu_close_clear_cancel);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AuthorActivity.class);
                startActivity(intent);
            }
        });

        db = AppDatabase.getDatabase(getApplicationContext());

        executorService = Executors.newSingleThreadExecutor();

        binding.recyclerViewPet.setLayoutManager(new LinearLayoutManager(this));
        PetAdapter petAdapter = new PetAdapter(this);
        petAdapter.setOnItemClickListener(this);
        binding.recyclerViewPet.setAdapter(petAdapter);

        db.petDao().listar().observe(this, new Observer<List<Pet>>() {
            @Override
            public void onChanged(List<Pet> pets) {
                petAdapter.setPets(pets);
            }
        });


        binding.fabAdicionar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddPetActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_author) {
            Intent intent = new Intent(this, AuthorActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onItemClick(Pet pet) {
        Intent intent = new Intent(MainActivity.this, EditPetActivity.class);
        intent.putExtra("pet_id", pet.getId());
        startActivity(intent);
    }

    public void deletarPet(Pet pet) {
        executorService.execute(() -> {
            db.petDao().deletar(pet);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}