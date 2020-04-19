package com.example.nowmeal.client.callback;
import com.example.nowmeal.client.model.CategoryModel;
import java.util.List;

public interface ICategoryCallbackListener {
    void onCategoryLoadSuccess(List<CategoryModel> categoryModelList);
    void onCategoryLoadFailed(String message);
}
