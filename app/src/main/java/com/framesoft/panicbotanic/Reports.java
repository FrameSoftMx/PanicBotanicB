package com.framesoft.panicbotanic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Reports extends AppCompatActivity {
    TextView para;
    EditText asunto,mensaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        Button enviar = (Button) findViewById(R.id.enviar);
        para = (TextView) findViewById(R.id.para);
        asunto = (EditText) findViewById(R.id.email_subject);
        mensaje = (EditText) findViewById(R.id.etmensaje);


        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reemplazamos el email por algun otro real
                String to = para.getText().toString();
                String subject = asunto.getText().toString();
                String message = mensaje.getText().toString();
               // String[] cc = { "otroEmail@ejemplo.com" };
                enviar(to, subject,message);
            }
        });

    }
    private void enviar(String to,
                        String subject, String message) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        //String[] to = direccionesEmail;
        //String[] cc = copias;
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
       // emailIntent.putExtra(Intent.EXTRA_CC, cc);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);
        emailIntent.setType("message/rfc822");
        startActivity(Intent.createChooser(emailIntent, "Email "));
    }


}
