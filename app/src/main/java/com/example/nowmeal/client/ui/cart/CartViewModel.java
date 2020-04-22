package com.example.nowmeal.client.ui.cart;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.nowmeal.client.common.Common;
import com.example.nowmeal.client.database.CartDataSource;
import com.example.nowmeal.client.database.CartDatabase;
import com.example.nowmeal.client.database.CartItem;
import com.example.nowmeal.client.database.LocalCartDataSource;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CartViewModel extends ViewModel {

    private MutableLiveData<List<CartItem>> mutableLiveDataCartItems;
    private CompositeDisposable compositeDisposable;
    private CartDataSource cartDataSource;



    public CartViewModel() {
        compositeDisposable = new CompositeDisposable();
    }


    public void initCartDataSource(Context context){

        cartDataSource = new LocalCartDataSource(CartDatabase.getInstance(context).cartDAO());
    }

    public void onStop(){
        compositeDisposable.clear();

    }
    public MutableLiveData<List<CartItem>> getMutableLiveDataCartItems() {
        if (mutableLiveDataCartItems == null)
            mutableLiveDataCartItems = new MutableLiveData<>();

        getAllCartItems();
        return mutableLiveDataCartItems;
    }

    private void getAllCartItems() {
        compositeDisposable.add(cartDataSource.getAllCart(Common.currentUser.getUid()).
                subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        .subscribe(cartItems -> {

            mutableLiveDataCartItems.setValue(cartItems);

        }, throwable -> {

            mutableLiveDataCartItems.setValue(null);

        }));


    }
}