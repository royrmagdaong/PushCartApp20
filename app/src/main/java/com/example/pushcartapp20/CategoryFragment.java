package com.example.pushcartapp20;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CategoryFragment extends Fragment {

    private static final String TAG = "CategoryFragment";

    //vars
    private ArrayList<GroceryItem> groceryItems = new ArrayList<>();
    private ArrayList<GroceryItem> filteredList;

    private CategoryRecyclerAdapter categoryRecyclerAdapter;

    private RecyclerView recyclerView;
    private Spinner spinner;
    private EditText searchEditTxt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_fragment, container,false);

        recyclerView = view.findViewById(R.id.recyclerView);
        searchEditTxt = view.findViewById(R.id.editSearch);
        spinner = view.findViewById(R.id.categorySpinner);

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
        Log.d(TAG, "onCreateView: Started!");
        initImageBitmaps();
        return view;
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

    private void initImageBitmaps(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(MainActivity.getContextOfApplication());
        databaseAccess.open();
        ArrayList<GroceryItem> data = databaseAccess.getData();

        groceryItems.addAll(data);
        databaseAccess.close();
        initRecyclerView();
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerView.");
        categoryRecyclerAdapter = new CategoryRecyclerAdapter(getContext(),groceryItems);
        recyclerView.setAdapter(categoryRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}
