package com.example.nowmeal.client.callback;

import com.example.nowmeal.client.model.PopularCategoryModel;

import java.util.List;

public interface IPopularCallbackListener {

    void onPopularLoadSuccess(List<PopularCategoryModel> popularCategoryModels);

    void onPopularLoadFailed(String message);

}

