package com.example.dashboard;

/*
Welcome to the Helping Hand App Main Activity Class!
The Main Activity sets up the navigation drawer, which allows users to navigate around the app
We have to set up onClick events for each item in the navigation drawer.
We also have to set up the Notification Manager for the app, to let them know when certain services are running.

Student Name: Rowan Armstrong
Student ID: s1541585

 */

// We need to use the NotificationManager API to snd the user notifications. These are the necessary imports.
import android.app.NotificationChannel;
import android.app.NotificationManager;

// We need to use a few other imports to get a drawer up and running
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.Menu;
import android.view.MenuItem;

// Finally, the Drawer API requires these imports. It allows a horizontal gravity and fancy animation to be used.
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;


// Let get started with our MainActivity class.
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    // The only declaration required is our DrawerLayout
    private DrawerLayout drawer;

    //OnCreate is called when someone opens the Helping Hands App
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //The layout of our drawer is specified in the activity_main .xml file
        setContentView(R.layout.activity_main);

        // More Navigation Drawer Stuff - there is a separate .xml file for our drawer_layout
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);

        // The getSupportFragmentManager helps us manage our fragments
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new DashboardFragment()).commit();

        // Nav_view allows us to open and close the drawer
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // code for changes colour of the emergency fragment. This needs to stand out.
        Menu myMenu = navigationView.getMenu();
        MenuItem tools= myMenu.findItem(R.id.nav_emergency);
        SpannableString s = new SpannableString(tools.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.MyTheme), 0, s.length(), 0);
        tools.setTitle(s);
        navigationView.setNavigationItemSelectedListener(this);

        // Lets set up our open and close toggle.
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // We need to create our notification channel as well, to let the user know when certain services are running.
        createNotificationChannel();

    }

    // We call this function to create a notification channel
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            String description = getString(R.string.action_setting);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("StepChannel", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    // We call this function when something in the drawer is selected. It takes us to the correct fragment.
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            //Take the user back to the dashboard
            case R.id.nav_message:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new DashboardFragment()).commit();
                break;
            //Take the user to the movement pattern fragement
            case R.id.nav_chat:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new StepMain()).commit();
            //Take the user to the reminder fragement
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ReminderMain()).commit();
            //Take the user to the fall detection fragment
                break;
            case R.id.nav_fall:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FallMain()).commit();
            //Take the user to the TakeMeHome fragement
                break;
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new TakemehomeMain()).commit();
            //Take the user to the Reaction Test Fragment
                break;
            case R.id.nav_game:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ReactionMain()).commit();
            //Take the user to the Emergency Information Fragement
                break;
            case R.id.nav_emergency:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Emergency_Information()).commit();
                break;
            //Take the user to the Emergency Information Fragement, in case they click below the actual button. (keep in mind its an emergency so lets make the button as big as possible).
            case R.id.nav_emergency2:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Emergency_Information()).commit();
                break;

        }
        //When we are finished selecting a fragment, don't forget to close the drawer behind you!
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Lets decide what happens when we press the back button
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
