package com.example.nowmeal.client;

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
import com.example.nowmeal.client.common.Common;
import com.example.nowmeal.client.model.UserModel;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
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

public class MainClientActivity extends AppCompatActivity {

    public static int APP_REQUEST_CODE = 7171; // any number
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener listener;
    private AlertDialog dialog;
    private DatabaseReference userRef;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_client);
        init();
    }


    private void init() {
        providers = Arrays.asList(new AuthUI.IdpConfig.PhoneBuilder().build());
        userRef = FirebaseDatabase.getInstance().getReference(Common.USER_REFERENCES);
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
        userRef.child(user.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            UserModel userModel = dataSnapshot.getValue(UserModel.class);
                            goToHomeActivity(userModel);
                        } else {

                            showRegisterDialog(user);
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {


                        dialog.dismiss();
                        Toast.makeText(MainClientActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void goToHomeActivity(UserModel userModel) {
        Common.currentUser = userModel;
        startActivity(new Intent(this, HomeActivity.class));
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

                    UserModel userModel = new UserModel();
                    userModel.setUid(user.getUid());
                    userModel.setName(edt_name.getText().toString());
                    userModel.setAddress(edt_address.getText().toString());
                    userModel.setPhone(edt_phone.getText().toString());
                    userRef.child(userModel.getUid())
                            .setValue(userModel)
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    dialog.dismiss();
                                    Toast.makeText(MainClientActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    dialog.dismiss();
                                    Toast.makeText(MainClientActivity.this, "Congratulations!, registered successfully", Toast.LENGTH_SHORT).show();
                                    goToHomeActivity(userModel);
                                }
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
