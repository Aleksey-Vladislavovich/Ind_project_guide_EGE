package com.example.ver3;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<RecyclerItem> listItems;
    private Context mContext;

    public MyAdapter(List<RecyclerItem> listItems, Context mContext) {
        this.listItems = listItems;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        final RecyclerItem itemList = listItems.get(position);
        holder.txtTitle.setText(itemList.getTitle());
        holder.txtDescription.setText(itemList.getDescription());
        holder.txtOptionDigit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Display option menu

                PopupMenu popupMenu = new PopupMenu(mContext, holder.txtOptionDigit);
                popupMenu.inflate(R.menu.option_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        String SubjName = listItems.get(position).getTitle().toString();
                        switch (item.getItemId()) {
                            case R.id.mnu_item_save:
//Вот здесь переход на сайт
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://edunews.ru/ege/"));;
                                switch (SubjName) {
                                    case "Математика":
                                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://edunews.ru/ege/matematika/"));
                                        break;
                                    case "Информатика":
                                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://edunews.ru/ege/informatika/"));
                                        break;
                                    case "Русский язык":
                                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://edunews.ru/ege/russkiy-yazyk/"));
                                        break;
                                    case "Физика":
                                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://edunews.ru/ege/fizika/"));
                                        break;
                                    case "История":
                                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://edunews.ru/ege/istoriya/"));
                                        break;
                                    case "Обществознание":
                                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://edunews.ru/ege/obschestvoznanie/"));
                                        break;
                                    case "Химия":
                                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://edunews.ru/ege/himiya/"));
                                        break;
                                    case "Биология":
                                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://edunews.ru/ege/biologiya/"));
                                        break;
                                }
                                mContext.startActivity(intent);
                                Toast.makeText(mContext, "Подробнее", Toast.LENGTH_LONG).show();
                                break;
                            case R.id.mnu_item_delete:
//Вот здесь удаление из базы предмета вторым потоком!!!!

                                //Delete item
                                listItems.remove(position);
                                //Создание потока
                                Thread myThready = new Thread(new Runnable()
                                {
                                    public void run() //Этот метод будет выполняться в побочном потоке
                                    {
                                        DelDBSubj(SubjName);
                                    }
                                });
                                myThready.start();	//Запуск потока


//                                MainActivity ac = (MainActivity) mContext;
//                                ac.DelDBSubj(SubjName);
                                //Получаем имя предмета, которого удалим.
                                //String SubjName = listItems.get(position).getTitle().toString();
                                //Не забудь про потоки!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                                //лучше всего подавать значение удаленного предмета в MainActivity,
                                //а там из Base получать имя пользователя, а потом там же (в MainActivuty)
                                //работать с базой, из нее получать строку, удалять, изменять, дописывать.


                                notifyDataSetChanged();
                                Toast.makeText(mContext, "Deleted", Toast.LENGTH_LONG).show();
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtTitle;
        public TextView txtDescription;
        public TextView txtOptionDigit;
        public ViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            txtDescription = (TextView) itemView.findViewById(R.id.txtDescription);
            txtOptionDigit = (TextView) itemView.findViewById(R.id.txtOptionDigit);
        }
    }
//Это тестовый блок для чтения всех записей базы, для программы он не нужен!
//    public void DelDBSubj (String Subj){
//        DBHelperUsers db = new DBHelperUsers(mContext); //Здесь должна быть не новая база, а старая
//
//        SQLiteDatabase database = db.getWritableDatabase();
//
//        ContentValues contentValues = new ContentValues();
//
//        //Организуем поиск по базе, получим на выходе id строки в которой нужно изменить предмет.
//        Cursor cursor = database.query(DBHelperUsers.TABLE_CONTACTS, null, null, null, null, null, null);
//
//        if (cursor.moveToFirst()) {
//            int idIndex = cursor.getColumnIndex(DBHelperUsers.KEY_ID);
//            int nameIndex = cursor.getColumnIndex(DBHelperUsers.KEY_NAME);
//            int emailIndex = cursor.getColumnIndex(DBHelperUsers.KEY_MATH);
//            do {
//                Log.d("mLog", "ID = " + cursor.getInt(idIndex) +
//                        ", name = " + cursor.getString(nameIndex) +
//                        ", math = " + cursor.getString(emailIndex));
//            } while (cursor.moveToNext());
//        } else
//            Log.d("mLog","0 rows");
//
//        cursor.close();
//        //database.delete(DBHelperUsers.TABLE_CONTACTS, null, null);
//    }


    public void DelDBSubj(String Subj){


        String[] SubjectNamesArray = {"Математика", "Информатика", "Русский язык", "Физика",
                "История", "Обществознание", "Химия", "Биология"};
        EditText ETemail = EmailPasswordActivity.getETemail();
        String ETemailstr = ETemail.getText().toString();

        DBHelperUsers db = new DBHelperUsers(mContext); //Здесь должна быть не новая база, а старая

//        SQLiteDatabase database = db.getWritableDatabase();
        SQLiteDatabase database = db.getReadableDatabase();
        ContentValues contentValues = new ContentValues();

        //Организуем поиск по базе, получим на выходе id строки в которой нужно изменить предмет.
        Cursor cursor = database.query(DBHelperUsers.TABLE_CONTACTS, null, null, null, null, null, null);

        int etId = 0;

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelperUsers.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DBHelperUsers.KEY_NAME);
            do {
                if (cursor.getString(nameIndex).equals(ETemailstr)) {
                    etId =cursor.getInt(idIndex);
                    break;
                }
            } while (cursor.moveToNext());
        }



        //Обновление строки базы данных по id и поменяем значение в нужном столбце

        String id =  Integer. toString(etId);
        switch (Subj){
            case "Математика":
                contentValues.put(DBHelperUsers.KEY_MATH, "false");
                break;
            case "Информатика":
                contentValues.put(DBHelperUsers.KEY_INF, "false");
                break;
            case "Русский язык":
                contentValues.put(DBHelperUsers.KEY_RUS, "false");
                break;
            case "Физика":
                contentValues.put(DBHelperUsers.KEY_PHY, "false");
                break;
            case "История":
                contentValues.put(DBHelperUsers.KEY_HIS, "false");
                break;
            case "Обществознание":
                contentValues.put(DBHelperUsers.KEY_SOC, "false");
                break;
            case "Химия":
                contentValues.put(DBHelperUsers.KEY_CHI, "false");
                break;
            case "Биология":
                contentValues.put(DBHelperUsers.KEY_BIO, "false");
                break;
        }

        int updCount = database.update(DBHelperUsers.TABLE_CONTACTS, contentValues, DBHelperUsers.KEY_ID + "= ?", new String[] {id});
        Log.d("mLog", "updated rows count = " + updCount);
        try {
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
            Log.d("mLog", "ID = " + cursor.getInt(idIndex) +
                    ", name = " + cursor.getString(nameIndex) +
                    ", math = " + cursor.getString(mathIndex) +
                    ", inf = " + cursor.getString(infIndex) +
                    ", rus = " + cursor.getString(rusIndex) +
                    ", phy = " + cursor.getString(phyIndex) +
                    ", his = " + cursor.getString(hisIndex) +
                    ", soc = " + cursor.getString(socIndex) +
                    ", chi = " + cursor.getString(chiIndex) +
                    ", bio = " + cursor.getString(bioIndex));

        }catch (Exception e){
            Log.d("mLog","0 rows");
        }



        cursor.close();

    }
}