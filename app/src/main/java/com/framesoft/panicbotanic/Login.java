package com.framesoft.panicbotanic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        /*if(firebaseUser != null){
            Intent intent = new Intent(getApplicationContext(), Taskbar.class);
            startActivity(intent);
            finish();
        }*/

        Button go = (Button) findViewById(R.id.entrar);
        email = (EditText) findViewById(R.id.email) ;
        password = (EditText) findViewById(R.id.password);

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    if(email.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
                        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                        builder.setTitle(R.string.error);
                        builder.setMessage(R.string.msg10);
                        builder.setPositiveButton(R.string.ok, null);
                        builder.create();
                        builder.show();
                    }else {
                        //circuloprogress.setVisibility(View.VISIBLE);
                        //iniciar.setEnabled(false);
                        String semail = email.getText().toString();
                        String spassword =password.getText().toString();
                        firebaseAuth.signInWithEmailAndPassword(semail, spassword)
                                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            //circuloprogress.setVisibility(View.VISIBLE);
                                            FirebaseUser user =firebaseAuth.getCurrentUser();
                                            Intent intent=new Intent(getApplicationContext(),Taskbar.class);
                                            startActivity(intent);
                                            finish();
                                            //overridePendingTransition(R.anim.left_in, R.anim.left_out);
                                        } else {
                                            //iniciar.setEnabled(true);
                                           //circuloprogress.setVisibility(View.INVISIBLE);
                                            Toast.makeText(getApplicationContext(), "Usuaio ó contraseña inválidos.", Toast.LENGTH_SHORT).show();
                                        }

                                        // ...
                                    }
                                });
                    }
                }catch (Exception ex){

                }


            }
        });

    }
}
