package com.example.ver3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.annotations.PublicApi;

public class Base extends AppCompatActivity {

    DBHelperUsers dbHelperUsers;

    public DBHelperUsers getDbHelperUsers() {
        return dbHelperUsers;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        //Обращаемся к базе и ищем данные по ETemail.
        dbHelperUsers = new DBHelperUsers(this);

        SQLiteDatabase database = dbHelperUsers.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        Cursor cursor = database.query(DBHelperUsers.TABLE_CONTACTS, null, null, null, null, null, null);

        String eTemailStr;
        Intent intent = getIntent();
        eTemailStr = intent.getStringExtra("myStringVariableName");

        //Виджеты активности.
        //TextView textView = findViewById(R.id.textView2);
        //textView.setText(eTemailStr);
        Button butList = findViewById(R.id.button);
        butList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialogFragment myDialogFragment = new MyDialogFragment();
                FragmentManager manager = getSupportFragmentManager();

                FragmentTransaction transaction = manager.beginTransaction();
                myDialogFragment.show(transaction, "dialog");
            }
        });


        //Проверяем наличие полученного имени в базе.
        boolean flagUserInBase = false;
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelperUsers.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DBHelperUsers.KEY_NAME);
            do {
                if (cursor.getString(nameIndex).equals(eTemailStr)) {
                    flagUserInBase = true;
                    break;
                }
            } while (cursor.moveToNext());
        }


        // Если их нет или база пустая, то
        if (!flagUserInBase) {
            cursor.close();
            // вызываем диалог с флажками для выбора предметов
            MyDialogFragment myDialogFragment = new MyDialogFragment();
            FragmentManager manager = getSupportFragmentManager();

            FragmentTransaction transaction = manager.beginTransaction();
            myDialogFragment.show(transaction, "dialog");
        } else {
            //Если есть такой пользователь, то получаем информацию о нем.
            //Можно это сделать более элегантно через цикл, но пока так.
                int idIndex = cursor.getColumnIndex(DBHelperUsers.KEY_ID);
                int nameIndex = cursor.getColumnIndex(DBHelperUsers.KEY_NAME);
                int mathIndex = cursor.getColumnIndex(DBHelperUsers.KEY_MATH);
                int infIndex = cursor.getColumnIndex(DBHelperUsers.KEY_INF);
                int rusIndex = cursor.getColumnIndex(DBHelperUsers.KEY_RUS);
                int phyIndex = cursor.getColumnIndex(DBHelperUsers.KEY_PHY);
                int hisIndex = cursor.getColumnIndex(DBHelperUsers.KEY_HIS);
                int socIndex = cursor.getColumnIndex(DBHelperUsers.KEY_SOC);
                int chiIndex = cursor.getColumnIndex(DBHelperUsers.KEY_CHI);
                int bioIndex = cursor.getColumnIndex(DBHelperUsers.KEY_BIO);
                String name = cursor.getString(nameIndex);
                boolean[] checkedItemsArray = {false, false, false, false, false, false, false, false};
                checkedItemsArray[0] = cursor.getString(mathIndex).equals("true");
                checkedItemsArray[1] = cursor.getString(infIndex).equals("true");
                checkedItemsArray[2] = cursor.getString(rusIndex).equals("true");
                checkedItemsArray[3] = cursor.getString(phyIndex).equals("true");
                checkedItemsArray[4] = cursor.getString(hisIndex).equals("true");
                checkedItemsArray[5] = cursor.getString(socIndex).equals("true");
                checkedItemsArray[6] = cursor.getString(chiIndex).equals("true");
                checkedItemsArray[7] = cursor.getString(bioIndex).equals("true");
                cursor.close();

                startNextActivity(checkedItemsArray);

        }
    }
    //Дополнительная функция для перехода к рабочей активности с путеводителем.
    public void startNextActivity(boolean[] checkedItemsArray){
        Intent intent = new Intent(Base.this, MainActivity.class);
        intent.putExtra("myBoolName", checkedItemsArray);
        startActivity(intent);
    }
    //Отработка кнопки "готово" на диалоговом окне.
    public void okClicked(boolean[] checkedItemsArray) {
        //проверка выбора флажков.
        boolean flagEmpty = true;
        for (int i = 0; i < checkedItemsArray.length; i++) {
            if (checkedItemsArray[i]) {
                flagEmpty = false;
            }
        }
        //Если флажков нет, то возврат к выбору предметов.
        if (flagEmpty){
            Toast.makeText(getApplicationContext(), "Вы не выбрали ни одного предмета!",
                    Toast.LENGTH_LONG).show();
            MyDialogFragment myDialogFragment = new MyDialogFragment();
            FragmentManager manager = getSupportFragmentManager();
            //myDialogFragment.show(manager, "dialog");

            FragmentTransaction transaction = manager.beginTransaction();
            myDialogFragment.show(transaction, "dialog");
        } else{
            //Формируем строку в базе пользователей.
//            Toast.makeText(getApplicationContext(), "Все ок!",
//                    Toast.LENGTH_LONG).show();

            dbHelperUsers = new DBHelperUsers(this);

//            SQLiteDatabase database = dbHelperUsers.getWritableDatabase();
            SQLiteDatabase database = dbHelperUsers.getReadableDatabase();

            ContentValues contentValues = new ContentValues();

            Cursor cursor = database.query(DBHelperUsers.TABLE_CONTACTS, null, null, null, null, null, null);

            String eTemailStr;
            Intent intent = getIntent();
            eTemailStr = intent.getStringExtra("myStringVariableName");
            contentValues.put(DBHelperUsers.KEY_NAME, eTemailStr);
            contentValues.put(DBHelperUsers.KEY_MATH, (checkedItemsArray[0] ? "true" : "false"));
            contentValues.put(DBHelperUsers.KEY_INF, (checkedItemsArray[1] ? "true" : "false"));
            contentValues.put(DBHelperUsers.KEY_RUS, (checkedItemsArray[2] ? "true" : "false"));
            contentValues.put(DBHelperUsers.KEY_PHY, (checkedItemsArray[3] ? "true" : "false"));
            contentValues.put(DBHelperUsers.KEY_HIS, (checkedItemsArray[4] ? "true" : "false"));
            contentValues.put(DBHelperUsers.KEY_SOC, (checkedItemsArray[5] ? "true" : "false"));
            contentValues.put(DBHelperUsers.KEY_CHI, (checkedItemsArray[6] ? "true" : "false"));
            contentValues.put(DBHelperUsers.KEY_BIO, (checkedItemsArray[7] ? "true" : "false"));


            int etId = -1;

            if (cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex(DBHelperUsers.KEY_ID);
                int nameIndex = cursor.getColumnIndex(DBHelperUsers.KEY_NAME);
                do {
                    if (cursor.getString(nameIndex).equals(eTemailStr)) {
                        etId =cursor.getInt(idIndex);
                        break;
                    }
                } while (cursor.moveToNext());
            }

            if (etId == -1){
                //Если новый пользователь!
                //Формируем новую строку - пользователя в базе данных
                database.insert(DBHelperUsers.TABLE_CONTACTS, null, contentValues);
            }else{
                //Если изменилась информация о старом пользователе.
                //Обновление строки базы данных по id и поменяем значение в нужном столбце

                String id =  Integer. toString(etId);

                int updCount = database.update(DBHelperUsers.TABLE_CONTACTS, contentValues, DBHelperUsers.KEY_ID + "= ?", new String[] {id});
                Log.d("mLog", "updated rows count = " + updCount);
            }

            cursor.close();

            //переходим к путеводителю.
            //отправляем данные строки базы с пользователем ETemail на следующую активность
            startNextActivity(checkedItemsArray);
        }

    }

}