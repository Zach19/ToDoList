package com.example.zachhauser.todolist;

import static android.R.attr.id;

/**
 * Created by zachhauser on 2016-11-30.
 */

public class ToDo {

    private int _id;
    private String ToDoItem;

    public ToDo() {

    }

    public ToDo (int id, String ToDoItem) {
        this._id = id;
        this.ToDoItem = ToDoItem;
    }

    public ToDo(String ToDoItem) {
        this.ToDoItem = ToDoItem;
    }

    public void setId(int id) {
        this._id = id;
    }

    public int getId() {
        return this._id;
    }

    public void setToDoItem(String ToDoItem) {
        this.ToDoItem = ToDoItem;
    }

    public String getToDoItem() {
        return this.ToDoItem;
    }

    @Override
    public String toString() {
        return "Item [id = " + id + ", task = " + ToDoItem;
    }
}
