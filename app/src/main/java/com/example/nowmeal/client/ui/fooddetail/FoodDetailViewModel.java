package com.example.nowmeal.client.ui.fooddetail;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.nowmeal.client.common.Common;
import com.example.nowmeal.client.model.FoodModel;

public class FoodDetailViewModel extends ViewModel {
   private MutableLiveData<FoodModel>  mutableLiveDataFood;

    public FoodDetailViewModel() {
    }

    public MutableLiveData<FoodModel> getMutableLiveDataFood() {
        if (mutableLiveDataFood == null)
            mutableLiveDataFood = new MutableLiveData<>();
        mutableLiveDataFood.setValue(Common.selectedFood);
        return mutableLiveDataFood;
    }
}
