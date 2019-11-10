package com.example.pushcartapp20;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;

public class CartRecyclerAdapter extends RecyclerView.Adapter<CartRecyclerAdapter.ViewHolder>{


    private ArrayList<CartItem> groceryItems;

    private Context context;

    public CartRecyclerAdapter(Context mContext, ArrayList<CartItem> mGroceryItems) {
        this.groceryItems = mGroceryItems;
        this.context = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list_item, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final CartItem currentItem = groceryItems.get(position);



        if(currentItem.getPromo()!=0){
            holder.minusSign.setVisibility(View.VISIBLE);
            holder.equalSign.setVisibility(View.VISIBLE);
            holder.productDiscount.setVisibility(View.VISIBLE);
            holder.priceLessDiscount.setVisibility(View.VISIBLE);

            holder.productDiscount.setTextColor(context.getResources().getColor(R.color.outOfStock));
            holder.priceLessDiscount.setTextColor(context.getResources().getColor(R.color.outOfStock));

            String productPromoStr = (int)(currentItem.getPromo()*100)+"%";
            holder.productDiscount.setText(productPromoStr);
            float price_less_promo = currentItem.getPrice() - (currentItem.getPrice()*currentItem.getPromo());

            NumberFormat formatter = NumberFormat.getNumberInstance();
            formatter.setMinimumFractionDigits(2);
            formatter.setMaximumFractionDigits(2);
            String output = formatter.format(price_less_promo);

            holder.priceLessDiscount.setText(output);

        }else if(currentItem.getDiscount()!=0){
            holder.minusSign.setVisibility(View.VISIBLE);
            holder.equalSign.setVisibility(View.VISIBLE);
            holder.productDiscount.setVisibility(View.VISIBLE);
            holder.priceLessDiscount.setVisibility(View.VISIBLE);

            holder.productDiscount.setTextColor(context.getResources().getColor(R.color.colorAccent));
            holder.priceLessDiscount.setTextColor(context.getResources().getColor(R.color.colorAccent));

            String productDiscountStr = (int)(currentItem.getDiscount()*100)+"%";
            holder.productDiscount.setText(productDiscountStr);
            float price_less_discount = currentItem.getPrice() - (currentItem.getPrice()*currentItem.getDiscount());

            NumberFormat formatter = NumberFormat.getNumberInstance();
            formatter.setMinimumFractionDigits(2);
            formatter.setMaximumFractionDigits(2);
            String output = formatter.format(price_less_discount);

            holder.priceLessDiscount.setText(output);
        }





        Bitmap bitmap = BitmapFactory.decodeByteArray(currentItem.getImageUrl(), 0, currentItem.getImageUrl().length);
        holder.image.setImageBitmap(bitmap);
        holder.prodName.setText(currentItem.getProductName());
        String quan = "Quantity: "+String.valueOf(currentItem.getQuantity());
        holder.quantity.setText(quan);
        String pric = "Price: "+String.valueOf(currentItem.getPrice());
        holder.prodPrice.setText(pric);
        holder.prodCategory.setText(currentItem.getCategory());

        if (currentItem.getDiscount()!=0){
            float tPrice = (currentItem.getPrice()-(currentItem.getPrice() * currentItem.getDiscount()))*currentItem.getQuantity();
            double roundOff = Math.round(tPrice * 100.0) / 100.0;

            String tPric = "Total: "+String.valueOf(roundOff);
            holder.totalPrice.setText(tPric);
        }else if (currentItem.getPromo()!=0){
            float tPrice = (currentItem.getPrice()-(currentItem.getPrice() * currentItem.getPromo()))*currentItem.getQuantity();
            double roundOff = Math.round(tPrice * 100.0) / 100.0;

            String tPric = "Total: "+String.valueOf(roundOff);
            holder.totalPrice.setText(tPric);
        }else{
            float tPrice = currentItem.getPrice()*currentItem.getQuantity();
            String tPric = "Total: "+String.valueOf(tPrice);
            holder.totalPrice.setText(tPric);
        }

        CartFragment.updateTotalPrice(String.valueOf(updateTprice()));
        holder.quantity_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "add", Toast.LENGTH_SHORT).show();
                groceryItems.get(position).setQuantity(groceryItems.get(position).getQuantity()+1);

                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(context);
                databaseAccess.open();
                databaseAccess.updateQuantityFromCartItem(currentItem.getProductId(),groceryItems.get(position).getQuantity());
                databaseAccess.close();

