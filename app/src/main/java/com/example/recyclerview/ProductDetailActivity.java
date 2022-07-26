package com.example.recyclerview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.recyclerview.databinding.ActivityProductDetailBinding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

public class ProductDetailActivity extends AppCompatActivity {

    ActivityProductDetailBinding binding;

    String name = "";
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
                Intent intent = new Intent(ProductDetailActivity.this,HomeActivity.class);
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
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Objects.requireNonNull(getSupportActionBar()).setTitle("PRODUCT PROTOTYPE");

        products = getProducts();

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            name = extras.getString("name");
        }

        SQLiteDatabase db = this.openOrCreateDatabase("Product", MODE_PRIVATE,null);
        Cursor c = db.rawQuery("SELECT * FROM Product",null);

        int nameIndex = c.getColumnIndex("Name");
        int priceIndex = c.getColumnIndex("Price");
        int availableIndex = c.getColumnIndex("Available");
        int descriptionIndex = c.getColumnIndex("Description");

        String price = "", availabe = "", description = "";

        while(c.moveToNext()) {
            if(name.equals(c.getString(nameIndex))) {
                price = c.getString(priceIndex);
                availabe = c.getString(availableIndex);
                description = c.getString(descriptionIndex);
            }
        }
        binding.nameProductDetailEdit.setText(name);
        binding.productDetailCheckBox.setChecked(availabe.equals("true"));
        binding.priceProductDetailEdit.setText(price);
        binding.descriptionText.setText(description);

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

    public void saveProduct(View view) {


    }
}