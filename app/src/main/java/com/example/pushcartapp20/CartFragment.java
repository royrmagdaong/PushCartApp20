package com.example.pushcartapp20;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartFragment extends Fragment {

    RecyclerView recyclerView;
    Button btnBuy;
    static TextView total_price;

    int counter=0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cart_fragment, container,false);

        recyclerView = view.findViewById(R.id.cartRecyclerView);
        btnBuy = view.findViewById(R.id.btnBuy);
        total_price = view.findViewById(R.id.total_price);

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Please Confirm");

                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(MainActivity.getContextOfApplication());
                        databaseAccess.open();
                        final ArrayList<CartItem> McartItems = databaseAccess.getCartItems();
                        databaseAccess.close();

                        counter = 0;

                        if (McartItems.size()>0){
                            for (final CartItem cartItem : McartItems){
                                counter++;
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_SENDTOCASHIER, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);

                                            if (counter==McartItems.size()){
                                                Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                            }

                                            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(MainActivity.getContextOfApplication());
                                            databaseAccess.open();
                                            databaseAccess.deleteCartItems();
                                            databaseAccess.close();

                                            initRecyclerView();
                                            total_price.setText("P0.00");


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getContext(), "Connection Problem", Toast.LENGTH_LONG).show();
                                    }
                                }){
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String,String> params = new HashMap<>();
                                        params.put("cart_no",String.valueOf(Constants.CART_NUMBER));
                                        params.put("prod_id",String.valueOf(cartItem.getProductId()));
                                        params.put("product_name",cartItem.getProductName());
                                        params.put("price",String.valueOf(cartItem.getPrice()));
                                        params.put("quan",String.valueOf(cartItem.getQuantity()));
                                        params.put("total_price",String.valueOf((float) cartItem.getQuantity() * cartItem.getPrice()));
                                        params.put("discount",String.valueOf(cartItem.getDiscount()));
                                        params.put("promo",String.valueOf(cartItem.getPromo()));
                                        return params;
                                    }
                                };

                                RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
                            }

                        }else{
                            Toast.makeText(getContext(), "There is no product in your Cart", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // what ever you want to do with No option.
                        Toast.makeText(getContext(), "CANCEL", Toast.LENGTH_SHORT).show();
                    }
                });
                alert.show();






            }
        });

        initRecyclerView();
        return view;
    }



    public void initRecyclerView(){

        ArrayList<CartItem> cartItems = new ArrayList<>();

        CartRecyclerAdapter cartRecyclerAdapter;

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(MainActivity.getContextOfApplication());
        databaseAccess.open();
        ArrayList<GroceryItem> data = databaseAccess.getData();
        ArrayList<CartItem> McartItems = databaseAccess.getCartItems();

        for (int x=0;x<McartItems.size();x++){
            for (int i = 0;i<data.size();i++){
                if (data.get(i).getProductName().equals(McartItems.get(x).getProductName())){
                    McartItems.get(x).setImageUrl(data.get(i).getImageUrl());
                }
            }
        }

        cartItems.addAll(McartItems);

        databaseAccess.close();

        cartRecyclerAdapter = new CartRecyclerAdapter(getContext(),cartItems);
        recyclerView.setAdapter(cartRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public static void updateTotalPrice(String tPrice){
        String s = "Total Price: "+tPrice;
        total_price.setText(s);
    }


}
