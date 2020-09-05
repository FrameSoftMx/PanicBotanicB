package com.framesoft.panicbotanic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;


import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {

    EditText nombre,contrasena,confcontraseña,celular,email;
    CheckBox terminos;
    Button registro;
    private FirebaseAuth firebaseAuth;

   // FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        // mAuth = FirebaseAuth.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

         registro = (Button) findViewById(R.id.regristro);
         nombre = (EditText) findViewById(R.id.nombre);
         contrasena = (EditText) findViewById(R.id.contrasena);
         confcontraseña = (EditText) findViewById(R.id.confconstrasena);
         celular = (EditText) findViewById(R.id.celular);
         email = (EditText) findViewById(R.id.correo);
        terminos = (CheckBox) findViewById(R.id.chkterminos);
        TextView textView = findViewById(R.id.terminos);
        String text = getResources().getString(R.string.terminos);

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (nombre.getText().toString().isEmpty() || contrasena.getText().toString().isEmpty() || confcontraseña.getText().toString().isEmpty()|| celular.getText().toString().isEmpty()|| email.getText().toString().isEmpty()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Signup.this);
                        builder.setTitle(R.string.error);
                        builder.setMessage(R.string.msg10);
                        builder.setPositiveButton(R.string.ok, null);
                        builder.create();
                        builder.show();
                    } else if (!terminos.isChecked()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Signup.this);
                        builder.setTitle(R.string.error);
                        builder.setMessage(R.string.msg15);
                        builder.setPositiveButton(R.string.ok, null);
                        builder.create();
                        builder.show();
                    } else {


                        final String snombre = nombre.getText().toString();
                        final String scontrasena = contrasena.getText().toString();
                        final String sconfcont = confcontraseña.getText().toString();
                        final String scelular = celular.getText().toString();
                        final String semail = email.getText().toString();

                         final constusuarios usuario = new constusuarios(snombre,scontrasena,sconfcont,scelular,semail);
                       // final constprivacidad privacidad = new constprivacidad(false,false);
                        //final constconfignotif notificaciones = new constconfignotif(false);
                        firebaseAuth.createUserWithEmailAndPassword(semail,scontrasena).
                                addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()) {
                                            try {

                                                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                                    Toast.makeText(Signup.this, "Este usuario ya está registrado .", Toast.LENGTH_SHORT).show();
                                                }

                                                String userID = firebaseAuth.getCurrentUser().getUid();
                                                mDatabase.child("usuario").child(userID).setValue(usuario);
                                                String fcmToken = FirebaseInstanceId.getInstance().getToken();
                                                mDatabase.child("usuario").child(userID).child("token").setValue(fcmToken);
                                                //mDatabase.child("favorito").child(userID);
                                                //mDatabase.child("privacidad").child(userID).setValue(privacidad);
                                                //mDatabase.child("bloqueado").child(userID);
                                                //mDatabase.child("historial").child(userID);
                                                //mDatabase.child("redirigido").child(userID).child("estatus").setValue(false);
                                               // mDatabase.child("dispositivos").child(userID);//.child(getMacAddress()).setValue(dir);
                                                //mDatabase.child("confignotificaciones").child(userID).setValue(notificaciones);
                                                Intent intent=new Intent(getApplicationContext(),Login.class);
                                                startActivity(intent);
                                                finish();
                                                //overridePendingTransition(R.anim.left_in, R.anim.left_out);
                                                //Toast.makeText(getApplicationContext(), R.string.msg2, Toast.LENGTH_SHORT)
                                                //  .show();
                                            }catch (Exception ex){
                                                Toast.makeText(getApplicationContext(), R.string.msg2 + ex.getMessage().toString(), Toast.LENGTH_SHORT)
                                                        .show();
                                            }
                                        }else{
                                            Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT)
                                                    .show();
                                        }
                                    }
                                });
                        //Toast.makeText(getApplicationContext(), R.string.msg2, Toast.LENGTH_SHORT)
                        //.show();
                    }
                }catch (Exception ex){
                    Toast.makeText(getApplicationContext(), R.string.msg2 + ex.getMessage().toString(), Toast.LENGTH_SHORT)
                            .show();
                }

            }

    });


    }

}
