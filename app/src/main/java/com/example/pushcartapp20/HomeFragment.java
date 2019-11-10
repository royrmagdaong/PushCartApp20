package com.example.pushcartapp20;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;


public class HomeFragment extends Fragment {

    static ImageView prodImage, floorPlanImage;
    Button searchButton, addToCartBtn;
    CheckBox checkBox_SNRPWD;
    TextView SENIORPWD;



    static TextView prodName, prodPrice, prodStock, prodCategory,equalSign, minusSign, txtDiscount, priceLessDiscount;

    //vars
    public static Dialog dialog;
    boolean loadData = false;

    private ArrayList<GroceryItem> groceryItems = new ArrayList<>();
    private ArrayList<GroceryItem> filteredList;
    private ArrayList<CartItem> cartItems;


    private CategoryRecyclerAdapter categoryRecyclerAdapter;

    private RecyclerView recyclerView;
    private Spinner spinner;
    private EditText searchEditTxt;

    private static int mprodId;
    private static String mname;
    private static String mcategory;
    private static float mprice;
    private static byte[] mimageUrl;

    private static float mdiscount=0, mpromo=0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.home_fragment, container,false);

       floorPlanImage = view.findViewById(R.id.floorPlanImage);
       prodImage = view.findViewById(R.id.prodImage);
       searchButton = view.findViewById(R.id.searchButton);
       prodName = view.findViewById(R.id.productName);
       prodPrice = view.findViewById(R.id.productPrice);
       prodStock = view.findViewById(R.id.productStock);
       prodCategory = view.findViewById(R.id.productCategory);
       addToCartBtn = view.findViewById(R.id.addToCartBtn);
       SENIORPWD = view.findViewById(R.id.SENIORPWD);
       checkBox_SNRPWD = view.findViewById(R.id.checkbox_SNRPWD);
       minusSign = view.findViewById(R.id.minusSign);
       equalSign = view.findViewById(R.id.equalSign);
       txtDiscount = view.findViewById(R.id.productDiscount);
       priceLessDiscount = view.findViewById(R.id.priceLessDiscount);


        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.category_fragment);
        dialog.setTitle("Grocery Items");

        recyclerView = dialog.findViewById(R.id.recyclerView);
        spinner = dialog.findViewById(R.id.categorySpinner);
        searchEditTxt = dialog.findViewById(R.id.editSearch);


        if (Constants.SENIOR_PWD){
            checkBox_SNRPWD.setChecked(true);
        }


        prodImage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               //showImage();
           }
       });

        searchButton();
       addToCart();
       showMap();
       pwd_senior_check();
       return view;
    }

    private void pwd_senior_check(){
        checkBox_SNRPWD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (isChecked){
                    Constants.SENIOR_PWD = true;
                }else{
                    Constants.SENIOR_PWD = false;
                }
            }
            }
        );
    }

    private void searchButton(){
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                initImageBitmaps();
                if (Constants.OFFLINE_MODE){
                    initSpinner();
                }


            }
        });
    }

    private void initSpinner(){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                searchEditTxt.setText("");
                filter(arg0.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        searchEditTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //filter(editable.toString());
                if (!editable.toString().isEmpty()){
                    searchFilter(editable.toString());
                }
            }
        });

        dialog.show();
    }

    private void filter(String text){
        filteredList = new ArrayList<>();

        if (text.equals("All")){
            filteredList.addAll(groceryItems);
            categoryRecyclerAdapter.filterList(filteredList);
        }else{
            for (GroceryItem item: groceryItems){
                if (item.getCategory().toLowerCase().contains(text.toLowerCase())){
                    filteredList.add(item);
                }
            }
            categoryRecyclerAdapter.filterList(filteredList);
        }
    }

    private void searchFilter(String text){
        ArrayList<GroceryItem> filteredSearchList = new ArrayList<>();

        for (GroceryItem item: filteredList){
            if (item.getProductName().toLowerCase().contains(text.toLowerCase())){
                filteredSearchList.add(item);
            }
        }
        categoryRecyclerAdapter.filterList(filteredSearchList);
    }


    // map image dialog
    public void showMap() {
        floorPlanImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog builder = new Dialog(getContext());
                builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                builder.getWindow().setBackgroundDrawable(
                        new ColorDrawable(android.graphics.Color.TRANSPARENT));
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        //nothing;
                    }
                });
                ImageView imageView = new ImageView(getContext());
                imageView.setImageResource(R.drawable.sections);
                builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                builder.show();
            }
        });
    }

    private void initImageBitmaps(){
        groceryItems.clear();
        loadData=true;
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(MainActivity.getContextOfApplication());
        databaseAccess.open();
        ArrayList<GroceryItem> data = databaseAccess.getData();

        groceryItems.addAll(data);
        databaseAccess.close();

        initRecyclerView();
    }

    private void initRecyclerView(){
        if (Constants.OFFLINE_MODE){
            categoryRecyclerAdapter = new CategoryRecyclerAdapter(getContext(),groceryItems);
            recyclerView.setAdapter(categoryRecyclerAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }else{
            StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_GETALLPRODUCTS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                //converting the string to json array object
                                JSONArray array = new JSONArray(response);

                                //Toast.makeText(getContext(), ""+array.toString(), Toast.LENGTH_SHORT).show();

                                //traversing through all the object
                                for (int i = 0; i < array.length(); i++) {

                                    //getting product object from json array
                                    JSONObject product = array.getJSONObject(i);

                                    groceryItems.get(i).setPrice(Float.parseFloat(product.getString("product_price")));
                                    groceryItems.get(i).setStock((product.getInt("product_stock")));
                                    groceryItems.get(i).setDiscount(Float.parseFloat(product.getString("discount")));
                                    groceryItems.get(i).setPromo(Float.parseFloat(product.getString("promo")));

                                }

                                // update ui if request from server is success
                                categoryRecyclerAdapter = new CategoryRecyclerAdapter(getContext(),groceryItems);
                                recyclerView.setAdapter(categoryRecyclerAdapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                                //init spinner in online mode
                                initSpinner();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });

            //adding our stringrequest to queue
            Volley.newRequestQueue(getContext()).add(stringRequest);
        }

    }

    public static void updateUi(int prodId,String name, String category, float price, int stock, byte[] imageUrl, float discount, boolean isDiscount){

        mprodId = prodId;
        mname = name;
        mcategory = category;
        mprice = price;
        mimageUrl = imageUrl;

        if(discount!=0){
            txtDiscount.setVisibility(View.VISIBLE);
            minusSign.setVisibility(View.VISIBLE);
            equalSign.setVisibility(View.VISIBLE);
            priceLessDiscount.setVisibility(View.VISIBLE);

            String txtDiscountStr = (int)(discount * 100)+ "%";
            txtDiscount.setText(txtDiscountStr);

            float price_less_discount= 0;
            price_less_discount = mprice - (mprice * discount);

            NumberFormat formatter = NumberFormat.getNumberInstance();
            formatter.setMinimumFractionDigits(2);
            formatter.setMaximumFractionDigits(2);
            String output = formatter.format(price_less_discount);

            priceLessDiscount.setText(output);

            if (isDiscount){
                mdiscount = discount;
                mpromo = 0;
            }else{
                mpromo = discount;
                mdiscount = 0;
            }
        }else{

            mpromo = 0;
            mdiscount = 0;

            txtDiscount.setVisibility(View.INVISIBLE);
            minusSign.setVisibility(View.INVISIBLE);
            equalSign.setVisibility(View.INVISIBLE);
            priceLessDiscount.setVisibility(View.INVISIBLE);
        }


        Bitmap bitmap = BitmapFactory.decodeByteArray(imageUrl, 0, imageUrl.length);
        prodImage.setImageBitmap(bitmap);
        prodName.setText(name);
        String categ = "Category: "+ category;
        prodCategory.setText(categ);
        String mPrice = "Price: "+String.valueOf(price);
        prodPrice.setText(mPrice);
        String mStock = "Stock: "+String.valueOf(stock);
        prodStock.setText(mStock);

        switch (mcategory){
            case "All":
                break;
            case "Baby Needs":
                floorPlanImage.setImageResource(R.drawable.baby_needs_section);
                break;
            case "Beverages":
                floorPlanImage.setImageResource(R.drawable.beverages_sectiob);
                break;
            case "Canned/Packaged Foods":
                floorPlanImage.setImageResource(R.drawable.canned_packaged_section);
                break;
            case "Cereals":
                floorPlanImage.setImageResource(R.drawable.cereals_section);
                break;
            case "Chips":
                floorPlanImage.setImageResource(R.drawable.chips_section);
                break;
            case "Cleaners":
                floorPlanImage.setImageResource(R.drawable.cleaners_section);
                break;
            case "Dairy":
                floorPlanImage.setImageResource(R.drawable.dairy_section);
                break;
            case "Fish":
                floorPlanImage.setImageResource(R.drawable.fish_section);
                break;
            case "Fruits":
                floorPlanImage.setImageResource(R.drawable.fruits_section);
                break;
            case "Meat":
                floorPlanImage.setImageResource(R.drawable.meat_secion);
                break;
            case "Powdered Drink":
                floorPlanImage.setImageResource(R.drawable.powdered_drink_section);
                break;
            case "Seasonings":
                floorPlanImage.setImageResource(R.drawable.seasonings_section);
                break;
            case "Soap/Shampoo":
                floorPlanImage.setImageResource(R.drawable.soap_shampoo_section);
                break;
            case "Vegetables":
                floorPlanImage.setImageResource(R.drawable.vegetables_section);
                break;
            case "Others":
                floorPlanImage.setImageResource(R.drawable.others_section);
                break;
        }

    }

    public void addToCart(){
        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                final EditText edittext = new EditText(getContext());
                edittext.setInputType(InputType.TYPE_CLASS_NUMBER);
                alert.setTitle("Enter Quantity");

                alert.setView(edittext);

                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (!edittext.getText().toString().isEmpty()){
                            if (mname!=null){
                                int quantity = Integer.parseInt(edittext.getText().toString());

                                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getContext());
                                databaseAccess.open();
                                ArrayList<CartItem> cartItems = databaseAccess.getCartItems();
                                boolean duplicate=false;
                                for (CartItem cartItem : cartItems){
                                    if (cartItem.getProductId()==mprodId){
                                        duplicate=true;
                                        Toast.makeText(getContext(), "You already have this item in your cart", Toast.LENGTH_LONG).show();
                                    }
                                }
                                if (!duplicate){
                                    Toast.makeText(getContext(), "Added to cart", Toast.LENGTH_SHORT).show();
                                    databaseAccess.insertCartItems(mprodId,mname,String.valueOf(mprice),quantity,mcategory,mdiscount,mpromo);
                                }
                                databaseAccess.close();
                            }
                        }else{
                            Toast.makeText(getContext(), "NULL", Toast.LENGTH_SHORT).show();
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
    }

}
