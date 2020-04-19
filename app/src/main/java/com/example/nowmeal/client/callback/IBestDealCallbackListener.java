package com.example.nowmeal.client.callback;

import com.example.nowmeal.client.model.BestDealModel;

import java.util.List;

public interface IBestDealCallbackListener {

    void onBestDealLoadSuccess(List<BestDealModel> bestDealModels);

    void onBestDealLoadFailed(String message);
}
