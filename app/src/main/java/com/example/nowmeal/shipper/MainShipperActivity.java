package com.example.nowmeal.shipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nowmeal.R;
import com.example.nowmeal.shipper.common.Common;
import com.example.nowmeal.shipper.model.ShipperUserModel;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class MainShipperActivity extends AppCompatActivity {

    private static int APP_REQUEST_CODE = 7171;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener listener;
    private AlertDialog dialog;
    private DatabaseReference serverRef;
    private List<AuthUI.IdpConfig> providers;


    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(listener);
    }


    @Override
    protected void onStop() {
        if (listener != null)
            firebaseAuth.removeAuthStateListener(listener);
        super.onStop();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        providers = Arrays.asList(new AuthUI.IdpConfig.PhoneBuilder().build());
        serverRef = FirebaseDatabase.getInstance().getReference(Common.SHIPPER_REF);
        firebaseAuth = FirebaseAuth.getInstance();
        dialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();
        listener = firebaseAuthLocal -> {

            FirebaseUser user = firebaseAuthLocal.getCurrentUser();
            if (user != null) {

                // check user from  firebase
                checkUserFromFirebase(user);

            } else {
                phoneLogin();
            }

        };
    }

    private void checkUserFromFirebase(FirebaseUser user) {

        dialog.show();
        serverRef.child(user.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            ShipperUserModel shipperUserModel = dataSnapshot.getValue(ShipperUserModel.class);
                            goToHomeActivity(shipperUserModel);
                        } else {

                            showRegisterDialog(user);
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {


                        dialog.dismiss();
                        Toast.makeText(MainShipperActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void goToHomeActivity(ShipperUserModel shipperUserModel) {
        Common.currentShipperUser = shipperUserModel;
        startActivity(new Intent(this, HomeShipperActivity.class));
        finish();

    }


    private void showRegisterDialog(FirebaseUser user) {

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Register");
        builder.setMessage("Please fill this information");

        View itemView = LayoutInflater.from(this).inflate(R.layout.layout_register, null);
        EditText edt_name = (EditText) itemView.findViewById(R.id.edt_name);
        EditText edt_address = (EditText) itemView.findViewById(R.id.edt_address);
        EditText edt_phone = (EditText) itemView.findViewById(R.id.edt_phone);


        // set data
        edt_phone.setText(user.getPhoneNumber());
        builder.setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("REGISTER", (dialogInterface, which) -> {
                    if (TextUtils.isEmpty(edt_name.getText().toString())) {
                        Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (TextUtils.isEmpty(edt_address.getText().toString())) {
                        Toast.makeText(this, "Please enter your address", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    ShipperUserModel shipperUserModel = new ShipperUserModel();
                    shipperUserModel.setUid(user.getUid());
                    shipperUserModel.setName(edt_name.getText().toString());
                    shipperUserModel.setAddress(edt_address.getText().toString());
                    shipperUserModel.setPhone(edt_phone.getText().toString());
                    shipperUserModel.setActive(false);

                    dialog.show();

                    serverRef.child(shipperUserModel.getUid())
                            .setValue(shipperUserModel)
                            .addOnFailureListener(e -> {
                                dialog.dismiss();
                                Toast.makeText(MainShipperActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }).addOnCompleteListener(task -> {
                                dialog.dismiss();
                                Toast.makeText(MainShipperActivity.this, "Congradulations! Register successfull. Admin will check and verify you later", Toast.LENGTH_SHORT).show();
                    });

                });

        builder.setView(itemView);

        androidx.appcompat.app.AlertDialog registerDialog = builder.create();
        registerDialog.show();


    }

    private void phoneLogin() {
        startActivityForResult(AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(), APP_REQUEST_CODE
        );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (requestCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            } else {
                Toast.makeText(this, "Failed to sign in", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
