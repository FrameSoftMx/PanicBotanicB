package com.framesoft.panicbotanic;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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

import static java.security.AccessController.getContext;

public class Contacs extends AppCompatActivity {

    ListView numeros;
    LinearLayout noContactos;
    private ArrayList<constfavorito> listanumeros;
    private ArrayList<constfavorito> listacopia;
    TextView noResultados;
    DatabaseReference mDatabase;
    FirebaseAuth firebaseAuth;
    SharedPreferences Configuracion;
    String numero;
    Contacs.Adaptador adaptador;
    Boolean acceso;
    String userID;
    final private int REQUEST_CODE_ASK_PERMISSION = 124;
    int hasReadContactsPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacs);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

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
                            Toast.makeText(Contacs.this, "Error " + ex.getMessage().toString(), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        numeros = (ListView) findViewById(R.id.listanumeros);
        noContactos = (LinearLayout) findViewById(R.id.nocontactos);
        noResultados = (TextView) findViewById(R.id.noMessagesText);
        listanumeros=new ArrayList<constfavorito>();
        listacopia=new ArrayList<constfavorito>();



        adaptador = new Contacs.Adaptador((Contacs.this));
        Query query =mDatabase.child("contacto").child(userID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for(DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                        try {
                            constfavorito numeros = userSnapshot.getValue(constfavorito.class);
                            listanumeros.add(numeros);
                        }catch (Exception ex){
                            Toast.makeText(Contacs.this, "Error " + ex.getMessage().toString(), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                    listacopia.addAll(listanumeros);
                }
                if(listanumeros.size()>0) {
                    Collections.sort(listanumeros, new Comparator<constfavorito>() {
                        public int compare(constfavorito obj1, constfavorito obj2) {
                            return obj1.getNombre().compareTo(obj2.getNombre());
                        }
                    });
                    numeros.setVisibility(View.VISIBLE);
                    noContactos.setVisibility(View.GONE);
                }else{
                    numeros.setVisibility(View.GONE);
                    noContactos.setVisibility(View.VISIBLE);
                }
                adaptador.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        numeros.setAdapter(adaptador);

        numeros.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if(Contacs.this.getIntent().getExtras() != null) {
                    numero = listanumeros.get(position).getTelefono();
                    Intent i = Contacs.this.getIntent();
                    i.putExtra("NUMERO", numero);
                    i.putExtra("FORMA", "Favorito");
                    Contacs.this.setResult(RESULT_OK, i);
                    Contacs.this.finish();
                }
            }
        });

        int permisocontactos = ContextCompat.checkSelfPermission(Contacs.this, Manifest.permission.READ_CONTACTS);

        if (permisocontactos != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Contacs.this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_ASK_PERMISSION);
        }
    }
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.importar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.importar:
                if(acceso) {
                    new AlertDialog.Builder(Contacs.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Importar contactos")
                            .setMessage("¿Desea importar todos sus contactos?")
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        Toast.makeText(Contacs.this, "Cargando lista de contactos...", Toast.LENGTH_SHORT)
                                                .show();
                                        SubirContactos subir = new SubirContactos();
                                        subir.execute();
                                    }catch (Exception exep){
                                        Toast.makeText(Contacs.this, "Ha ocurrido un error al importar los contactos.", Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                    finally {
                                        recargarconactos();
                                    }
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }else {
                    Toast.makeText(Contacs.this, "Debe permitir el acceso a los contactos en Cuenta->Privacidad", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    class Adaptador extends ArrayAdapter<constfavorito> {

        AppCompatActivity appCompatActivity;

        Adaptador(AppCompatActivity context) {
            super(context, R.layout.item_favorito, listanumeros);
            appCompatActivity = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = appCompatActivity.getLayoutInflater();
            View item = inflater.inflate(R.layout.item_favorito, null);

            TextView textView1 = (TextView)item.findViewById(R.id.nombrefav);
            textView1.setText(listanumeros.get(position).getNombre());

            TextView textView2 = (TextView)item.findViewById(R.id.telfav);
            textView2.setText(listanumeros.get(position).getTelefono());

            ImageView imageView1 = (ImageView)item.findViewById(R.id.iperfil);
            imageView1.setImageResource(R.drawable.amigo);

            return(item);
        }
    }
    public void recargarconactos(){
        Query query =mDatabase.child("contacto").child(userID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for(DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                        try {
                            constfavorito numeros = userSnapshot.getValue(constfavorito.class);
                            listanumeros.add(numeros);
                        }catch (Exception ex){
                            Toast.makeText(Contacs.this, "Error " + ex.getMessage().toString(), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                }
                if(listanumeros.size()>0) {
                    Collections.sort(listanumeros, new Comparator<constfavorito>() {
                        public int compare(constfavorito obj1, constfavorito obj2) {
                            return obj1.getNombre().compareTo(obj2.getNombre());
                        }
                    });
                    numeros.setVisibility(View.VISIBLE);
                    noContactos.setVisibility(View.GONE);
                }else{
                    numeros.setVisibility(View.GONE);
                    noContactos.setVisibility(View.VISIBLE);
                }
                adaptador.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private class SubirContactos extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            try{
                constfavorito user;
                Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                ContentResolver cr = Contacs.this.getContentResolver();
                Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                        null, null, null, null);
                String[] projection = new String[]{
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER};
                Cursor names = Contacs.this.getContentResolver().query(
                        uri, projection, null, null, null);
                int indexName = names.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                int indexNumber = names.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.NUMBER);
                names.moveToFirst();
                do {
                    user = new constfavorito(names.getString(indexName), names.getString(indexNumber));
                    mDatabase.child("contacto").child(userID).child(names.getString(indexName)).setValue(user);
                } while (names.moveToNext());

                return true;
            }
            catch (Exception ex){
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            try {
                if(result){
                    Toast.makeText(Contacs.this, "Lista de contactos agregada correctamente.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(Contacs.this, "Error al cargar contactos", Toast.LENGTH_LONG).show();
                }
            } catch (Exception ex) {
                //Toast.makeText(getApplicationContext(), "Message Failed, Unknown error. " + ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }



}
