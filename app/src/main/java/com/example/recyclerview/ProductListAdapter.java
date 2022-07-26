package com.example.recyclerview;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerview.databinding.RecyclerRowBinding;

import java.util.ArrayList;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductListHolder> {

    ArrayList<Product> products;

    public ProductListAdapter(ArrayList<Product> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public ProductListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerRowBinding recyclerRowBinding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ProductListHolder(recyclerRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListHolder holder, int position) {
        holder.binding.nameRowText.setText(products.get(position).getName());
        holder.binding.priceRowText.setText(String.valueOf(products.get(position).getPrice()));

        String bool = products.get(position).getAvailable();
        holder.binding.availableRowCheck.setChecked(bool.equals("true"));
        String name = products.get(position).getName();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(holder.itemView.getContext(), ProductDetailActivity.class);
                intent.putExtra("name", name);
                holder.itemView.getContext().startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return products.size();
    }


    public class ProductListHolder extends RecyclerView.ViewHolder {
        private RecyclerRowBinding binding;

        public ProductListHolder(RecyclerRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
