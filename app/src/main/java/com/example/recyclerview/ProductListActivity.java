package com.example.recyclerview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.recyclerview.databinding.ActivityProductListBinding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

public class ProductListActivity extends AppCompatActivity {

    ActivityProductListBinding binding;
    String mail;
    ArrayList<Product> products;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.exit:
                Intent intent = new Intent(ProductListActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Objects.requireNonNull(getSupportActionBar()).setTitle("PRODUCT PROTOTYPE");

        products = getProducts();



        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ProductListAdapter landmarkAdapter = new ProductListAdapter(products);
        binding.recyclerView.setAdapter(landmarkAdapter);



    }

    private ArrayList<Product> getProducts() {
        ArrayList<Product> tempArr = new ArrayList<>();

        try {
            InputStreamReader pro = new InputStreamReader(getAssets().open("products.csv"));
            BufferedReader reader = new BufferedReader(pro);

            String[] productCol = reader.readLine().split(",");

            while (reader.ready()) {
                String[] line = reader.readLine().split(",");
                Product temp = new Product();
                temp.setId(Integer.parseInt(line[0]));
                temp.setName(line[1]);
                temp.setPrice(Integer.parseInt(line[2]));
                temp.setAvailable(line[3]);
                temp.setDescription(line[4]);

                tempArr.add(temp);

            }


        } catch (IOException e) {
            e.printStackTrace();
        }


        return tempArr;
    }

    public void openProduct(View view) {
        Intent intent = new Intent(ProductListActivity.this, ProductListActivity.class);
        startActivity(intent);
        finish();
    }

    public void openProfile(View view) {
        Bundle extras = getIntent().getExtras();
        mail = "";
        if(extras != null) {
            mail = extras.getString("mail");
        }
        Intent intent = new Intent(ProductListActivity.this, ProfileActivity.class);
        intent.putExtra("mail", mail);
        startActivity(intent);
        finish();
    }


}