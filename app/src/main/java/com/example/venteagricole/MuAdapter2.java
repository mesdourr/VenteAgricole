package com.example.venteagricole;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MuAdapter2 extends RecyclerView.Adapter<MuAdapter2.ViewHolder> {
    public static class  ViewHolder extends RecyclerView.ViewHolder{

        TextView namep, quentity, prix, place, tel ;
        ImageView menu;
        ImageView img_produit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
           img_produit = itemView.findViewById(R.id.img_produit);
            namep = itemView.findViewById(R.id.txt_namep);
            quentity = itemView.findViewById(R.id.txt_quentity);
            prix = itemView.findViewById(R.id.txt_prix);
            place = itemView.findViewById(R.id.txt_place);
            tel = itemView.findViewById(R.id.txt_tel);
            menu = itemView.findViewById(R.id.mMenus);
        }
    }
    private Context context;
    private List<Produit> posts;

    public  MuAdapter2(Context c, List<Produit> postList){
        this.context = c;
        this.posts = postList;
    }


    @NonNull
    @Override
    public MuAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MuAdapter2.ViewHolder holder, final int position) {
        Produit p = posts.get(position);
        holder.namep.setText(p.getNamp());
        holder.quentity.setText(p.getQuentiity());
        holder.prix.setText(p.getPrix());
        holder.place.setText(p.getPlace());
        holder.tel.setText(p.getTel());
       Picasso.get().load(p.getImagep()).into(holder.img_produit);
        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context,view);
                popupMenu.getMenuInflater().inflate(R.menu.pop2,popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.allez:
                                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION )== PackageManager.PERMISSION_GRANTED){
                                    Double latitude = homeFragment.produitArrayList2.get(position).getLatitude();
                                    Double longitude = homeFragment.produitArrayList2.get(position).getLongitude();
                                    String destination = latitude+","+longitude;

                                    DisplayTrack(destination);
                                }else{
                                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
                                }


                                break;
                        }
                        return true;
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {

        return posts.size();
    }
    public void filterList(ArrayList<Produit> filteredList) {
        posts = filteredList;
        notifyDataSetChanged();
    }
    public void DisplayTrack(String destination){

        try {
            Uri uri = Uri.parse("https://www.google.co.in/maps/dir//"+destination);
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            intent.setPackage("com.google.android.apps.maps");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }catch (ActivityNotFoundException e){
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }



}
