package com.example.recyclerview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.recyclerview.databinding.ActivityProfileBinding;


import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding binding;
    String mail, fullName = "", title = "";
    SQLiteDatabase db;

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
                Intent intent = new Intent(ProfileActivity.this,HomeActivity.class);
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
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Objects.requireNonNull(getSupportActionBar()).setTitle("PRODUCT PROTOTYPE");

        Bundle extras = getIntent().getExtras();
        mail = "";
        if(extras != null) {
            mail = extras.getString("mail");
        }

        db = this.openOrCreateDatabase("User", MODE_PRIVATE,null);
        Cursor c = db.rawQuery("SELECT * FROM User",null);

        int mailIndex = c.getColumnIndex("Email");
        int fullNameIndex = c.getColumnIndex("FullName");
        int titleIndex = c.getColumnIndex("Title");

        while(c.moveToNext()) {
            if(mail.equals(c.getString(mailIndex))) {
                fullName = c.getString(fullNameIndex);
                title = c.getString(titleIndex);
            }
        }

        binding.nameProfileEdit.setHint(fullName);
        binding.emailProfileEdit.setHint(mail);
        binding.titleProfileEdit.setHint(title);

    }

    public void saveProfile(View view) {
        String fullNameN = binding.nameProfileEdit.getText().toString();
        String emailN = binding.emailProfileEdit.getText().toString();
        String titleN = binding.titleProfileEdit.getText().toString();

        ContentValues val = new ContentValues();

        if(!fullName.equals(fullNameN) && fullNameN.length() != 0) {
            val.put("FullName", fullNameN);
            fullNameN = fullNameN.toLowerCase().trim();
            System.out.println("full name trim: " +fullNameN);
        }
        if(!mail.equals(emailN) && emailN.length() != 0) {
            val.put("Email", emailN);
        }
        if(!title.equals(titleN) && titleN.length() != 0) {
            val.put("Title", titleN);
        }
        else {
            val.put("FullName", fullName);
            val.put("Email", mail);
            val.put("Title", title);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("UYARI!");
        builder.setMessage("Değişiklikleri kaydetmek istediğinizden emin misiniz?");
        builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                int updt = db.update("User",val,"FullName = ?",new String[]{fullName});
                System.out.println("updt: " + updt);
                printDB();
                Toast.makeText(ProfileActivity.this, "evet e basıldı", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }

    public void openProduct(View view) {
        Intent intent = new Intent(ProfileActivity.this, ProductListActivity.class);
        startActivity(intent);
        finish();
    }

    public void openProfile(View view) {
        Intent intent = new Intent(ProfileActivity.this,ProfileActivity.class);
        intent.putExtra("mail", mail);
        startActivity(intent);
        finish();
    }

    private void printDB() {
        db = this.openOrCreateDatabase("User", MODE_PRIVATE,null);
        Cursor c = db.rawQuery("SELECT * FROM User",null);

        int mailIndex = c.getColumnIndex("Email");
        int fullNameIndex = c.getColumnIndex("FullName");
        int titleIndex = c.getColumnIndex("Title");

        while(c.moveToNext()) {
            System.out.println("full name: " + c.getString(fullNameIndex));
            System.out.println("mail: " + c.getString(mailIndex));
            System.out.println("title: " + c.getString(titleIndex));
        }
    }
}