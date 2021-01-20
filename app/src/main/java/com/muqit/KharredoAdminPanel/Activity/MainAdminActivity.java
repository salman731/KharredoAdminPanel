package com.muqit.KharredoAdminPanel.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.AppBarConfiguration.Builder;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.muqit.KharredoAdminPanel.R;
import com.muqit.KharredoAdminPanel.utils.SessionManager;

public class MainAdminActivity extends AppCompatActivity {
    private TextView AdminEmail;
    private AppBarConfiguration mAppBarConfiguration;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_main_admin);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.AdminEmail)).setText(getIntent().getStringExtra(NotificationCompat.CATEGORY_EMAIL));
        this.mAppBarConfiguration = new Builder(R.id.nav_Dashboard, R.id.nav_EmailFeed, R.id.nav_Orders, R.id.nav_Sales, R.id.nav_Chat, R.id.nav_Employees, R.id.nav_Users, R.id.nav_Vendors, R.id.nav_AffliateMarketers, R.id.nav_ProductList, R.id.nav_Category, R.id.nav_Featured, R.id.nav_Display_blogs, R.id.nav_Banners,R.id.nav_Insertion_blogs,R.id.nav_Page).setDrawerLayout(drawerLayout).build();
        NavController findNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController((AppCompatActivity) this, findNavController, this.mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, findNavController);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_admin, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.logout) {
            new SessionManager(this).logoutUser();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.nav_host_fragment), this.mAppBarConfiguration) || super.onSupportNavigateUp();
    }
}
