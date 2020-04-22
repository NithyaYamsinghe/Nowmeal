package com.example.nowmeal.client.eventbus;

import com.example.nowmeal.client.database.CartItem;

public class UpdateItemInCart {

    private CartItem cartItem;

    public UpdateItemInCart(CartItem cartItem) {
        this.cartItem = cartItem;
    }


    public CartItem getCartItem() {
        return cartItem;
    }

    public void setCartItem(CartItem cartItem) {
        this.cartItem = cartItem;
    }
}
