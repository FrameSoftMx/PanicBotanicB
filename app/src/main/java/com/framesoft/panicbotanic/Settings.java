package com.framesoft.panicbotanic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.security.Policy;

public class Settings extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;
    ListView list;

    String[] maintitle ={
            "NOTIFICATIONS","PERMITIONS",
            "REPORT AN ISSUE","HELP",
            "PRIVACY POLICY","LOG OUT"
    };

    String[] subtitle ={
            "Descripcion1","Descripcion2",
            "Descripcion3","Descripcion4",
            "Descripcion5","Otra desc"
    };

    Integer[] imgid={
            R.drawable.notificacion,R.drawable.megusta,
            R.drawable.reporte,R.drawable.ayuda,
            R.drawable.legal,R.drawable.salir,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseAuth.getInstance().signOut();


        MyListAdapter adapter=new MyListAdapter(Settings.this, maintitle, subtitle,imgid);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                if(position == 0) {
                    //code specific to first list item
                   //Toast.makeText(getApplicationContext(),"Place Your First Option Code",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),Notifications.class);
                    startActivity(intent);
                }

                else if(position == 1) {
                    //code specific to 2nd list item
                    //Toast.makeText(getApplicationContext(),"Place Your Second Option Code",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),Permitions.class);
                    startActivity(intent);
                }

                else if(position == 2) {
                   // Toast.makeText(getApplicationContext(),"Place Your Third Option Code",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),Reports.class);
                    startActivity(intent);
                }
                else if(position == 3) {
                    Toast.makeText(getApplicationContext(),"Place Your Forth Option Code",Toast.LENGTH_SHORT).show();
                }
                else if(position == 4) {
                    //Toast.makeText(getApplicationContext(),"Place Your Fifth Option Code",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),Politic.class);
                    startActivity(intent);
                }
                else if(position == 5) {
                    //Toast.makeText(getApplicationContext(),"Place Your Fifth Option Code",Toast.LENGTH_SHORT).show();
                    try {
                        firebaseAuth.signOut();
                        Toast.makeText(Settings.this, "User Sign out!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),Login.class);
                        startActivity(intent);
                    }catch (Exception e) {
                        //Log.e(Settings.this, "onClick: Exception "+e.getMessage(),e );
                        Toast.makeText(Settings.this, "User Sign out!" +e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });


    }
}
