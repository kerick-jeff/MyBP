package com.mybp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    NavigationView navigationView = null;
    Toolbar toolbar = null;
    FloatingActionMenu fabMenu;
    FloatingActionButton fabPlan, fabIncome, fabExpenses;
    View viewDimmer;

    private String lastActiveFragment = null;
    static final String LAST_ACTIVE_FRAGMENT = "LAST_ACTIVE_FRAGMENT";

    @Override
    @SuppressWarnings("deprecation")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseHelper.getInstance(this);

        new RateApp().appLaunched(this);

        // Set DashboardFragment initially
        DashboardFragment dashboardFragment = new DashboardFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, dashboardFragment);
        fragmentTransaction.commit();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Dashboard");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_dashboard);

        /* Setup Floating action button */
        viewDimmer = (View) findViewById(R.id.viewDimmer);
        fabMenu = (FloatingActionMenu) findViewById(R.id.fabMenu);

        viewDimmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabMenu.close(true);
            }
        });

        fabMenu.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if(opened) { // build FloatingActionMenu & FloatingActionButton(s) dynamically at runtime
                    fabPlan = new FloatingActionButton(MainActivity.this);
                    fabPlan.setLabelText(getResources().getString(R.string.nav_plan));
                    fabPlan.setImageDrawable(getResources().getDrawable(R.drawable.ic_assignment_white_24dp));
                    fabPlan.setButtonSize(FloatingActionButton.SIZE_MINI);
                    fabPlan.setColorNormal(getResources().getColor(R.color.blue));
                    fabPlan.setColorPressed(getResources().getColor(R.color.blue_deep));
                    fabPlan.setColorRipple(getResources().getColor(R.color.fab_ripple));
                    fabPlan.setShowShadow(true);
                    fabPlan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            toolbar.setTitle("Create Plan");
                            lastActiveFragment = "PlanFragment";

                            PlanFragment planFragment = new PlanFragment();
                            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container, planFragment);
                            fragmentTransaction.commit();

                            navigationView.setCheckedItem(R.id.nav_plan); // navigation view updates selected item
                            fabMenu.close(true);
                        }
                    });

                    fabIncome = new FloatingActionButton(MainActivity.this);
                    fabIncome.setLabelText(getResources().getString(R.string.nav_income));
                    fabIncome.setImageDrawable(getResources().getDrawable(R.drawable.ic_attach_money_white_24dp));
                    fabIncome.setButtonSize(FloatingActionButton.SIZE_MINI);
                    fabIncome.setColorNormal(getResources().getColor(R.color.green));
                    fabIncome.setColorPressed(getResources().getColor(R.color.green_deep));
                    fabIncome.setColorRipple(getResources().getColor(R.color.fab_ripple));
                    fabIncome.setShowShadow(true);
                    fabIncome.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            toolbar.setTitle("Income");
                            lastActiveFragment = "IncomeFragment";

                            IncomeFragment incomeFragment = new IncomeFragment();
                            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container, incomeFragment);
                            fragmentTransaction.commit();

                            navigationView.setCheckedItem(R.id.nav_income);
                            fabMenu.close(true);
                        }
                    });

                    fabExpenses = new FloatingActionButton(MainActivity.this);
                    fabExpenses.setLabelText(getResources().getString(R.string.nav_expenses));
                    fabExpenses.setImageDrawable(getResources().getDrawable(R.drawable.ic_money_off_white_24dp));
                    fabExpenses.setButtonSize(FloatingActionButton.SIZE_MINI);
                    fabExpenses.setColorNormal(getResources().getColor(R.color.red));
                    fabExpenses.setColorPressed(getResources().getColor(R.color.red_deep));
                    fabExpenses.setColorRipple(getResources().getColor(R.color.fab_ripple));
                    fabExpenses.setShowShadow(true);
                    fabExpenses.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            toolbar.setTitle("Expenses");
                            lastActiveFragment = "ExpensesFragment";

                            ExpensesFragment expensesFragment = new ExpensesFragment();
                            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container, expensesFragment);
                            fragmentTransaction.commit();

                            navigationView.setCheckedItem(R.id.nav_expenses);
                            fabMenu.close(true);
                        }
                    });

                    fabMenu.addMenuButton(fabPlan);
                    fabMenu.addMenuButton(fabIncome);
                    fabMenu.addMenuButton(fabExpenses);
                } else {
                    fabMenu.removeAllMenuButtons();
                }

                setBackgroundDimming(opened);
            }

            private void setBackgroundDimming(boolean dimmed) {
                final float targetAlpha = dimmed ? 1f : 0;
                final int endVisibility = dimmed ? View.VISIBLE : View.GONE;
                viewDimmer.setVisibility(View.VISIBLE);
                viewDimmer.animate()
                        .alpha(targetAlpha)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                viewDimmer.setVisibility(endVisibility);
                            }
                        })
                        .start();
            }
        });

        fabMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fabMenu.isOpened()) {
                    fabMenu.close(true);
                }
            }
        });
        /* End fab setup */
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id) {
            case R.id.action_share:
                String shareText = "Get it on Play Store. MyBP, your best Personal Finance and Budget Planner app of all times\nhttps://play.google.com/store/apps/details?id=com.google.android.apps.maps";

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");

                intent.putExtra(Intent.EXTRA_TEXT, shareText);
                intent.putExtra(Intent.EXTRA_TITLE, "MyBP: Budget Planner");
                intent.putExtra(Intent.EXTRA_SUBJECT, "MyBP: Budget Planner");

                Intent openInChooser = new Intent(intent);

                if(intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(openInChooser);
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (id == R.id.nav_dashboard) {
            toolbar.setTitle("Dashboard");
            lastActiveFragment = "DashboardFragment";

            DashboardFragment dashboardFragment = new DashboardFragment();
            fragmentTransaction.replace(R.id.fragment_container, dashboardFragment);
        } else if (id == R.id.nav_plan) {
            toolbar.setTitle("Create Plan");
            lastActiveFragment = "PlanFragment";

            PlanFragment planFragment = new PlanFragment();
            fragmentTransaction.replace(R.id.fragment_container, planFragment);
        } else if (id == R.id.nav_plans) {
            toolbar.setTitle("Budget Plans");
            lastActiveFragment = "PlansFragment";

            PlansFragment plansFragment = new PlansFragment();
            fragmentTransaction.replace(R.id.fragment_container, plansFragment);
        } else if (id == R.id.nav_income) {
            toolbar.setTitle("Income");
            lastActiveFragment = "IncomeFragment";

            IncomeFragment incomeFragment = new IncomeFragment();
            fragmentTransaction.replace(R.id.fragment_container, incomeFragment);
        } else if (id == R.id.nav_expenses) {
            toolbar.setTitle("Expenses");
            lastActiveFragment = "ExpensesFragment";

            ExpensesFragment expensesFragment = new ExpensesFragment();
            fragmentTransaction.replace(R.id.fragment_container, expensesFragment);
        } else if (id == R.id.nav_statistics) {
            toolbar.setTitle("Statistics");
            lastActiveFragment = "StatisticsFragment";

            StatisticsFragment statisticsFragment = new StatisticsFragment();
            fragmentTransaction.replace(R.id.fragment_container, statisticsFragment);
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if(id == R.id.nav_rate) {
            new RateApp().rate(this);
        } else if (id == R.id.nav_help_feedback) {
            startActivity(new Intent(this, HelpFeedbackActivity.class));
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(this, AboutActivity.class));
        }

        fragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(LAST_ACTIVE_FRAGMENT, lastActiveFragment);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        lastActiveFragment = savedInstanceState.getString(LAST_ACTIVE_FRAGMENT, null);
        if(lastActiveFragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            switch (lastActiveFragment) {
                case "DashboardFragment":
                    toolbar.setTitle("Dashboard");
                    DashboardFragment dashboardFragment = new DashboardFragment();
                    fragmentTransaction.replace(R.id.fragment_container, dashboardFragment);
                    break;
                case "PlanFragment":
                    toolbar.setTitle("Create Plan");
                    PlanFragment planFragment = new PlanFragment();
                    fragmentTransaction.replace(R.id.fragment_container, planFragment);
                    break;
                case "PlansFragment":
                    toolbar.setTitle("Budget Plans");
                    PlansFragment plansFragment = new PlansFragment();
                    fragmentTransaction.replace(R.id.fragment_container, plansFragment);
                    break;
                case "IncomeFragment":
                    toolbar.setTitle("Income");
                    IncomeFragment incomeFragment = new IncomeFragment();
                    fragmentTransaction.replace(R.id.fragment_container, incomeFragment);
                    break;
                case "ExpensesFragment":
                    toolbar.setTitle("Expenses");
                    ExpensesFragment expensesFragment = new ExpensesFragment();
                    fragmentTransaction.replace(R.id.fragment_container, expensesFragment);
                    break;
                case "StatisticsFragment":
                    toolbar.setTitle("Statistics");
                    StatisticsFragment statisticsFragment = new StatisticsFragment();
                    fragmentTransaction.replace(R.id.fragment_container, statisticsFragment);

            }
            fragmentTransaction.commit();
        }
    }
}
