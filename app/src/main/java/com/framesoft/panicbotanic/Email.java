package com.framesoft.panicbotanic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Email extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    DatabaseReference mDatabase;


    private Button btnconfirmacion,btnmodificar;
    private static final String TAG = Email.class.getName();
    EditText email,nuevoemail;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        userID = firebaseAuth.getCurrentUser().getUid();

        btnconfirmacion =  (Button) findViewById(R.id.confirmacion);
        btnmodificar = (Button) findViewById(R.id.modificar);
        email = (EditText) findViewById(R.id.email);
        nuevoemail = (EditText) findViewById(R.id.nuevoemail);




        Query query =mDatabase.child("usuario").child(userID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for(DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                        try {
                            //  constfavorito numeros = userSnapshot.getValue(constfavorito.class);
                            String clave = userSnapshot.getKey();
                            switch (clave) {
                                case "email":
                                String semail = userSnapshot.getValue(String.class);
                                email.setText(semail);
                                //correo = semail;
                                break;
                            }

                        }catch (Exception ex){
                            Toast.makeText(getApplicationContext(), "Error  " + ex.getMessage().toString(), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


        btnconfirmacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                user.sendEmailVerification()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // email sent
                                    Toast.makeText(getApplicationContext(), "Confirma desde tu correo " , Toast.LENGTH_SHORT).show();

                                }
                                else
                                {
                                    // email not sent, so display message and restart the activity or do whatever you wish to do

                                    //restart this activity
                                    overridePendingTransition(0, 0);
                                    finish();
                                    overridePendingTransition(0, 0);
                                    startActivity(getIntent());

                                }
                            }
                        });

            }
        });

        btnmodificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (email.getText().toString().isEmpty() || nuevoemail.getText().toString().isEmpty()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Email.this);
                        builder.setTitle(R.string.error);
                        builder.setMessage(R.string.msg13);
                        builder.setPositiveButton(R.string.ok, null);
                        builder.create();
                        builder.show();
                    }else {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String newEmail = nuevoemail.getText().toString();

                        user.updateEmail(newEmail)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), R.string.correcto, Toast.LENGTH_SHORT)
                                                    .show();
                                        }
                                    }
                                });
                    }
                }catch (Exception ex){
                    Toast.makeText(getApplicationContext(), R.string.msg2 + ex.getMessage().toString(), Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

    }




}
