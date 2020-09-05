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

public class Notifications extends AppCompatActivity {

    private ArrayList<constsettings> listaconfignotificacion;
    ListView lista;
    DatabaseReference mDatabase;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        lista = (ListView)findViewById(R.id.listconfignotif);
        listaconfignotificacion=new ArrayList<constsettings>();
        listaconfignotificacion.add(new constsettings(getResources().getString(R.string.msg7),getResources().getString(R.string.msg8), false));

        final Notifications.AdaptadorNotificaciones adaptador = new Notifications.AdaptadorNotificaciones(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseUser = firebaseAuth.getCurrentUser();

        String userID = firebaseAuth.getCurrentUser().getUid();
        Query query =mDatabase.child("confignotificaciones").child(userID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int position =0;
                    for(DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                        try {
                            boolean activado = userSnapshot.getValue(Boolean.class);
                            listaconfignotificacion.get(position).
                                    Checked(activado);
                            position++;
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
        lista.setAdapter(adaptador);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseUser = firebaseAuth.getCurrentUser();
    }

    class AdaptadorNotificaciones extends ArrayAdapter<constsettings> {

        AppCompatActivity appCompatActivity;

        AdaptadorNotificaciones(AppCompatActivity context) {
            super(context, R.layout.item_config, listaconfignotificacion);
            appCompatActivity = context;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = appCompatActivity.getLayoutInflater();
            View item = inflater.inflate(R.layout.item_config, null);

            TextView textView1 = (TextView) item.findViewById(R.id.configuracion);
            textView1.setText(listaconfignotificacion.get(position).getNombre());

            TextView textView2 = (TextView) item.findViewById(R.id.descconfiguracion);
            textView2.setText(listaconfignotificacion.get(position).getDescripcion());

            final Switch checkBox = (Switch) item.findViewById(R.id.swconfig);

            checkBox.setChecked(listaconfignotificacion.get(position).isChecked());
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String idusuario = firebaseUser.getUid();
                    if (checkBox.isChecked()) {
                        //listaprivacidad.get(position).Checked(true);
                        switch (position) {
                            case 0:
                                constconfignotif confnotificacion = new constconfignotif(true);
                                mDatabase.child("confignotificaciones").child(idusuario).setValue(confnotificacion);
                                break;
                        }
                    } else {
                        //listaprivacidad.get(position).Checked(false);
                        switch (position) {
                            case 0:
                                constconfignotif confnotificacion = new constconfignotif(false);
                                mDatabase.child("confignotificaciones").child(idusuario).setValue(confnotificacion);
                                break;
                        }
                    }
                }
            });

            return (item);

        }
    }
}
