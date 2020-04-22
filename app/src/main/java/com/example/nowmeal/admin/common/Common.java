package com.example.nowmeal.admin.common;

import com.example.nowmeal.admin.model.CategoryModel;
import com.example.nowmeal.admin.model.FoodModel;
import com.example.nowmeal.admin.model.ServerUserModel;

public class Common {

    public static final String SERVER_REF = "Server";
    public static final String CATEGORY_REF = "Category";
    public static ServerUserModel currentServerUser;
    public static CategoryModel categorySelected;

    public static final int DEFAULT_COLUMN_COUNT = 0;
    public static final int FULL_WIDTH_COLUMN = 1;
    public  static FoodModel selectedFood;
}
