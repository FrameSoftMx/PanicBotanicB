package com.framesoft.panicbotanic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

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
import android.widget.SearchView;
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

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static java.security.AccessController.getContext;


public class Favoritos extends AppCompatActivity {
    ListView favoritos;
    LinearLayout noFavoritos;
    private ArrayList<constfavorito> listafavoritos;
    private ArrayList<constfavorito> listacopia;
    private FirebaseAuth firebaseAuth;
    DatabaseReference mDatabase;
    String numero;
    int ultimo;
    Boolean acceso;
    Favoritos.AdaptadorFav adaptador;
    String userID;
    private final int NUMERO = 4;
    TextView noResultados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);


            firebaseAuth = FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance().getReference();
            userID = firebaseAuth.getCurrentUser().getUid();
            favoritos = (ListView) findViewById(R.id.listafavoritos);
            noFavoritos = (LinearLayout) findViewById(R.id.nofavoritos);
            noResultados = (TextView) findViewById(R.id.nofavsText);
            listafavoritos=new ArrayList<constfavorito>();
            listacopia=new ArrayList<constfavorito>();

            adaptador = new Favoritos.AdaptadorFav(Favoritos.this);

            favoritos.setAdapter(adaptador);
            Query query =mDatabase.child("favorito").child(userID);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for(DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                            try {
                                constfavorito numeros = userSnapshot.getValue(constfavorito.class);
                                listafavoritos.add(numeros);
                            }catch (Exception ex){
                                Toast.makeText(Favoritos.this, "Error " + ex.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        listacopia.addAll(listafavoritos);
                    }
                    if(listafavoritos.size()>0) {
                        Collections.sort(listafavoritos, new Comparator<constfavorito>() {
                            public int compare(constfavorito obj1, constfavorito obj2) {
                                return obj1.getNombre().compareTo(obj2.getNombre());
                            }
                        });
                        favoritos.setVisibility(View.VISIBLE);
                        noFavoritos.setVisibility(View.GONE);
                    }else{
                        favoritos.setVisibility(View.GONE);
                        noFavoritos.setVisibility(View.VISIBLE);
                    }
                    adaptador.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            Query permiso =mDatabase.child("privacidad").child(userID);
            permiso.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for(DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                            try {
                                // String cel = userSnapshot.getValue(String.class);
                                String clave = userSnapshot.getKey();
                                switch (clave){
                                    case "acontacto":
                                        acceso = userSnapshot.getValue(Boolean.class);
                                        break;
                                }
                            }catch (Exception ex){
                                Toast.makeText(Favoritos.this, "Error " + ex.getMessage().toString(), Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            favoritos.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                  /*  if(getActivity().getIntent().getExtras() != null) {
                        numero = listafavoritos.get(position).getTelefono();
                        Intent i = getActivity().getIntent();
                        i.putExtra("NUMERO", numero);
                        i.putExtra("FORMA", "Favorito");
                        getActivity().setResult(RESULT_OK, i);
                        getActivity().finish();
                    }*/
                }
            });

            //return vista;
        }

        class AdaptadorFav extends ArrayAdapter<constfavorito> {

            AppCompatActivity appCompatActivity;

            AdaptadorFav(AppCompatActivity context) {
                super(context, R.layout.item_favorito, listafavoritos);
                appCompatActivity = context;
            }

            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = appCompatActivity.getLayoutInflater();
                View item = inflater.inflate(R.layout.item_favorito, null);

                TextView textView1 = (TextView)item.findViewById(R.id.nombrefav);
                textView1.setText(listafavoritos.get(position).getNombre());

                TextView textView2 = (TextView)item.findViewById(R.id.telfav);
                textView2.setText(listafavoritos.get(position).getTelefono());

                return(item);
            }

            public void notifyDataSetChanged() {
            }
        }





        @Override
        public boolean onOptionsItemSelected(MenuItem item) {

            /*switch (item.getItemId()) {
                case R.id.agregar:
                    if(acceso) {
                        Intent intent = new Intent(getContext(), AgregarFavoritos.class);
                        startActivityForResult(intent,NUMERO);
                    }
                    else {
                        Toast.makeText(Favoritos.this, "Debe permitir el acceso a los contactos en Cuenta->Privacidad", Toast.LENGTH_SHORT)
                                .show();
                    }
                    break;
            }*/
            return  true;
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            // Comprobamos si el resultado de la segunda actividad es "RESULT_CANCELED".
            if (resultCode != RESULT_CANCELED) {
                Query query =mDatabase.child("favorito").child(userID);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for(DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                                try {
                                    constfavorito numeros = userSnapshot.getValue(constfavorito.class);
                                    listafavoritos.add(numeros);
                                }catch (Exception ex){
                                    Toast.makeText(Favoritos.this, "Error " + ex.getMessage().toString(), Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }
                        }
                        if(listafavoritos.size()>0) {
                            Collections.sort(listafavoritos, new Comparator<constfavorito>() {
                                public int compare(constfavorito obj1, constfavorito obj2) {
                                    return obj1.getNombre().compareTo(obj2.getNombre());
                                }
                            });
                            favoritos.setVisibility(View.VISIBLE);
                            noFavoritos.setVisibility(View.GONE);
                        }else{
                            favoritos.setVisibility(View.GONE);
                            noFavoritos.setVisibility(View.VISIBLE);
                        }
                        adaptador.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        }


    }
}
