package com.example.exbd;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.PetViewHolder> {
    private List<Pet> pets = new ArrayList<>();
    private OnItemClickListener listener;
    private Context context;


    public interface OnItemClickListener {
        void onItemClick(Pet pet);
    }

    public PetAdapter(Context context) {
        this.context = context;
    }

    public void setPets(List<Pet> pets) {
        this.pets.clear();
        this.pets.addAll(pets);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public PetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pet, parent, false);
        return new PetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PetViewHolder holder, int position) {
        Pet pet = pets.get(position);
        holder.textNome.setText("Nome: " + pet.getNome());
        holder.textRaca.setText("Raça: " + pet.getRaca());
        holder.textPeso.setText(String.format("Peso: %.2f KG", pet.getPeso()));
        holder.textIdade.setText(String.format("Idade: %d KG", pet.getIdade()));

        // Clique no item para editar
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(pet);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Excluir Pet")
                    .setMessage("Tem certeza que deseja excluir este pet?")
                    .setPositiveButton("Sim", (dialog, which) -> {
                        if (context instanceof MainActivity) {
                            ((MainActivity) context).deletarPet(pet);
                        }
                    })
                    .setNegativeButton("Não", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return pets != null ? pets.size() : 0;
    }

    public static class PetViewHolder extends RecyclerView.ViewHolder {
        TextView textNome, textRaca, textPeso, textIdade;
        ImageButton btnDelete;

        public PetViewHolder(View itemView) {
            super(itemView);
            textNome = itemView.findViewById(R.id.textNome);
            textRaca = itemView.findViewById(R.id.textRaca);
            textPeso = itemView.findViewById(R.id.textPeso);
            textIdade = itemView.findViewById(R.id.textIdade);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
