package it.jaschke.alexandria;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;


import it.jaschke.alexandria.api.Callback;


public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks, Callback {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment navigationDrawerFragment;

    private static final String SCAN_RESULT = "SCAN_RESULT";
    private static final String SCAN_RESULT_FORMAT = "SCAN_RESULT_FORMAT";
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence title;
    public static boolean IS_TABLET = false;
    private BroadcastReceiver messageReciever;

    public static final String MESSAGE_EVENT = "MESSAGE_EVENT";
    public static final String MESSAGE_KEY = "MESSAGE_EXTRA";
    Toolbar mtooToolbar;
    int backPressFlag = 0;
    int selectedPosition = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IS_TABLET = isTablet();
        if (IS_TABLET) {
            setContentView(R.layout.activity_main_tablet);
        } else {
            setContentView(R.layout.activity_main);
        }
        mtooToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtooToolbar);

        messageReciever = new MessageReciever();
        IntentFilter filter = new IntentFilter(MESSAGE_EVENT);
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReciever, filter);

        navigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        title = getTitle();
//
//        // Set up the drawer.
        navigationDrawerFragment.setUp(R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout), mtooToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment nextFragment;
        String tagName;
        if (position != selectedPosition) {
            switch (position) {
                default:
                case 0:
                    nextFragment = new ListOfBooks();
                    tagName = getString(R.string.list_book_fragment);
                    selectedPosition = 0;
                    break;
                case 1:
                    nextFragment = new AddBook();
                    tagName = getString(R.string.add_book_fragment);
                    selectedPosition = 1;
                    break;
                case 2:
                    nextFragment = new About();
                    tagName = getString(R.string.about_fragment);
                    selectedPosition = 2;
                    break;

            }
            System.out.println("inside onvaigationdrawer" + position);
            if (position != 0) {
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    fragmentManager.popBackStack();
                }
                fragmentManager.beginTransaction()
                        .replace(R.id.container, nextFragment, tagName)
                        .addToBackStack((String) title)
                        .commit();
            } else {
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    fragmentManager.popBackStack();
                }
                fragmentManager.beginTransaction()
                        .replace(R.id.container, nextFragment, tagName)
                        .commit();
            }
        }
    }

    public void setTitle(int titleId) {
        title = getString(titleId);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
//        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(title);
    }

    @Override
    public void onBackPressed() {
        // only close the drawer if it's opened

        // if we are coming back from the book detail fragment, reset the hamburger icon
        if (getSupportActionBar().getTitle().equals("Book Detail")) {
//            System.out.println("Closing book details");
            toggleToolbarDrawerIndicator(false);
            getSupportActionBar().setTitle(getString(R.string.books));
        }
        if (navigationDrawerFragment.isDrawerOpen()) {
            navigationDrawerFragment.closeDrawer();
            return;
        }
        if ((getSupportFragmentManager().getBackStackEntryCount() == 0 || selectedPosition == 0)
                && getSupportFragmentManager().findFragmentByTag("Book Detail") == null) {
            if (backPressFlag == 0) {
                backPressFlag = 1;
                Toast.makeText(this, getString(R.string.back_button_press), Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        backPressFlag = 0;
                    }
                }, 3000);
            } else {
                finish();
            }
        } else {
            if (getSupportActionBar().getTitle().equals(getString(R.string.about)) ||
                    getSupportActionBar().getTitle().equals(getString(R.string.scan))) {
                navigationDrawerFragment.selectItem(0);
                selectedPosition = 0;
                getSupportActionBar().setTitle(getString(R.string.books));
            }else {
                super.onBackPressed();
            }
        }
    }

    public void toggleToolbarDrawerIndicator(boolean backToHome) {
        // if we are not on a tablet in landscape mode
        if (findViewById(R.id.right_container) == null) {
            navigationDrawerFragment.toggleToolbarDrawerIndicator(backToHome);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!navigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else if (item.getItemId() == android.R.id.home) {

            // hide keyboard when the drawer is opened
            hideKeyboardFromActivity(this);

            // if we are not on a tablet in landscape mode
            if (findViewById(R.id.right_container) == null) {

                // if we are coming back from the bookdetail fragment, reset the hamburger
                if (getSupportActionBar().getTitle().equals("Book Detail")) {
                    getSupportFragmentManager().popBackStack();
                    toggleToolbarDrawerIndicator(false);
                    return true;
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReciever);
        super.onDestroy();
    }

    @Override
    public void onItemSelected(String ean) {
        Bundle args = new Bundle();
        args.putString(BookDetail.EAN_KEY, ean);

        BookDetail fragment = new BookDetail();
        fragment.setArguments(args);

        int id = R.id.container;
        if (findViewById(R.id.right_container) != null) {
            id = R.id.right_container;
        }
        getSupportActionBar().setTitle("Book Detail");
        getSupportFragmentManager().beginTransaction()
                .replace(id, fragment, "Book Detail")
                .addToBackStack("Book Detail")
                .commit();
        toggleToolbarDrawerIndicator(true);
        hideKeyboardFromActivity(this);

    }

    private class MessageReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra(MESSAGE_KEY) != null) {
                Toast.makeText(MainActivity.this, intent.getStringExtra(MESSAGE_KEY), Toast.LENGTH_LONG).show();
                if (getSupportFragmentManager().findFragmentByTag(getString(R.string.add_book_fragment)) != null) {
                    AddBook addBook = (AddBook) getSupportFragmentManager().findFragmentByTag(
                            getString(R.string.add_book_fragment)
                    );
                    addBook.toggleProgressVisibility();
                }
            }
        }
    }

    public static void hideKeyboardFromActivity(Activity activity) {

        // get the inputmanager from the activity
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);

        // find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();

        // if no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }

        // and hide the keyboard
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void goBack(View view) {
        getSupportFragmentManager().popBackStack();
        toggleToolbarDrawerIndicator(false);
    }

    private boolean isTablet() {
        return (getApplicationContext().getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String contents = data.getStringExtra(SCAN_RESULT);
            String format = data.getStringExtra(SCAN_RESULT_FORMAT);
            // We run the task to check the format and data availability of the barcode scanned
            ((AddBook) getSupportFragmentManager().findFragmentByTag(getString
                    (R.string.add_book_fragment))).fetchData(contents);
        }
//        else if (resultCode == RESULT_CANCELED)
//        {
//
//        }
    }
}