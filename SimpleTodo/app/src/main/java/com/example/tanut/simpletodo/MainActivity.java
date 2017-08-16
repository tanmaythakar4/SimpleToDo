package com.example.tanut.simpletodo;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tanut.simpletodo.adapter.ToDoAdapter;
import com.example.tanut.simpletodo.backend.AppDatabase;
import com.example.tanut.simpletodo.backend.Item;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

import static android.R.id.input;
import static org.apache.commons.io.FileUtils.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener , EditItemDialog.SendResultListner {
    ToDoAdapter todoAdapter;
    ArrayList<Item> items;
    EditText etItem;
    ImageButton btAdd;
    ListView lvItem;
    AppDatabase database;
    private final int RESULT_CODE = 20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etItem = (EditText)findViewById(R.id.etTodo);
        btAdd = (ImageButton) findViewById(R.id.btSubmit);
        lvItem = (ListView) findViewById(R.id.lvItems);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        database = AppDatabase.getDatabase(getApplicationContext());
        //readTodo();
        items = (ArrayList<Item>) database.itemModel().getAllItem();
        todoAdapter = new ToDoAdapter(this,items);
        todoAdapter.sort(new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                return o2.priority-o1.priority;
            }
        });
        lvItem.setAdapter(todoAdapter);
        btAdd.setOnClickListener(this);
        lvItem.setOnItemLongClickListener(this);
        lvItem.setOnItemClickListener(this);
    }


    @Override
    public void onClick(View v) {
       if(v.getId()==R.id.btSubmit){
           onAddItem();
       }
    }

    private void onAddItem() {
        String input = etItem.getText().toString();
        Item build = Item.builder().setItem(input).build();
        if(!input.equals("")){
            todoAdapter.add(build);
            etItem.setText("");
            database.itemModel().addItem(build);
        }
        else{
            Toast.makeText(this,"Please enter the item you want to add",Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Item input =items.get(position);
        items.remove(position);
        todoAdapter.notifyDataSetChanged();
        database.itemModel().removeItem(input.id);
        //writeTodo();
        return true;
    }

   /* private void readTodo(){
        File filedir = getFilesDir();
        File todoFile = new File(filedir,"todo.txt");
        try {
            items = (ArrayList<String>) FileUtils.readLines(todoFile);
        } catch (IOException e) {
            items = new ArrayList<String>();
            e.printStackTrace();
        }
    }
*/
 /*   private void writeTodo(){
        File filedir = getFilesDir();
        File todoFile = new File(filedir,"todo.txt");
        try {
            FileUtils.writeLines(todoFile,items);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }*/

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        FragmentManager fm = getSupportFragmentManager();
        EditItemDialog editDialog = EditItemDialog.newInstance(items.get(position),position);
        editDialog.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Dialog_FullScreen);
        editDialog.show(fm,"fragment_edit");

//        Intent in = new Intent(this,EditItemActivity.class);
//        in.putExtra("Item", items.get(position));
//        //in.putExtra("item",items.get(position));
//        in.putExtra("pos",position);
//        startActivityForResult(in,RESULT_CODE);
    }

/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == RESULT_CODE){
            Item item = data.getExtras().getParcelable("item");
            int pos = data.getExtras().getInt("pos");
            items.set(pos,item);
            todoAdapter.notifyDataSetChanged();
            //writeTodo();
            database.itemModel().updateItem(item);

        }
    }
*/

    @Override
    protected void onDestroy() {
        AppDatabase.destroyInstance();
        super.onDestroy();
    }

    @Override
    public void onFinshEdit(Item item, int position, boolean isRemove) {

        if(isRemove){
            items.remove(position);
            todoAdapter.notifyDataSetChanged();
            database.itemModel().removeItem(item.id);
        }
        else{
            items.set(position,item);
            todoAdapter.notifyDataSetChanged();
            //writeTodo();
            database.itemModel().updateItem(item);
        }


    }

    public void checkboxSelected(boolean isSelect,int pos)
    {
        // Do stuff with myObj here
        Item item = items.get(pos);

        if(isSelect){
            Log.d("CheckActvity","SELECTED");
            item.status = 1;
        }
        else{
            item.status = 0;
        }

       items.set(pos,item);
       database.itemModel().updateItem(item);
    }
}
