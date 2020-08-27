package com.example.csvsqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DataBase extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "sqlitedb.db";
    public final static String TABLE_NAME = "datatable";
    public static final String COL_1 = "TITLE";
    public static final String COL_2 = "LEVEL";
    public static final String COL_3 = "LANGUAGE";
    public static final String COL_4 = "QUALITY";


    public DataBase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public DataBase(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DataBase(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }


    @Override
    public void onCreate(SQLiteDatabase sqdb) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COL_1 + " TEXT," + COL_2 + " TEXT,"
                + COL_3 + " TEXT,"
                + COL_4 + " TEXT" + ")";
        sqdb.execSQL(CREATE_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqdb, int oldVersion, int newVersion) {
        sqdb.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqdb);
    }

    public boolean insertData(String title, String level, String language,
                              String quality) {
        SQLiteDatabase sqdb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, title);
        contentValues.put(COL_2, level);
        contentValues.put(COL_3, language);
        contentValues.put(COL_4, quality);
        Long result = sqdb.insert(TABLE_NAME, null, contentValues);
        if (result == -1) return true;

        return false;
    }

    public Cursor getData(String id) {
        SQLiteDatabase sqdb = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE ID='" + id + "'";
        Cursor cursor = sqdb.rawQuery(query, null);
        return cursor;
    }

    public Cursor getAllData() {
        SQLiteDatabase sqdb = this.getWritableDatabase();
        Cursor cursor = sqdb.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return cursor;
    }
//  Read database by each items------
    public List getDataJArry(ArrayList<String> itemArray) {
        SQLiteDatabase sqdb = this.getWritableDatabase();
        List resultList = new ArrayList();
        String strQry = "";
        for (int i = 0; i < itemArray.size(); i++) {
            strQry += itemArray.get(i);
            if (i < itemArray.size() - 1)
                strQry += ", ";
        }
        //reading each item query
        Cursor cursor = sqdb.rawQuery(new StringBuilder().append("select ").append(strQry).append(" from ").append(TABLE_NAME).toString(), null);

        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String res1 = "";
                    String[] row = new String[4];
                    ArrayList<String> arrList = new ArrayList<>();
                    if (itemArray.contains(COL_1)) {
                        arrList.add(cursor.getString(0) + "");
                    }
                    if (itemArray.contains(COL_2)) {
                        arrList.add(cursor.getString(1) + "");
                    }
                    if (itemArray.contains(COL_3)) {
                        arrList.add(cursor.getString(2) + "");
                    }
                    if (itemArray.contains(COL_4)) {
                        arrList.add(cursor.getString(3) + "");
                    }
                    row = arrList.toArray(new String[arrList.size()]);
                    resultList.add(row);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return resultList;
    }
}
