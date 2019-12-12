package com.example.parkls;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.auth.User;

public class Signup_activity extends AppCompatActivity {


    private EditText name, email_id, passwordcheck,rePass, carMake,carModel,carYear;
    //private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private static final String TAG = "";
    private ProgressBar progressBar;

// nex thing to add is to check if passwords matches
    // if not then send a toast that says passwords do not match
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);



        TextView btnSignUp = findViewById(R.id.login_page);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( Signup_activity.this, SignIn.class);
                startActivity(intent);
            }
        });


        mAuth = FirebaseAuth.getInstance();
        name = findViewById(R.id.input_name);
        email_id = findViewById(R.id.input_email);
        passwordcheck =  findViewById(R.id.input_password);
        carMake = findViewById(R.id.input_carmake);
        carModel = findViewById(R.id.input_carmodel);
        carYear = findViewById(R.id.input_caryear);
        rePass = findViewById(R.id.input_repass);

        progressBar = findViewById(R.id.progressBar);


        Button ahsignup = findViewById(R.id.btn_signup);


        ahsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = email_id.getText().toString();
                String password = passwordcheck.getText().toString();
                String input_repass = rePass.getText().toString();
                final String nameInput = name.getText().toString();
                final String carmakeInput = carMake.getText().toString();
                final String carmodelInput = carModel.getText().toString();
                final String caryearInput = carYear.getText().toString();


                if(TextUtils.isEmpty(nameInput)){

                    Toast.makeText(getApplicationContext(), "Name Field is empty", Toast.LENGTH_SHORT).show();
                    return;

                }

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter Email Id", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                else if(password.length() < 8){

                    Toast.makeText(getApplicationContext(), "Password must be at least 8 characters long", Toast.LENGTH_SHORT).show();
                    return;

                }

                else if(!password.matches(input_repass)){


                    Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;


                }

                if(TextUtils.isEmpty(carmakeInput)){
                    Toast.makeText(getApplicationContext(), "Car make is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(carmodelInput)){
                    Toast.makeText(getApplicationContext(), "Car Model is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(caryearInput)){
                    Toast.makeText(getApplicationContext(), "Car Year is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener( Signup_activity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                progressBar.setVisibility(View.GONE);

                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Intent intent = new Intent( Signup_activity.this, Home_Screen.class);
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference myRef = database.getReference("Users").child(email
                                            .replaceAll("[^a-zA-Z0-9]", ""));
                                    myRef.child("Name:").setValue(nameInput);
                                   myRef.child("Car Make:").setValue(carmakeInput);
                                   myRef.child("Car Model:").setValue(carmodelInput);
                                   myRef.child("Car Year:").setValue(caryearInput);

                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText( Signup_activity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }

                            }


                        });
            }
        });


    }



}
