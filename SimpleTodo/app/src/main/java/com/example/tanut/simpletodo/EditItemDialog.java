package com.example.tanut.simpletodo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.tanut.simpletodo.backend.Item;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.tanut.simpletodo.R.id.date_picker;
import static com.example.tanut.simpletodo.R.id.tv_pr;

public class EditItemDialog extends DialogFragment  {
    private Item toDOItem;
    private EditText et_item,etNote;
    private TextView tv_item , tv_priority , tv_created;
    private DatePicker date_picker;
    private ImageButton bacButton,deleteButton;
    private Spinner spPriority;
    private  SendResultListner sendListner;
    private View row;
    private CheckBox cb_selected;
    List<String> priorites;


    public EditItemDialog(){

    }

    public static EditItemDialog newInstance(Item item,int position) {
        item = item;
        Bundle args = new Bundle();
        args.putParcelable("Item",item);
        args.putInt("pos",position);
        EditItemDialog fragment = new EditItemDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public interface SendResultListner{
        void onFinshEdit(Item item,int position,boolean isRemove);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_edit_item,container);
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;

        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        super.onResume();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        this.toDOItem = ((Item)getArguments().getParcelable("Item"));
        sendListner = (SendResultListner) getActivity();
        bacButton = (ImageButton) view.findViewById(R.id.bacButton);
        bacButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDOItem.item = et_item.getText().toString();
                toDOItem.notes = etNote.getText().toString();
                Log.d("SELECTED NAME",spPriority.getSelectedItem().toString());
                Log.d("SELECTD ID",spPriority.getSelectedItem().toString());
                toDOItem.priority = priorites.indexOf(spPriority.getSelectedItem());
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DAY_OF_MONTH, date_picker.getDayOfMonth());
                cal.set(Calendar.MONTH, date_picker.getMonth());
                cal.set(Calendar.YEAR, date_picker.getYear());
                toDOItem.due = cal.getTimeInMillis();
                toDOItem.status = cb_selected.isChecked()==true?1:0;

                sendListner.onFinshEdit(toDOItem,getArguments().getInt("pos"),false);
                dismiss();
            }
        });
        row = view.findViewById(R.id.include);
        et_item = (EditText) row.findViewById(R.id.tvItemEdit);
        tv_item = (TextView) row.findViewById(R.id.tvItem);
        tv_priority = (TextView) row.findViewById(R.id.tvPriority);
        cb_selected = (CheckBox) row.findViewById(R.id.cb_selection);

        etNote = (EditText) view.findViewById(R.id.etNote);
        spPriority = (Spinner) view.findViewById(R.id.spPriority);
        date_picker = (DatePicker) view.findViewById(R.id.date_picker);
        tv_created = (TextView) view.findViewById(R.id.tv_created);
        deleteButton = (ImageButton) view.findViewById(R.id.iv_delete);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               AlertDialog dialog = AskOption();
                dialog.show();
            }
        });

        tv_item.setVisibility(View.GONE);
        tv_priority.setVisibility(View.GONE);
        et_item.setVisibility(View.VISIBLE);


        et_item.setText(toDOItem.item);
        et_item.setSelection(et_item.getText().length());
        Log.d("STATUS DIALOG :: ",toDOItem.status+"");


        cb_selected.setChecked(toDOItem.status==1);

        // adding notes
        if(!toDOItem.notes.equals("")){
          etNote.setText(toDOItem.notes);
          etNote.setSelection(etNote.getText().length());
        }

        // Spinner Drop down elements
        priorites = new ArrayList<String>();
        priorites.add("LOW");
        priorites.add("MEDIUM");
        priorites.add("HIGH");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, priorites);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spPriority.setAdapter(dataAdapter);
        spPriority.setSelection(toDOItem.priority);

        Log.d("DUE DATE",new Date(toDOItem.due).toString());
        // set due date
        Date due = new Date(toDOItem.due);
        Calendar cal = Calendar.getInstance();
        cal.setTime(due);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        date_picker.setMinDate(toDOItem.created);
        date_picker.updateDate(year,month,day);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        tv_created.setText("Created: "+df.format(new Date(toDOItem.created)));


    }



    private AlertDialog AskOption()
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(getContext())
                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete")
                .setIcon(R.drawable.ic_delete_forever_24dp)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        sendListner.onFinshEdit(toDOItem,getArguments().getInt("pos"),true);
                        dialog.dismiss();
                        dismiss();
                    }

                })



                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
        return myQuittingDialogBox;

    }
    @Override
    public void dismiss() {
        super.dismiss();
    }
    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_edit_item);
//        //et_item = (EditText)findViewById(R.id.etItem);
//        //bt_save = (Button)findViewById(R.id.btn_save);
//        Log.d("EDIT","ONCREATE");
//        toDoItem = (Item)getIntent().getParcelableExtra("Item");
//        position = getIntent().getIntExtra("pos",0);
//        //et_item.setText(toDoItem.item);



//        bt_save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(!toDoItem.equals("")){
//                    Intent data = new Intent();
//                    toDoItem.item = et_item.getText().toString();
//                    data.putExtra("item",toDoItem);
//                    data.putExtra("pos",position);
//                    setResult(RESULT_OK,data);
//                    finish();
//                }
//                else{
//                    Toast.makeText(EditItemActivity.this,"Please enter the item you want to add",Toast.LENGTH_LONG).show();
//                }
//
//            }
//        });

}

