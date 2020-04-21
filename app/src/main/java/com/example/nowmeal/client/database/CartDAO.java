package com.example.nowmeal.client.database;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface CartDAO {

    @Query("SELECT * FROM Cart WHERE uid=:uid")
    Flowable<List<CartItem>> getAllCart(String uid);

    @Query("SELECT COUNT(*) FROM Cart WHERE uid=:uid")
    Single<Integer> countItemInCart(String uid);

//    @Query("SELECT SUM() FROM Cart WHERE uid=:uid")
//    Single<Integer> countItemInCart(String uid);




}
