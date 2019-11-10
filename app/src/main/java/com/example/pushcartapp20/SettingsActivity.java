package com.example.pushcartapp20;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    Button btnCartNumber, btnIPAddress, btnOfflineMode;
    EditText cartNumber, IPAddress;
    TextView txtOfflineMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnCartNumber = findViewById(R.id.btnCartNumber);
        btnIPAddress = findViewById(R.id.btnIPAddress);
        cartNumber = findViewById(R.id.cartNumber);
        IPAddress = findViewById(R.id.IPAddress);
        btnOfflineMode = findViewById(R.id.btnOfflineMode);
        txtOfflineMode = findViewById(R.id.txtOfflineMode);

        cartNumber.setText(String.valueOf(Constants.CART_NUMBER));
        IPAddress.setText(Constants.IPADDRESS);

        if (Constants.OFFLINE_MODE){
            txtOfflineMode.setTextColor(getResources().getColor(R.color.On));
            btnOfflineMode.setText("OFF");
        }


        btnCartNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cartNumber.isEnabled()){
                    Constants.CART_NUMBER = Integer.parseInt(cartNumber.getText().toString());
                    cartNumber.setEnabled(false);
                    btnCartNumber.setText("EDIT");
                }else{
                    cartNumber.setEnabled(true);
                    btnCartNumber.setText("SAVE");
                }

            }
        });

        btnIPAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (IPAddress.isEnabled()){
                    Constants.IPADDRESS = IPAddress.getText().toString();

                    Constants.ROOT_URL = "http://"+Constants.IPADDRESS+"/pushcart/v1/";
                    Constants.URL_SENDTOCASHIER = Constants.ROOT_URL + "sendToCashier.php";
                    Constants.URL_LOGIN = Constants.ROOT_URL+ "userLogin.php";

                    IPAddress.setEnabled(false);
                    btnIPAddress.setText("EDIT");
                }else{
                    IPAddress.setEnabled(true);
                    btnIPAddress.setText("SAVE");
                }

            }
        });

        btnOfflineMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnOfflineMode.getText().toString().equals("ON")){
                    btnOfflineMode.setText("OFF");
                    txtOfflineMode.setTextColor(getResources().getColor(R.color.On));
                    Constants.OFFLINE_MODE = true;
                }else{
                    btnOfflineMode.setText("ON");
                    txtOfflineMode.setTextColor(getResources().getColor(R.color.Off));
                    Constants.OFFLINE_MODE = false;
                }
            }
        });
    }


}
