package com.example.ver3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<RecyclerItem> listItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean[] checkedItemsArray;
        Intent intent = getIntent();
        checkedItemsArray = intent.getBooleanArrayExtra("myBoolName");



        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItems = new ArrayList<>();



        //вписать наши предметы, удавлетворяющие выбору пользователя.
        String[] SubjectNamesArray = {"Математика", "Информатика", "Русский язык", "Физика",
                "История", "Обществознание", "Химия", "Биология"};
        for (int i = 0; i<8; i++) {
            if (checkedItemsArray[i]){
                listItems.add(new RecyclerItem("" + SubjectNamesArray[i], "Нажимай на три точки и узнай подробнее об этом экзамене!"));
            }
        }

        //Устанавливаем адаптер
        adapter = new MyAdapter(listItems, this);
        recyclerView.setAdapter(adapter);
    }


}