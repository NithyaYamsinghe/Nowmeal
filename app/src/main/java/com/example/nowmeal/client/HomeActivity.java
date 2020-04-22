package com.example.nowmeal.client;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.example.nowmeal.MainActivity;
import com.example.nowmeal.R;
import com.example.nowmeal.client.common.Common;
import com.example.nowmeal.client.database.CartDataSource;
import com.example.nowmeal.client.database.CartDatabase;
import com.example.nowmeal.client.database.LocalCartDataSource;
import com.example.nowmeal.client.eventbus.CategoryClick;
import com.example.nowmeal.client.eventbus.CounterCartEvent;
import com.example.nowmeal.client.eventbus.FoodItemClick;
import com.example.nowmeal.client.eventbus.HideFABCart;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private  NavController navController;
    private  DrawerLayout drawer;
    private CartDataSource cartDataSource;


    @BindView(R.id.fab)
    CounterFab fab;


    @Override
    protected void onResume() {
        super.onResume();
        countCartItem();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);
        cartDataSource = new LocalCartDataSource(CartDatabase.getInstance(this).cartDAO());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.nav_cart);
            }
        });
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_menu , R.id.nav_food_list, R.id.nav_food_detail, R.id.nav_cart)
                .setDrawerLayout(drawer)
                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront(); // fixed

        View headerView = navigationView.getHeaderView(0);
        TextView txt_user =  (TextView) headerView.findViewById(R.id.txt_user);
        Common.setSpanString("Hey ", Common.currentUser.getName(), txt_user);

        countCartItem();


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    // event bus
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        drawer.closeDrawers();
        switch (item.getItemId())
        {

            case R.id.nav_home:
                navController.navigate(R.id.nav_home);
                break;
            case R.id.nav_menu:
                navController.navigate(R.id.nav_menu);
                break;
            case R.id.nav_cart:
                navController.navigate(R.id.nav_cart);
                break;
            case R.id.nav_sign_out:
               signOut();
                break;

        }
        return true;
    }

    private void signOut() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("sign out").setMessage("Do you really want to sign out?").setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Common.selectedFood = null;
                Common.categorySelected = null;
                Common.currentUser = null;
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();



            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onCategorySelected(CategoryClick event){
        if (event.isSuccess()){
            navController.navigate(R.id.nav_food_list);
//            Toast.makeText(this, "Click to "+event.getCategoryModel().getName(), Toast.LENGTH_SHORT).show();
        }
    }



    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onFoodItemClick(FoodItemClick event){
        if (event.isSuccess()){
            navController.navigate(R.id.nav_food_detail);

        }
    }



    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onHideFABEvent(HideFABCart event){
        if (event.isHidden()){

            fab.hide();

        }
        else
            fab.show();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onCartCounter(CounterCartEvent event){
        if (event.isSuccess()){
                
            countCartItem();

        }
    }

    private void countCartItem() {

    cartDataSource.countItemInCart(Common.currentUser.getUid())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Integer>() {
        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onSuccess(Integer integer) {
            fab.setCount(integer);

        }

        @Override
        public void onError(Throwable e) {
            if (!e.getMessage().contains("Query returned empty"))

             {

                 Toast.makeText(HomeActivity.this, "[COUNT CART]"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            else
                fab.setCount(0);


        }
    });
    }
}
