package com.example.dashboard;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class ReminderMain extends Fragment {

    final List<String> list = new ArrayList<>();
    int[] backgroundColors;
    int[] textColors;
    public static Context context = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.reminder_layout, container, false);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        final ListView listView = getView().findViewById(R.id.listView);
        final TextAdapter adapter = new TextAdapter();
        context = getActivity();

        int maxItems = 100;
        backgroundColors = new int[maxItems];
        textColors = new int[maxItems];

        for(int i=0;i<maxItems;i++){
            backgroundColors[i] = Color.LTGRAY;
            textColors[i] = Color.BLACK;
        }

        readInfo();



        adapter.setData(list, backgroundColors, textColors);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog dialog = new AlertDialog.Builder(ReminderMain.context)
                        .setTitle("Delete this task?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                list.remove(position);
                                adapter.setData(list, backgroundColors, textColors);
                                saveInfo();
                            }
                        })
                        .setNegativeButton("No", null)
                        .create();
                dialog.show();
            }
        });

        final Button newTaskButton = getView().findViewById(R.id.newTaskButton);

        newTaskButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final EditText taskInput =  new EditText(ReminderMain.context);
                taskInput.setSingleLine();
                AlertDialog dialog = new AlertDialog.Builder(ReminderMain.context)
                        .setTitle("Add a new Task")
                        .setMessage("What is your new task?")
                        .setView(taskInput)
                        .setPositiveButton("Add Task", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                list.add(taskInput.getText().toString());
                                adapter.setData(list, backgroundColors, textColors);
                                saveInfo();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
            }
        });

        final Button deleteAllTasksButton = getView().findViewById(R.id.deleteAllTasksButton);

        deleteAllTasksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(ReminderMain.context)
                        .setTitle("Delete all tasks?")
                        .setMessage("Do you really want to delete all tasks?")
                        .setPositiveButton("Delete All Tasks", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                list.clear();
                                adapter.setData(list, backgroundColors, textColors);
                                saveInfo();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
            }
        });

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    private void saveInfo() {
        try {
            File file = new File(context.getFilesDir(), "saved");

            FileOutputStream fOut = new FileOutputStream(file);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fOut));

            for (int i = 0; i < list.size(); i++) {
                bw.write(list.get(i));
                bw.newLine();
            }

            bw.close();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void readInfo(){
        File file = new File(context.getFilesDir(), "saved");
        if(!file.exists()){
            return;
        }

        try {
            FileInputStream is = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = reader.readLine();
            while (line != null) {
                list.add(line);
                line = reader.readLine();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    class TextAdapter extends BaseAdapter {

        List<String> list = new ArrayList<>();

        int[] backgroundColors;
        int[] textColors;

        void setData(List<String> mList, int[] mBackgroundColors, int[] mTextColors){
            list.clear();
            list.addAll(mList);

            backgroundColors = new int[list.size()];
            textColors = new int[list.size()];

            for(int i = 0; i < list.size(); i++) {
                backgroundColors[i] = mBackgroundColors[i];
                textColors[i] = mTextColors[i];
            }
            notifyDataSetChanged();
        }

        @Override
        public int getCount(){
            return list.size();
        }
        @Override
        public Object getItem(int position){
            return null;
        }

        @Override
        public long getItemId(int position){
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater)
                        ReminderMain.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.reminder_item, parent, false);
            }
            TextView textView = convertView.findViewById(R.id.task);

            textView.setBackgroundColor(backgroundColors[position]);
            textView.setTextColor(textColors[position]);
            textView.setText(list.get(position));

            return convertView;
        }
    }
}

