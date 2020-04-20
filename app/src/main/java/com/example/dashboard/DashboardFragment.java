package com.example.dashboard;

/*
Welcome to the Helping Hand App Dashboard Class!
The dashboard class gives the user a friendly central home page.
It contains information on the date/time, reminders and which trackers are active.
The menu drawer is accessible from the dashboard, and this is how users navigate the app.

Student Name: Rowan Armstrong
Student ID: s1541585

 */

//Imports for the display
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

// Imports for the list view
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

// This is a fragment class: It is accessible from the drawer menu
public class DashboardFragment extends Fragment {

    // Reminder list declarations
    private final List<String> list = new ArrayList<>();
    private static Context context = null;

    //Lets create the Dashboard view!
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //The dashboard_layout.xml file can be found in the 'red.layout' directory.
        return inflater.inflate(R.layout.dashboard_layout, container, false);
    }

    // On creation we have to save the instance state, which is especially important for fragments.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Now that the Dashboard view is created, lets fill it with some beautiful TextViews, ImageViews and ListViews.
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        // This App needs to be friendly! Lets greet the user.
        TextView goodsomething = (TextView) getView().findViewById(R.id.GoodSomething);
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        //If its morning time, lets which the user 'Good Morning'
        if (timeOfDay >= 0 && timeOfDay < 12) {
            goodsomething.setText("Good Morning");
        //If its afternoon time, lets wish the user 'Good Afternoon'
        } else if (timeOfDay >= 12 && timeOfDay < 18) {
            goodsomething.setText("Good Afternoon");
        //If its evening time, lets wish the user 'Good Evening'
        } else if (timeOfDay >= 18 && timeOfDay < 24) {
            goodsomething.setText("Good Evening");
        }


        // Now, lets show the time, day and date within the calender icon
        TextView clock = (TextView) getView().findViewById(R.id.clock1);
        TextView dateTimeDisplay = (TextView) getView().findViewById(R.id.text_date_display);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d");
        String date = dateFormat.format(calendar.getTime());
        dateTimeDisplay.setText(date);
        TextView dateTimeDisplay2 = (TextView) getView().findViewById(R.id.text_date_display2);
        Calendar calendar2 = Calendar.getInstance();
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("EEE");
        String date2 = dateFormat2.format(calendar2.getTime());

        /*
        The SimpleDateFormat method returns the first three letters of the day. It would be nice
        to greet the user properly, so lets fill out the rest of the day name.
        */
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

        // We don;t want to give the Dashboard class too much work to do, so lets put a cap on the number of list items
        int maxItems = 100;
        // Beautiful background
        int[] backgroundColors = new int[maxItems];
        // Beautiful text colour
        int[] textColors = new int[maxItems];

            // When we populate the list, we need to make sure that the background and text colours are populated also.
            for (int i = 0; i < maxItems; i++) {
                backgroundColors[i] = getResources().getColor(R.color.lightpink);
                textColors[i] = Color.BLACK;
            }

            // Lets put the list on the screen! To do this, we treat the list as an adapter.
            final ListView listView = getView().findViewById(R.id.listView);
            final TextAdapter adapter = new TextAdapter();
            context = getActivity();
            // The readInfo function is defined below. It lets us read the info to be put in our list!
            readInfo();
            adapter.setData(list, backgroundColors, textColors);
            listView.setAdapter(adapter);
        }


        // We need to read the info from our list, which is stored locally on the Android Device. Thats what this function is for.
        private void readInfo () {
            // The file where our list info resides is called 'saved'
            File file = new File(context.getFilesDir(), "saved");
            //Oh no! The list is empty. lets return.
            if (!file.exists()) {
                return;
            }
            //If list info is present, we need to use a buffer to populate the list on the screen, entry by entry.
            try {
                FileInputStream is = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line = reader.readLine();
                // We use a while loop to populate the list, until there are no more entries left.
                while (line != null) {
                    list.add(line);
                    line = reader.readLine();
                }
            // For debugging purposes
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Our TextAdapter lets us populate the list. However, we cannot edit the list from the dashboard. To do this, you will have to use the 'reminders' fragement
        class TextAdapter extends BaseAdapter {
            // First we must declare the list
            List<String> list = new ArrayList<>();
            // Lets declare our beautiful colours
            int[] backgroundColors;
            int[] textColors;
            // This retrieves the colour and text for each entry, and allows us to populate the list when dashboard is opened.
            void setData(List<String> mList, int[] mBackgroundColors, int[] mTextColors) {
                //Reset the data from last time
                list.clear();
                //Fill up the list
                list.addAll(mList);
                // Declare our colours here
                backgroundColors = new int[list.size()];
                textColors = new int[list.size()];
                // We can have alternative list colours if we like, but for now we will keep it simple.
                for (int i = 0; i < list.size(); i++) {
                    backgroundColors[i] = mBackgroundColors[i];
                    textColors[i] = mTextColors[i];
                }
                //Let everyone know that the list data has been changed
                notifyDataSetChanged();
            }

            // This little function returns the list size
            @Override
            public int getCount() {
                return list.size();
            }

            // This little function returns an item for a given position (1,2,3...)
            @Override
            public Object getItem(int position) {
                return null;
            }

            // This little function gives a name to each list position.
            @Override
            public long getItemId(int position) {
                return 0;
            }

            // This little function hands over the list to the .xml file, to present to the user.
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    LayoutInflater inflater = (LayoutInflater)
                            // A service which lets us inflate our list
                            DashboardFragment.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.reminder_item, parent, false);
                }
                // Lets send our lovely list to the .xml file for the user to see.
                TextView textView = convertView.findViewById(R.id.task);
                textView.setBackgroundColor(backgroundColors[position]);
                textView.setTextColor(textColors[position]);
                textView.setText(list.get(position));
                return convertView;
            }
        }


    }
