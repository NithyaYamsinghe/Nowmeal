package com.example.nowmeal.admin.ui.food_list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.nowmeal.R;
import com.example.nowmeal.admin.adapter.MyFoodListAdapter;
import com.example.nowmeal.admin.common.Common;
import com.example.nowmeal.admin.model.FoodModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FoodListFragment extends Fragment {

    private FoodListViewModel fooodListViewModel;

    Unbinder unbinder;
    MyFoodListAdapter adapter;
    private FoodListViewModel foodListViewModel;
    LayoutAnimationController layoutAnimationController;

    @BindView(R.id.recycler_food_list)
    RecyclerView recycler_food_list;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        fooodListViewModel =
                ViewModelProviders.of(this).get(FoodListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_admin_food_list, container, false);

        unbinder = ButterKnife.bind(this, root);
        initViews();


        foodListViewModel.getMutableLiveDataFoodList().observe(getViewLifecycleOwner(), new Observer<List<FoodModel>>() {
            @Override
            public void onChanged(List<FoodModel> foodModels) {
                adapter = new MyFoodListAdapter(getContext(), foodModels);
                recycler_food_list.setAdapter(adapter);
                recycler_food_list.setLayoutAnimation(layoutAnimationController);
            }
        });

        return root;
    }

    private void initViews() {

            // change the title bar when click a category
            ((AppCompatActivity)getActivity())
                    .getSupportActionBar()
                    .setTitle(Common.categorySelected.getName());

            recycler_food_list.setHasFixedSize(true);
            recycler_food_list.setLayoutManager(new LinearLayoutManager(getContext()));
            layoutAnimationController  = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_item_from_left);

        }
    }

