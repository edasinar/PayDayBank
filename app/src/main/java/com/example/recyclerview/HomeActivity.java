package com.example.recyclerview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.recyclerview.databinding.ActivityHomeBinding;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        Objects.requireNonNull(getSupportActionBar()).setTitle("WELCOME");


        sharedPreferences = this.getSharedPreferences("prof_pref", Context.MODE_PRIVATE);
        if(sharedPreferences.getInt("isOpenDatabase",0) == 0){
            applySharedPref();
        }

    }

    public void login(View view) {

        String mail = binding.emailHomeEditText.getText().toString();
        System.out.println("home activity mail: " + mail);
        Boolean find = false;
        String password = "";

        SQLiteDatabase db = this.openOrCreateDatabase("User", MODE_PRIVATE,null);
        Cursor c = db.rawQuery("SELECT * FROM User",null);

        int mailIndex = c.getColumnIndex("Email");
        int passwordIndex = c.getColumnIndex("Password");

        while(c.moveToNext()) {
            if(mail.equals(c.getString(mailIndex))) {
                find = true;
                password = c.getString(passwordIndex);
            }
        }

        if(find && password.equals(binding.passwordHomeEdit.getText().toString())) {
            binding.passwordHomeEdit.setText("");
            binding.emailHomeEditText.setText("");
            Intent intent = new Intent(HomeActivity.this, ProductListActivity.class);
            intent.putExtra("mail", mail);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Hatalı Kullanıcı Adı ya da Şifre", Toast.LENGTH_LONG).show();
        }
    }

    public void register(View view) {
        Intent intent = new Intent(HomeActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    private void applySharedPref() {
        if (sharedPreferences.getInt("isOpenDatabase",0) == 0){
            createDB();
            sharedPreferences.edit().putInt("isOpenDatabase", 1).apply();
        }
    }

    private void createDB() {

        try {
            InputStreamReader products = new InputStreamReader(getAssets().open("products.csv"));
            InputStreamReader users = new InputStreamReader(getAssets().open("users.csv"));

            BufferedReader readerP = new BufferedReader(products);
            BufferedReader readerU = new BufferedReader(users);

            String[] productsCol = readerP.readLine().split(",");
            String[] usersCol = readerU.readLine().split(",");


            SQLiteDatabase productDB = this.openOrCreateDatabase("Product", MODE_PRIVATE, null);
            SQLiteDatabase userDB = this.openOrCreateDatabase("User", MODE_PRIVATE, null);

            productDB.execSQL("CREATE TABLE IF NOT EXISTS Product ("
                    + productsCol[0] + " INTEGER PRIMARY KEY,"
                    + productsCol[1] + " VARCHAR,"
                    + productsCol[2] + " INTEGER,"
                    + productsCol[3] + " VARCHAR,"
                    + productsCol[4] + " TEXT)");

            userDB.execSQL("CREATE TABLE IF NOT EXISTS User ("
                    + usersCol[0] + " INTEGER PRIMARY KEY,"
                    + usersCol[1] + " VARCHAR,"
                    + usersCol[2] + " VARCHAR,"
                    + usersCol[3] + " VARCHAR,"
                    + usersCol[4] + " VARCHAR)");


            while(readerP.ready()) {
                String[] temp =  readerP.readLine().split(",");
                ContentValues val = new ContentValues();
                val.put(productsCol[0], temp[0]);
                val.put(productsCol[1], temp[1]);
                val.put(productsCol[2], temp[2]);
                val.put(productsCol[3], temp[3]);
                val.put(productsCol[4], temp[4]);

                productDB.insert("Product", null, val);
            }
            while(readerU.ready()) {
                String[] temp =  readerU.readLine().split(",");
                ContentValues val = new ContentValues();
                val.put(usersCol[0], temp[0]);
                val.put(usersCol[1], temp[1]);
                val.put(usersCol[2], temp[2]);
                val.put(usersCol[3], temp[3]);
                val.put(usersCol[4], temp[4]);

                userDB.insert("User", null, val);
            }

            readerP.close();
            readerU.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}