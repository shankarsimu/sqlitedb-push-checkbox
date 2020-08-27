package com.example.csvsqlite;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Parcelable;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.csvsqlite.CSVFile;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView listView;
    private ItemArrayAdapter itemArrayAdapter;
    private Button button;
    Dialog dialog;
    final String[] items = {"TITLE", "LEVEL", "LANGUAGE", "QUALITY"};
    final ArrayList itemsSelected = new ArrayList();
    //    private CheckBox ck1, ck2, ck3, ck4;
    DataBase db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_csv);
        db = new DataBase(MainActivity.this);
        listView = (ListView) findViewById(R.id.listView);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);

        itemArrayAdapter = new ItemArrayAdapter(getApplicationContext(), R.layout.item_layout);

        Parcelable state = listView.onSaveInstanceState();
        listView.setAdapter(itemArrayAdapter);
        listView.onRestoreInstanceState(state);

        InputStream inputStream = getResources().openRawResource(R.raw.data);
        CSVFile csvFile = new CSVFile(inputStream);
        List scoreList = csvFile.read();
        for (Object scoreData : scoreList) {
            String[] str = (String[]) scoreData;
            db.insertData(str[0], str[1], str[2], str[3]);
            itemArrayAdapter.add(scoreData);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Select Options You Want : ");

            final boolean[] checkedItems = null;
            builder.setMultiChoiceItems(items, checkedItems,
                    new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int selectedItemId,
                                            boolean isSelected) {
                            if (isSelected) {
                                if (!itemsSelected.contains(items[selectedItemId])) {
                                    itemsSelected.add(items[selectedItemId]);
                                }
                            } else if (itemsSelected.contains(items[selectedItemId])) {
                                itemsSelected.remove(items[selectedItemId]);
                            }
                        }
                    })
                    .setPositiveButton("Done!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            List datalist = db.getDataJArry(itemsSelected);
                            itemArrayAdapter.clear();
                            itemArrayAdapter.notifyDataSetChanged();
                            Parcelable state = listView.onSaveInstanceState();
                            listView.setAdapter(itemArrayAdapter);
                            listView.onRestoreInstanceState(state);
                            for (Object scoreData : datalist) {
                                itemArrayAdapter.add(scoreData);
                            }
                            itemArrayAdapter.notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                                listView.clearChoices();
                        }
                    });

            dialog = builder.create();

            dialog.show();
        }
    }
}