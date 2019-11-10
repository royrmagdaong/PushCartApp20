package com.example.pushcartapp20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QueueActivity extends AppCompatActivity {

    ListView listView;
    TextView cartNum;
    ArrayList<String> cartList;


    private ArrayAdapter<String> listAdapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.listViewQueue);
        cartNum = findViewById(R.id.cartNum);
        cartList = new ArrayList<>();

        String s = "You are Cart Number: "+Constants.CART_NUMBER;
        cartNum.setText(s);

        updateQueue();

    }

    public void updateQueue(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_GETQUEUE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);


                    //Toast.makeText(getBaseContext(), jsonObject.getJSONObject("cart_no").toString(), Toast.LENGTH_SHORT).show();
                    //Toast.makeText(QueueActivity.this, jsonObject.getString("cart_no"), Toast.LENGTH_SHORT).show();

                    int index = Integer.parseInt(jsonObject.getString("cart_no").substring(0,jsonObject.getString("cart_no").indexOf(',')));
                    int i = jsonObject.getString("cart_no").indexOf(',');
                    for (int x=0;x<index;x++){
                        int index1 = jsonObject.getString("cart_no").indexOf(',',i+1);
                        String s = jsonObject.getString("cart_no").substring(i+1,index1);
                        i = index1;
                        cartList.add("("+(x+1)+")    Cart number: "+s);
                        //Toast.makeText(QueueActivity.this, ""+s, Toast.LENGTH_SHORT).show();
                    }

                    listAdapter = new ArrayAdapter<>(getBaseContext(),R.layout.simple_list_view, cartList);
                    listView.setAdapter(listAdapter);

                   // Toast.makeText(QueueActivity.this, ""+index, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(QueueActivity.this, "ow Here", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), "Connection Problem", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();

                return params;
            }
        };

        RequestHandler.getInstance(getBaseContext()).addToRequestQueue(stringRequest);

    }
}
