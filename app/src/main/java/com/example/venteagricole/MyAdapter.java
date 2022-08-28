package com.example.venteagricole;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
   private static String URL_DELETE="https://karim2.000webhostapp.com/Delete_Produit.php";
    public static class  ViewHolder extends RecyclerView.ViewHolder{

        TextView namep, quentity, prix ;
        ImageView menu;
        ImageView img_produit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_produit = itemView.findViewById(R.id.img_produit);
           namep = itemView.findViewById(R.id.txt_namep);
           quentity = itemView.findViewById(R.id.txt_quentity);
           prix = itemView.findViewById(R.id.txt_prix);
            menu = itemView.findViewById(R.id.mMenus);
        }
    }
    private Context context;
    private List<Produit> posts;

    public  MyAdapter(Context c, List<Produit> postList){
        this.context = c;
        this.posts = postList;

    }


    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item1, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, final int position) {
        Produit p = posts.get(position);
       holder.namep.setText(p.getNamp());
      holder.quentity.setText(p.getQuentiity());
        holder.prix.setText(p.getPrix());
        Picasso.get().load(p.getImagep()).into(holder.img_produit);

        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context,view);
                popupMenu.getMenuInflater().inflate(R.menu.pop,popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.edit:
                                Intent intent=new Intent(context,Edit_produit.class);
                                intent.putExtra("position",position);
                               context.startActivity(intent);

                                break;
                            case R.id.delete:
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setMessage("Vous voulez supprimer le produit?")
                                        .setCancelable(false)
                                        .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                remove(position);
                                            }
                                        })

                                          .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                                              @Override
                                              public void onClick(DialogInterface dialog, int which) {
                                                  dialog.cancel();
                                              }
                                          });

                               AlertDialog alertDialog = builder.create();
                               alertDialog.show();

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
    public void remove(final int position){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DELETE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")){
                                Toast.makeText(context,"Produit Supprimer",Toast.LENGTH_SHORT).show();
                                MilieuFragment.produitArrayList.remove(position);
                                notifyDataSetChanged();

                            }else{

                                Toast.makeText(context,"Produit non Supprimer",Toast.LENGTH_SHORT).show();
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context,"Error!1  "+e.toString(),Toast.LENGTH_SHORT).show();

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,"Error!2  "+error.toString(),Toast.LENGTH_SHORT).show();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id",MilieuFragment.produitArrayList.get(position).getId());


                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }
}
