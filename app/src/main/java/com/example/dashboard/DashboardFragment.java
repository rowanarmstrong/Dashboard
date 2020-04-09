package com.example.dashboard;


import android.content.Context;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class DashboardFragment extends Fragment {

    private TextView goodsomething;
    private TextView dateTimeDisplay;
    private TextView dateTimeDisplay2;
    private Calendar calendar;
    private Calendar calendar2;
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat dateFormat2;
    private String date;
    private String date2;

    // Reminder list stuff
    final List<String> list = new ArrayList<>();
    int[] backgroundColors;
    int[] textColors;
    private static Context context = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dashboard_layout, container, false);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        // Good Something
        goodsomething = (TextView) getView().findViewById(R.id.GoodSomething);
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 12) {
            goodsomething.setText("Good Morning");
        } else if (timeOfDay >= 12 && timeOfDay < 18) {
            goodsomething.setText("Good Afternoon");
        } else if (timeOfDay >= 18 && timeOfDay < 24) {
            goodsomething.setText("Good Evening");
        }


        // Date and Time Stuff
        TextView clock = (TextView) getView().findViewById(R.id.clock1);
        dateTimeDisplay = (TextView) getView().findViewById(R.id.text_date_display);
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MMM d");
        date = dateFormat.format(calendar.getTime());
        dateTimeDisplay.setText(date);
        dateTimeDisplay2 = (TextView) getView().findViewById(R.id.text_date_display2);
        calendar2 = Calendar.getInstance();
        dateFormat2 = new SimpleDateFormat("EEE");
        date2 = dateFormat2.format(calendar2.getTime());

        if (date2.equals("Mon") || date2.equals("Fri") || date2.equals("Sun")) {
            dateTimeDisplay2.setText(date2 + "day");
        } else if (date2.equals("Thu")) {
            dateTimeDisplay2.setText(date2 + "rsday");
        } else if (date2.equals("Tue")) {
            dateTimeDisplay2.setText(date2 + "sday");
        } else if (date2.equals("Wed")) {
            dateTimeDisplay2.setText(date2 + "nesday");
        } else if (date2.equals("Sat")) {
            dateTimeDisplay2.setText(date2 + "urday");
        }


            int maxItems = 100;
            backgroundColors = new int[maxItems];
            textColors = new int[maxItems];

            for (int i = 0; i < maxItems; i++) {
                backgroundColors[i] = getResources().getColor(R.color.lightpink);
                textColors[i] = Color.BLACK;
            }

            final ListView listView = getView().findViewById(R.id.listView);
            final TextAdapter adapter = new TextAdapter();
            context = getActivity();
            readInfo();
            adapter.setData(list, backgroundColors, textColors);
            listView.setAdapter(adapter);


        }


        private void readInfo () {
            File file = new File(context.getFilesDir(), "saved");
            if (!file.exists()) {
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        class TextAdapter extends BaseAdapter {

            List<String> list = new ArrayList<>();

            int[] backgroundColors;
            int[] textColors;

            void setData(List<String> mList, int[] mBackgroundColors, int[] mTextColors) {
                list.clear();
                list.addAll(mList);

                backgroundColors = new int[list.size()];
                textColors = new int[list.size()];

                for (int i = 0; i < list.size(); i++) {
                    backgroundColors[i] = mBackgroundColors[i];
                    textColors[i] = mTextColors[i];
                }
                notifyDataSetChanged();
            }

            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    LayoutInflater inflater = (LayoutInflater)
                            DashboardFragment.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
