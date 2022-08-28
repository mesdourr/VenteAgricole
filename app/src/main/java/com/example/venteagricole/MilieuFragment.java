package com.example.venteagricole;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class MilieuFragment extends Fragment {
    MainActivity mn;

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    public static ArrayList<Produit> produitArrayList = new ArrayList<>();
    public static String URL_UP="https://karim2.000webhostapp.com/upload_produit.php";
    Produit produit;

    public MilieuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_milieu, container, false);
        mn = (MainActivity) getActivity();

        recyclerView = view.findViewById(R.id.rv);
        adapter = new MyAdapter(mn,produitArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(mn));
        recyclerView.setAdapter(adapter);

        uploadData();




        FloatingActionButton floatingActionButton;
        floatingActionButton = view.findViewById(R.id.add);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mn,Data_Produit.class);
                startActivity(intent);
            }
        });
        return view;
    }

public void uploadData (){
    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UP,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    produitArrayList.clear();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if (success.equals("1")){
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                String id = object.getString("id").trim();
                                String namep = object.getString("namep").trim();
                                String imagep = object.getString("imagep").trim();
                                String quentity = object.getString("quentity").trim();
                                String prix = object.getString("prix").trim();
                                String place = object.getString("place").trim();

                             Produit produit = new Produit(id, namep,imagep, quentity, prix, "tel", place, "iduser", 1.1, 1.1);
                              produitArrayList.add(produit);
                             adapter.notifyDataSetChanged();


                              }
                            MainActivity.cst=0;
                        }
                  } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(mn,"Eroor1"+e.toString(),Toast.LENGTH_SHORT).show();

                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(mn,"Eroor2"+error.toString(),Toast.LENGTH_SHORT).show();

                }
            })
    { @Override
        protected Map<String, String> getParams() throws AuthFailureError {
        Map<String,String> params = new HashMap<>();
        params.put("iduser",MainActivity.extraId);

        return params;
    }
    };


    RequestQueue requestQueue = Volley.newRequestQueue(mn);
    requestQueue.add(stringRequest);

}

}
