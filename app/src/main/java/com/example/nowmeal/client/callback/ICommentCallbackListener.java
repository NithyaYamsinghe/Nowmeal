package com.example.nowmeal.client.callback;

import com.example.nowmeal.client.model.CommentModel;

import java.util.List;

public interface ICommentCallbackListener {

    void onCommentLoadSuccess(List<CommentModel> commentModels);
    void onCommentLoadFailed(String message);

}
