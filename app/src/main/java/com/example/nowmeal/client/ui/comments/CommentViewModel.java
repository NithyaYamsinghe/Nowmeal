package com.example.nowmeal.client.ui.comments;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.nowmeal.client.model.CommentModel;
import com.example.nowmeal.client.model.FoodModel;

import java.util.List;

public class CommentViewModel extends ViewModel {

    private MutableLiveData <List<CommentModel>> mutableLiveDataFoodList;

    public CommentViewModel() {

        mutableLiveDataFoodList = new MutableLiveData<>();

    }

    public MutableLiveData<List<CommentModel>> getMutableLiveDataFoodList() {
        return mutableLiveDataFoodList;
    }

    public void setCommentList(List<CommentModel> commentList){
        mutableLiveDataFoodList.setValue(commentList);
    }
}
