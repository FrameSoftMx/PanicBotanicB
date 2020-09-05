
package com.framesoft.panicbotanic;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AgregarFavoritos extends AppCompatActivity {

    final private int REQUEST_CODE_ASK_PERMISSION = 124;
    int hasReadContactsPermission;

    private ArrayList<Contacto> listafavoritos;
    ListView lista;
    DatabaseReference mDatabase;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_favoritos);

        accessPermission();

        lista = (ListView)findViewById(R.id.listacontactos);
        if(listafavoritos.size()>0)
            Collections.sort(listafavoritos, new Comparator<Contacto>() {
                public int compare(Contacto obj1, Contacto obj2) {
                    return obj1.getNombre().compareTo(obj2.getNombre());
                }
            });
        AgregarFavoritos.AdaptadorFavoritos adaptador = new AgregarFavoritos.AdaptadorFavoritos(this);
        lista.setAdapter(adaptador);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseUser = firebaseAuth.getCurrentUser();
    }

    class AdaptadorFavoritos extends ArrayAdapter<Contacto> {

        AppCompatActivity appCompatActivity;

        AdaptadorFavoritos(AppCompatActivity context) {
            super(context, R.layout.item_contacto, listafavoritos);
            appCompatActivity = context;
        }
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = appCompatActivity.getLayoutInflater();
            View item = inflater.inflate(R.layout.item_contacto, null);

            TextView textView1 = (TextView)item.findViewById(R.id.nombre);
            textView1.setText(listafavoritos.get(position).getNombre());

            TextView textView2 = (TextView)item.findViewById(R.id.tel);
            textView2.setText(listafavoritos.get(position).getTel());

            final CheckBox checkBox = (CheckBox)item.findViewById(R.id.seleccion);


           checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(checkBox.isChecked()) {
                        listafavoritos.get(position).Checked(true);
                    }
                    else {
                        listafavoritos.get(position).Checked(false);
                    }
                }
            });

            return(item);

        }

    }
    private  void accessPermission(){
        hasReadContactsPermission = checkSelfPermission(Manifest.permission.READ_CONTACTS);
        if (hasReadContactsPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                    REQUEST_CODE_ASK_PERMISSION);
        }

    }

    public void onRequestPermissionsResult(int requestCode,String[] permissions,int [] grantResults){
        switch (requestCode){

            case REQUEST_CODE_ASK_PERMISSION:
                if (grantResults[0]== PackageManager.PERMISSION_GRANTED){

                    Toast.makeText(AgregarFavoritos.this, "Permisos de Contactos Aceptados", Toast.LENGTH_SHORT)
                            .show();
                }else{

                    Toast.makeText(AgregarFavoritos.this, "Permisos de Contactos Denegados", Toast.LENGTH_SHORT)
                            .show();
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions,grantResults);

        }
    }
}
