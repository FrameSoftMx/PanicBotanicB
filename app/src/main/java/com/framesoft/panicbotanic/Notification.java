package com.framesoft.panicbotanic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Notification extends AppCompatActivity {
    private static final String TAG = Notification.class.getSimpleName();
    private ListView list;
    private ArrayList<constnotificaciones> listanotif;
    private ArrayList<constnotificaciones> listaordenada;
    private FirebaseAuth firebaseAuth;
    DatabaseReference mDatabase;
    LinearLayout noNotificaciones;
    String userID;
    Notification.AdaptadorNotificacion adaptador;
    int c =0;
    int resultado = 15;
    Menu menunotif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        list= (ListView) findViewById(R.id.lista);
        noNotificaciones = (LinearLayout)findViewById(R.id.nonotificaciones);
        listanotif=new ArrayList<constnotificaciones>();
        listaordenada=new ArrayList<constnotificaciones>();
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        userID = firebaseAuth.getCurrentUser().getUid();

        adaptador = new Notification.AdaptadorNotificacion(this);
        list.setAdapter(adaptador);

        Query query =mDatabase.child("notificaciones").child(userID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for(DataSnapshot userSnapshot : dataSnapshot.getChildren()){

                        try {
                            constnotificaciones notifica = userSnapshot.getValue(constnotificaciones.class);
                            listaordenada.add(notifica);
                            //c++;
                        }catch (Exception ex){
                            Toast.makeText(getApplicationContext(), "Error " + ex.getMessage().toString(), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                }
                if(listaordenada.size()>0) {
                    Collections.sort(listaordenada, new Comparator<constnotificaciones>() {
                        public int compare(constnotificaciones obj1, constnotificaciones obj2) {
                            return obj1.getFecha().compareTo(obj2.getFecha());
                        }
                    });
                    for(int i = c;i<resultado;i++){
                        if(i<listaordenada.size()) {
                            listanotif.add(listaordenada.get(i));
                            c++;
                        }else {
                            break;
                        }
                    }
                    if(listaordenada.size()>resultado)
                    {
                        menunotif.getItem(0).setVisible(true);
                    } else {
                        menunotif.getItem(0).setVisible(false);
                    }
                    resultado+=15;
                    list.setVisibility(View.VISIBLE);
                    noNotificaciones.setVisibility(View.GONE);

                }else{
                    list.setVisibility(View.GONE);
                    noNotificaciones.setVisibility(View.VISIBLE);
                    menunotif.getItem(0).setVisible(false);
                }
                adaptador.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent=new Intent(getApplicationContext(),VerNotificacion.class);
                intent.putExtra("titulo",listanotif.get(position).getTitulo());
                intent.putExtra("mensaje",listanotif.get(position).getDescripcion());
                intent.putExtra("fecha",listanotif.get(position).getFecha());
                startActivity(intent);
            }
        });


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.notificacion, menu); //your file name
        menunotif = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.right_in, R.anim.right_out);
                break;
            case R.id.next:
                listanotif.clear();
                if(listaordenada.size()>c) {
                    for (int i = c; i < resultado; i++) {
                        if (i < listaordenada.size())
                            listanotif.add(listaordenada.get(i));
                        else
                            break;
                    }
                    if(listaordenada.size()>resultado)
                    {
                        menunotif.getItem(1).setVisible(true);
                    } else {
                        menunotif.getItem(1).setVisible(false);
                    }
                }
                else {
                    list.setVisibility(View.GONE);
                    noNotificaciones.setVisibility(View.VISIBLE);
                    menunotif.getItem(0).setVisible(false);
                }
                adaptador.notifyDataSetChanged();
                break;


        }
        return true;
    }

    class AdaptadorNotificacion extends ArrayAdapter<constnotificaciones> {

        AppCompatActivity appCompatActivity;

        AdaptadorNotificacion(AppCompatActivity context) {
            super(context, R.layout.item_notificacion, listanotif);
            appCompatActivity = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = appCompatActivity.getLayoutInflater();
            View item = inflater.inflate(R.layout.item_notificacion, null);

            TextView textView1 = (TextView)item.findViewById(R.id.notificacion);
            textView1.setText(listanotif.get(position).getTitulo());

            TextView textView2 = (TextView)item.findViewById(R.id.descrip);
            textView2.setText(listanotif.get(position).getDescripcion());

            if(listanotif.get(position).getVisto())
                item.setBackground(getResources().getDrawable(R.drawable.visto));

            return(item);
        }
    }

}
