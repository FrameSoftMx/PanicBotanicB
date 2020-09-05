package com.framesoft.panicbotanic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Permitions extends AppCompatActivity {
    private ArrayList<constsettings> listaprivacidad;
    ListView lista;
    DatabaseReference mDatabase;
    FirebaseAuth firebaseAuth;
     FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permitions);



        lista = (ListView)findViewById(R.id.listaprivacidad);
        listaprivacidad=new ArrayList<constsettings>();
        listaprivacidad.add(new constsettings(getResources().getString(R.string.acceso),getResources().getString(R.string.descacceso), false));
        //listaprivacidad.add(new Settings(getResources().getString(R.string.permubicacion),getResources().getString(R.string.descubicacion), false));
        //listaprivacidad.add(new Settings(getResources().getString(R.string.msg11),getResources().getString(R.string.msg12)));

        final Permitions.AdaptadorPrivacidad adaptador = new Permitions.AdaptadorPrivacidad(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();


        firebaseAuth = FirebaseAuth.getInstance();
        //firebaseUser = firebaseAuth.getCurrentUser();
      FirebaseUser  mUser = FirebaseAuth.getInstance().getCurrentUser();


        try {
        //String userID = firebaseAuth.getCurrentUser().getUid();
        String user_id = mUser.getUid();

        Query query =mDatabase.child("privacidad").child(user_id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int position =0;
                    for(DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                        try {
                            boolean activado = userSnapshot.getValue(Boolean.class);
                            listaprivacidad.get(position).Checked(activado);
                            position++;
                            //constbloqueados numeros = new constbloqueados();
                            //numeros.setTelefono(cel);
                        }catch (Exception ex){
                            Toast.makeText(getApplicationContext(), "Error " + ex.getMessage().toString(), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                }
                adaptador.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        }catch(Exception ex) {
            Toast.makeText(getApplicationContext(), "Error " + ex.getMessage().toString(), Toast.LENGTH_SHORT)
                    .show();
        }

        lista.setAdapter(adaptador);
    }

    class AdaptadorPrivacidad extends ArrayAdapter<constsettings> {

        AppCompatActivity appCompatActivity;

        AdaptadorPrivacidad(AppCompatActivity context) {
            super(context, R.layout.item_config, listaprivacidad);
            appCompatActivity = context;
        }
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = appCompatActivity.getLayoutInflater();
            View item = inflater.inflate(R.layout.item_config, null);

            TextView textView1 = (TextView) findViewById(R.id.configuracion);
            textView1.setText(listaprivacidad.get(position).getNombre());

            TextView textView2 = (TextView) findViewById(R.id.descconfiguracion);
            textView2.setText(listaprivacidad.get(position).getDescripcion());

            final Switch checkBox = (Switch) findViewById(R.id.swconfig);

            checkBox.setChecked(listaprivacidad.get(position).isChecked());
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String idusuario = firebaseUser.getUid();
                    if(checkBox.isChecked()) {
                        //listaprivacidad.get(position).Checked(true);
                        switch (position) {
                            case 0:
                                mDatabase.child("privacidad").child(idusuario).child("acontacto").setValue(true);
                                break;
                            case 1:
                                mDatabase.child("privacidad").child(idusuario).child("ubicacion").setValue(true);
                                break;
                        }
                    }
                    else {
                        //listaprivacidad.get(position).Checked(false);
                        switch (position) {
                            case 0:
                                mDatabase.child("privacidad").child(idusuario).child("acontacto").setValue(false);
                                break;
                            case 1:
                                mDatabase.child("privacidad").child(idusuario).child("ubicacion").setValue(false);
                                break;
                        }
                    }
                }
            });

            if(position == 2){
                checkBox.setVisibility(View.GONE);
            }
            return(item);

        }
    }



}
