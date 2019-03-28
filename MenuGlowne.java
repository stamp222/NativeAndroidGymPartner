package com.example.jacek.gympartner;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jacek.gympartner.Fragmenty.HomeFragment;
import com.example.jacek.gympartner.Fragmenty.NotificationsFragment;
import com.example.jacek.gympartner.Fragmenty.SettingsFragment;
import com.example.jacek.gympartner.Fragmenty.WorkoutFragment;
import com.example.jacek.gympartner.aktywnosci.trening.TabsLayout;

public class MenuGlowne extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;
    private FloatingActionButton fab;

    // indeks aktualnego obiektu
    public static int navItemIndex = 0;

    // tagi by załączyć fragmenty
    private static final String TAG_HOME = "home";
    private static final String TAG_TRAINING = "workout";
    private static final String TAG_NOTIFICATIONS = "notifications";
    private static final String TAG_SETTINGS = "ustawienia";
    public static String CURRENT_TAG = TAG_HOME;

    // pasek narzędzi tytułów opcji
    private String[] activityTitles;

    // ładowanie home przy przycisku cofnięcia
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHandler = new Handler();



        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);


        // widoki głównych nawigacji
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);

        // ładuje pasek narzędzi tytułów z tablicy stringow
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        // możliwość zmiany na inną akcje floatingbar
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Zmiana na jakas inna aktywnosc", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // metoda ładująca tworce maila tlo i zdjecie
        loadNavHeader();

        // inicjalizuje menu nawigacji
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
    }

    /***
     * ładuje informacje o menu nawigacji
     * takimi jak tlo, zdjecie
     * autor, strona internetowa czy kropka przy przycisku notifications
     */
    private void loadNavHeader() {
        // tworca i email
        txtName.setText("Jacek Szyper");
        txtWebsite.setText("jacekszyper@gmail.com");

        // pokazuje kropke w menu przy obiektach
        navigationView.getMenu().getItem(0).setActionView(R.layout.menu_kropka);
        navigationView.getMenu().getItem(1).setActionView(R.layout.menu_kropka);
        navigationView.getMenu().getItem(2).setActionView(R.layout.menu_kropka);
        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_kropka);

    }

    /***
     * zwraca odpowiedni fragment
     * który wybrał użytkownik
     */
    private void loadHomeFragment() {
        // wybiera odpowiedni obiekti z menu nawigacji
        selectNavMenu();
        // ustawia tytuly paska narzedzi
        setToolbarTitle();
        // jesli uzytkownik wciska jeszcze raz ten sam obiekt nie rób nic tylko wylacz menu nawgiacji
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
            // pokazuje lub ukrywa przycisk fab
            toggleFab();
            return;
        }
        // czasem kiedy fragment ma za duzo danych zdaje sie jakby obraz wisial kiedy zmienia sie obiekt w nawigacji
        // poprzez uzycie watku fragment laduje sie wraz cross fade effect co niweluje problem efekt ten moze byc widaczny w GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // aktualizuje zawartosc glowna przez zamiane fragmentow
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };
        // jeśli mPendingRunnable nie jest zerem, to dodaje kolejke wiadomosci
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }
        // pokazuje lub ukrywa przycisk fab
        toggleFab();
        //wylacza drawer
        drawer.closeDrawers();
        // odswieza pasek narzedzi menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home fragment
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                // trening fragment
                WorkoutFragment workoutFragment = new WorkoutFragment();
                return workoutFragment;
            case 2:
                // ustawienia fragment
                SettingsFragment settingsFragment = new SettingsFragment();
                return settingsFragment;
            case 3:
                // notifications fragment
                NotificationsFragment notificationsFragment = new NotificationsFragment();
                return notificationsFragment;

            default:
                return new HomeFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Konfigurowanie obiektu nawigacji po przycisnieciu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // metoda po przycisnieciu obiektu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //sprawdza ktory obiekt zostal wcisniety i generuje odpowiednie akcje po przycisnieciu
                switch (menuItem.getItemId()) {
                    //Zamienia glowna zawartosc z zawartoscia fragmentu ktory jest Inbox View;
                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_workout:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_TRAINING;
                        break;
                    case R.id.nav_notifications:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_NOTIFICATIONS;
                        break;
                    case R.id.nav_settings:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_SETTINGS;
                        break;
                    case R.id.nav_about_us:
                        // wlacza nowa aktynowc
                        startActivity(new Intent(MenuGlowne.this, Onas.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_privacy_policy:
                        // wlacza nowa aktywnosc
                        startActivity(new Intent(MenuGlowne.this, Zastrzezenia.class));
                        drawer.closeDrawers();
                        return true;
                    default:
                        navItemIndex = 0;
                }
                //sprawdza czy obiekt zostal wcisniety jesli nie to oznacza go jako wcisniety
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);
                loadHomeFragment();
                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // kod bedzie tutaj uzyty tylko kiedy drawer zostanie zamkniety kiedy nie chcemy by cokolwiek sie dzialo i zostawiamy to puste
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // kod bedzie tutaj uzyty tylko kiedy drawer zostanie otworzony kiedy nie chcemy by cokolwiek sie dzialo i zostawiamy to puste
                super.onDrawerOpened(drawerView);
            }
        };

        //dodaje actionbartoggle do drawer layout
        drawer.addDrawerListener(actionBarDrawerToggle);

        //wywoluje synchronizacje ktora sprawa ze ikony zawsze sie pojawia
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }
        // laduje home fragment przy przycisnieciu przycisku wstecz
        // kiedy uzytkownik jest w innym fragmencie
        if (shouldLoadHomeFragOnBackPress) {
            // sprawdza czy uzytkownik jest w innym fragmencie
            // innym niz home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // wyswietla menu, dodaje obiekty do action bar jest jest obecne
        // wyswietla menu tylko dla home fragment
        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.main, menu);
        }
        // kiedy jest wlaczony fragment notifications laduje menu dla niego
        if (navItemIndex == 2) {
            getMenuInflater().inflate(R.menu.notification, menu);
        }

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        // Manipuluje po kliknieciu obiektu action bar.
        // Akcja ta automatycznie bedzie sie zajmowala po kliknieciu home/up przycisku
        // tak dlugo dopoki nie sprecyzuje sie aktywnosci parent w androidmanifescie
        int id = item.getItemId();

        // Wyłączenie aplikacju
        if (id == R.id.action_logout) {
            Toast.makeText(getApplicationContext(), "Wylogowanie!", Toast.LENGTH_LONG).show();
            finishAndRemoveTask();
            return true;
        }

        // uzytkownik jest w obiekcie notification i przyciska oznaczone jak przeczytane
        if (id == R.id.action_mark_all_read) {
            Toast.makeText(getApplicationContext(), "Wszystkie powiadomienia oznaczam jako przeczytane!", Toast.LENGTH_LONG).show();
        }

        // uzytkownik jest w obiekcie notification i przyciska wyczysc wszystko
        if (id == R.id.action_clear_notifications) {
            Toast.makeText(getApplicationContext(), "Czysci powiadomienia", Toast.LENGTH_LONG).show();
        }


        return super.onOptionsItemSelected(item);
    }

    // pokazuje lub ukrywa przycisk fab
    private void toggleFab() {
        if (navItemIndex == 0)
            fab.show();
        else
            fab.hide();
    }

    public void open(View view) {
        Intent i = new Intent(getApplicationContext(), TabsLayout.class);
        startActivity(i);
    }
}