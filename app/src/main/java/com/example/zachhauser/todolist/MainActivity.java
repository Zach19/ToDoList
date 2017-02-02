package com.example.zachhauser.todolist;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.button;
import static android.R.attr.data;
import static android.R.attr.onClick;
import static android.R.id.input;
import static android.R.id.list;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static com.example.zachhauser.todolist.DBHandler.COLUMN_ITEM;
import static com.example.zachhauser.todolist.DBHandler.TABLE_TODO;
import static com.example.zachhauser.todolist.R.id.deleteBtn;
import static com.example.zachhauser.todolist.R.id.design_bottom_sheet;
import static com.example.zachhauser.todolist.R.id.inputEdit;
import static com.example.zachhauser.todolist.R.id.toDoItem;
import static com.example.zachhauser.todolist.R.id.todoTV;


public class MainActivity extends AppCompatActivity {

    String inputText;
    DBHandler dbHandler = new DBHandler(this, null, null, 1);
    SimpleCursorAdapter simpleCursorAdapter;
    ListView listView;
    List<ToDo> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        listView = (ListView) findViewById(R.id.itemsLV);

        list = dbHandler.getAllItems();
        dbHandler.getIds(list.get(0));
        displayToDoItems();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialogInput = new Dialog(MainActivity.this);
                dialogInput.setContentView(R.layout.task_input_box);

                dialogInput.show();

                Button addButton = (Button) dialogInput.findViewById(R.id.inputBtn);
                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        EditText inputET = (EditText) dialogInput.findViewById(R.id.inputEdit);
                        inputText = inputET.getText().toString();
                        addItem();
                        dialogInput.dismiss();
                    }
                });
            }
        });

    }

    public void addItem() {
        ToDo toDoItem = new ToDo(inputText);
        dbHandler.addToDoItem(toDoItem);
        list.add(toDoItem);
        displayToDoItems();
        simpleCursorAdapter.notifyDataSetChanged();
    }

    public void deleteTask(View v) {
        int position = positionNum(v);
        View deleteView = getViewByPosition(position, listView);

        TextView toDoItem = (TextView) deleteView.findViewById(R.id.todoTV);
        String toDoItemText = toDoItem.getText().toString();

        boolean result = dbHandler.deleteItem(toDoItemText);

        if (result) {
            list.remove(position);
            displayToDoItems();
            simpleCursorAdapter.notifyDataSetChanged();
        }
    }

    private void displayToDoItems() {
        try {
            Cursor c = dbHandler.getItems();
            if (c == null) {
                return;
            }
            if (c.getCount() == 0) {
                return;
            }

            String[] columns = new String[] {
                    dbHandler.COLUMN_ITEM
            };

            int[] boundTo = new int[] {
                    todoTV
            };

            simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.todoitem, c, columns, boundTo, 0);
            listView.setAdapter(simpleCursorAdapter);
        }

        catch (Exception e) {
        }
    }

    public int positionNum(View v) {
        int positionInt = listView.getPositionForView(v);
        return positionInt;
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        }
        else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
}
