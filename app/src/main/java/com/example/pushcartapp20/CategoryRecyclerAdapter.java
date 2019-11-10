package com.example.pushcartapp20;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.ViewHolder>{
    private static final String TAG = "CategoryRecyclerAdapter";

    private ArrayList<GroceryItem> groceryItems;

    private Context context;

    float discount = 0;
    float price_less_discount = 0;

    float promo = 0;
    float price_less_promo = 0;


    public CategoryRecyclerAdapter(Context mContext, ArrayList<GroceryItem> mGroceryItems) {
        this.groceryItems = mGroceryItems;
        this.context = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_item, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        final GroceryItem currentItem = groceryItems.get(position);



        if (Constants.SENIOR_PWD){
            if (currentItem.getDiscount()== 0){
                holder.priceLessDiscount.setVisibility(View.INVISIBLE);
                holder.equalSign.setVisibility(View.INVISIBLE);
                holder.minusSign.setVisibility(View.INVISIBLE);
                holder.discount.setVisibility(View.INVISIBLE);
            }else{
                holder.priceLessDiscount.setVisibility(View.VISIBLE);
                holder.equalSign.setVisibility(View.VISIBLE);
                holder.minusSign.setVisibility(View.VISIBLE);
                holder.discount.setVisibility(View.VISIBLE);


                discount = currentItem.getDiscount();
                price_less_discount = currentItem.getPrice() - (discount * currentItem.getPrice());

                String discountStr = String.valueOf((int)(discount * 100)) + "%";
                holder.discount.setText(discountStr);

                NumberFormat formatter = NumberFormat.getNumberInstance();
                formatter.setMinimumFractionDigits(2);
                formatter.setMaximumFractionDigits(2);
                String output = formatter.format(price_less_discount);

                holder.priceLessDiscount.setText(output);


            }
        }else{
            if (currentItem.getPromo() == 0){
                holder.priceLessDiscount.setVisibility(View.INVISIBLE);
                holder.equalSign.setVisibility(View.INVISIBLE);
                holder.minusSign.setVisibility(View.INVISIBLE);
                holder.discount.setVisibility(View.INVISIBLE);
            }else{
                holder.priceLessDiscount.setVisibility(View.VISIBLE);
                holder.equalSign.setVisibility(View.VISIBLE);
                holder.minusSign.setVisibility(View.VISIBLE);
                holder.discount.setVisibility(View.VISIBLE);

                // text font turn red if there is a promo
                holder.priceLessDiscount.setTextColor(context.getResources().getColor(R.color.outOfStock));
                holder.discount.setTextColor(context.getResources().getColor(R.color.outOfStock));

                promo = currentItem.getPromo();
                price_less_promo = currentItem.getPrice() - (promo * currentItem.getPrice());

                String promoStr = String.valueOf((int)(promo * 100)) + "%";
                holder.discount.setText(promoStr);

                NumberFormat formatter = NumberFormat.getNumberInstance();
                formatter.setMinimumFractionDigits(2);
                formatter.setMaximumFractionDigits(2);
                String output = formatter.format(price_less_promo);

                holder.priceLessDiscount.setText(output);
            }
        }





        Bitmap bitmap = BitmapFactory.decodeByteArray(currentItem.getImageUrl(), 0, currentItem.getImageUrl().length);
        holder.image.setImageBitmap(bitmap);
        holder.prodName.setText(currentItem.getProductName());
        String strStock = "Stock: "+currentItem.getStock();
        holder.stock.setText(strStock);
        String strPrice = "Price: "+currentItem.getPrice();
        holder.prodPrice.setText(strPrice);
        holder.prodCategory.setText(currentItem.getCategory());


        if (currentItem.getStock() == 0){
            holder.prodName.setTextColor(context.getResources().getColor(R.color.outOfStock));


            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "This Product is out of stock!", Toast.LENGTH_SHORT).show();
                }
            });
        }else if(Constants.SENIOR_PWD && discount!=0){
            holder.prodName.setTextColor(context.getResources().getColor(R.color.Black));


            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick: Clicked on: "+currentItem.getProductName());

                    HomeFragment.updateUi(currentItem.getProductId(),currentItem.getProductName(),currentItem.getCategory(),currentItem.getPrice(),
                            currentItem.getStock(), currentItem.getImageUrl(), currentItem.getDiscount(), true);


                    HomeFragment.dialog.dismiss();
                }
            });
        }else if(!Constants.SENIOR_PWD && promo!=0){
            holder.prodName.setTextColor(context.getResources().getColor(R.color.Black));


            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick: Clicked on: "+currentItem.getProductName());

                    HomeFragment.updateUi(currentItem.getProductId(),currentItem.getProductName(),currentItem.getCategory(),currentItem.getPrice(),
                            currentItem.getStock(), currentItem.getImageUrl(), currentItem.getPromo(), false);


                    HomeFragment.dialog.dismiss();
                }
            });
        } else{
            holder.prodName.setTextColor(context.getResources().getColor(R.color.Black));


            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick: Clicked on: "+currentItem.getProductName());

                    HomeFragment.updateUi(currentItem.getProductId(),currentItem.getProductName(),currentItem.getCategory(),currentItem.getPrice(),
                            currentItem.getStock(), currentItem.getImageUrl(),0, false);


                    HomeFragment.dialog.dismiss();
                    //Toast.makeText(context, currentItem.getProductName(), Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return groceryItems.size();
    }

    public void filterList(ArrayList<GroceryItem> filteredList){
        groceryItems = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView prodName, stock, prodPrice, prodCategory;

        TextView discount, minusSign, equalSign, priceLessDiscount;

        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.productImage);
            prodName = itemView.findViewById(R.id.productName);
            stock = itemView.findViewById(R.id.productStock);
            prodPrice = itemView.findViewById(R.id.productPrice);
            prodCategory = itemView.findViewById(R.id.productCategory);
            parentLayout = itemView.findViewById(R.id.parent_layout);

            discount = itemView.findViewById(R.id.productDiscount);
            minusSign = itemView.findViewById(R.id.minusSign);
            equalSign = itemView.findViewById(R.id.equalSign);
            priceLessDiscount = itemView.findViewById(R.id.priceLessDiscount);
        }
    }
}