                CartFragment.updateTotalPrice(String.valueOf(updateTprice()));
                notifyDataSetChanged();
            }
        });
        holder.quantity_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "minus", Toast.LENGTH_SHORT).show();
                if (groceryItems.get(position).getQuantity()>1){
                    groceryItems.get(position).setQuantity(groceryItems.get(position).getQuantity()-1);

                    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(context);
                    databaseAccess.open();
                    databaseAccess.updateQuantityFromCartItem(currentItem.getProductId(),groceryItems.get(position).getQuantity());
                    databaseAccess.close();

                    CartFragment.updateTotalPrice(String.valueOf(updateTprice()));
                    notifyDataSetChanged();
                }
            }
        });
        holder.remove_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Do you want to remove this item?");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Toast.makeText(context, "removed! "+ currentItem.getProductId(), Toast.LENGTH_SHORT).show();
                        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(context);
                        databaseAccess.open();
                        databaseAccess.removeItemFromCart(currentItem.getProductId());
                        databaseAccess.close();
                        groceryItems.remove(position);

                        CartFragment.updateTotalPrice(String.valueOf(updateTprice()));

                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, getItemCount() - position);


                        //notifyDataSetChanged();


//                        if (groceryItems.size()<1){
//                            CartFragment.updateTotalPrice("P0.00");
//                        }

                    }
                });

                alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show();
                    }
                });
                alert.show();
            }
        });

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, ""+currentItem.getQuantity(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(context, "Discount: "+currentItem.getDiscount()+" ---- Promo: "+currentItem.getPromo(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void UpdateCart(){
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return groceryItems.size();
    }

    public float updateTprice(){
//        float totalPrice=0;
//        for (CartItem cartItem: groceryItems){
//            totalPrice += cartItem.getPrice()*cartItem.getQuantity();
//
//        }
//        return totalPrice;

        float totalPrice=0;
        for (CartItem cartItem: groceryItems){

            if(cartItem.getPromo()!=0){
                totalPrice += (cartItem.getPrice()-(cartItem.getPrice()*cartItem.getPromo()))  *cartItem.getQuantity();
            }else if(cartItem.getDiscount()!=0){
                totalPrice += (cartItem.getPrice()-(cartItem.getPrice()*cartItem.getDiscount()))  *cartItem.getQuantity();
            }else{
                totalPrice += cartItem.getPrice()*cartItem.getQuantity();
            }
        }

        double roundOff = Math.round(totalPrice * 100.0) / 100.0;

        return (float)roundOff;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView prodName, quantity, prodPrice, totalPrice, prodCategory;
        Button quantity_add, quantity_minus, remove_item;

        TextView minusSign, equalSign, productDiscount, priceLessDiscount;

        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.productImage);
            prodName = itemView.findViewById(R.id.productName);
            quantity = itemView.findViewById(R.id.product_Quantity);
            prodPrice = itemView.findViewById(R.id.product_Price);
            totalPrice = itemView.findViewById(R.id.productTotalPrice);
            prodCategory = itemView.findViewById(R.id.product_Category);
            parentLayout = itemView.findViewById(R.id.parent_layout2);
            quantity_add = itemView.findViewById(R.id.quantity_add);
            quantity_minus = itemView.findViewById(R.id.quantity_minus);
            remove_item = itemView.findViewById(R.id.remove_item);
            minusSign = itemView.findViewById(R.id.minusSign);
            equalSign = itemView.findViewById(R.id.equalSign);
            productDiscount = itemView.findViewById(R.id.productDiscount);
            priceLessDiscount = itemView.findViewById(R.id.priceLessDiscount);
        }
    }

}
