package com.framesoft.panicbotanic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

public class Pagos extends AppCompatActivity {

    private static  final  int SCAN_RESULT= 100;
    private TextView tvTarjeta;
    EditText numero,nombre,date,etcvv;
    TextView tvnumero,tvdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagos);

        init();


    }

    private void init() {

       nombre = (EditText) findViewById(R.id.nombre);
       numero = (EditText) findViewById(R.id.numero);
       date = (EditText) findViewById(R.id.expiracion);
       etcvv = (EditText) findViewById(R.id.cvv);
       tvnumero = (TextView) findViewById(R.id.tvnumero);
       tvdate = (TextView) findViewById(R.id.tvexpiracion);
    }

    public void scanearTarjeta(View view){
        Intent intent = new Intent(this, CardIOActivity.class)
                .putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY,true)
                .putExtra(CardIOActivity.EXTRA_REQUIRE_CVV,true)
                .putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE,false);
        startActivityForResult(intent,SCAN_RESULT);
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == SCAN_RESULT){
            if (data !=null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)){

                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
                numero.setText(scanResult.getRedactedCardNumber());
                tvnumero.setText(scanResult.getRedactedCardNumber());



                if (scanResult.isExpiryValid()){
                    String mes = String.valueOf(scanResult.expiryMonth);
                    String an = String.valueOf(scanResult.expiryYear);
                    date.setText(mes + "/" + an);
                    tvdate.setText(mes + "/" + an);

                }
                if (scanResult.isExpiryValid()){
                    String cvv = String.valueOf(scanResult.cvv);
                    etcvv.setText(cvv);
                }

            }

        }

    }
}
