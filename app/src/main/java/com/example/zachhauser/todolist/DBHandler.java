package com.example.zachhauser.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static android.R.attr.author;
import static com.example.zachhauser.todolist.R.id.toDoItem;

/**
 * Created by zachhauser on 2016-12-28.
 */

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ToDoDatabase.db";
    public static final String TABLE_TODO = "todolist";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ITEM = "_item";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_ITEMS = "CREATE TABLE " + TABLE_TODO +
                "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_ITEM + " TEXT" + ")";

        db.execSQL(CREATE_TABLE_ITEMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
        onCreate(db);
    }

    public void addToDoItem(ToDo toDo) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ITEM, toDo.getToDoItem());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_TODO, null, values);
        db.close();
    }

    public boolean deleteItem(String toDoItemText) {

        boolean result = false;

        String query = "Select * FROM " + TABLE_TODO + " WHERE " + COLUMN_ITEM + " = \"" + toDoItemText + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(query, null);

        ToDo item = new ToDo();

        if(c.moveToFirst()) {
            item.setId(Integer.parseInt(c.getString(0)));
            db.delete(TABLE_TODO, COLUMN_ITEM + " = ?", new String[] {String.valueOf(toDoItemText)});
            c.close();
            result = true;
        }
        db.close();
        return result;
    }

    public Cursor getItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_TODO, new String[] {COLUMN_ID, COLUMN_ITEM}, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
            return c;
        }
        else {
            return null;
        }
    }

    public List<ToDo> getAllItems() {

        List<ToDo> todoItems = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_TODO;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor toDoCursor = db.rawQuery(query, null);

        ToDo item;

        if(toDoCursor.moveToFirst()) {
            do {
                item = new ToDo();
                item.setId(Integer.parseInt(toDoCursor.getString(0)));
                item.setToDoItem(toDoCursor.getString(1));

                todoItems.add(item);
            } while(toDoCursor.moveToNext());
        }
        return todoItems; 
    }

    public int getIds(ToDo todo) {
        String selectQuery = "SELECT _id FROM " + TABLE_TODO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        c.moveToFirst();
        int total = c.getCount();
        return total;
    }
}
