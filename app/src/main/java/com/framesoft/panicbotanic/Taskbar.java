package com.framesoft.panicbotanic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class Taskbar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskbar);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_taskbar,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_perfil:
                //this.names.add("Nuevo " + (++counter));
                //this.myAdapter.notifyDataSetChanged();
                Intent intent = new Intent(getApplicationContext(),Profile.class);
                startActivity(intent);
                break;
            case R.id.action_configurar:
                //this.names.add("Nuevo " + (++counter));
                //this.myAdapter.notifyDataSetChanged();
                Intent intentdos = new Intent(getApplicationContext(),Settings.class);
                startActivity(intentdos);
                break;
            default:
                return super.onOptionsItemSelected(item);

        }
        return false;
    }

}
