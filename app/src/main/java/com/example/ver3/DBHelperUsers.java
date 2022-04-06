package com.example.ver3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelperUsers  extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "usersDb";
    public static final String TABLE_CONTACTS = "users";

    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_MATH = "math";
    public static final String KEY_INF = "inf";
    public static final String KEY_RUS = "rus";
    public static final String KEY_PHY = "phy";
    public static final String KEY_HIS = "his";
    public static final String KEY_SOC = "soc";
    public static final String KEY_CHI = "chi";
    public static final String KEY_BIO = "bio";

    public DBHelperUsers(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_CONTACTS + "(" + KEY_ID
                + " integer primary key," + KEY_NAME + " text," + KEY_MATH + " text," + KEY_INF
                + " text," + KEY_RUS + " text," + KEY_PHY + " text," + KEY_HIS + " text," + KEY_SOC
                + " text," + KEY_CHI + " text," + KEY_BIO + " text" + ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_CONTACTS);

        onCreate(db);

    }
}

