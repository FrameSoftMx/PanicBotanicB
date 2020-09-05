package com.framesoft.panicbotanic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.Locale;

public class VerNotificacion extends AppCompatActivity {
    TextView title,message,date;
    DatabaseReference mDatabase;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String idusuario;
    Date dfecha;
    Boolean permiso = false;
    String nuevafecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_notificacion);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser == null){
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
            return;
        }
        String titulo = getIntent().getExtras().getString("titulo");
        String mensaje = getIntent().getExtras().getString("mensaje");
        String sfecha = getIntent().getExtras().getString("fecha");

        Locale esLocale = new Locale("es", "ES");
        nuevafecha = sfecha.replaceAll("-", "/");
        //setupActionBar();
       // this.setTitle(titulo);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        idusuario = firebaseAuth.getCurrentUser().getUid();

        try {
            mDatabase.child("notificaciones").child(idusuario).child(sfecha).child("visto").setValue(true);
        }catch (Exception e){

        }
        // title = (TextView)findViewById(R.id.tvwtitle);
        message=(TextView)findViewById(R.id.tvwmessage);
        date = (TextView)findViewById(R.id.tvwdate);

        //title.setText(titulo);
        message.setText(mensaje);
        //date.setText(sfecha);
        date.setText(nuevafecha);


    }
}
