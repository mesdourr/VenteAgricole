package com.example.venteagricole;


import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class homeFragment extends Fragment {
    MainActivity mn2;
    private RecyclerView recyclerView2;
    private MuAdapter2 adapter2;
    public static ArrayList<Produit> produitArrayList2 = new ArrayList<>();
    public static String URL_UP2="https://karim2.000webhostapp.com/upload_produit2.php";
    SearchView searchView;
    public homeFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mn2 = (MainActivity) getActivity();

        recyclerView2 = view.findViewById(R.id.rv2);
        adapter2 = new MuAdapter2(mn2,produitArrayList2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(mn2));
        recyclerView2.setAdapter(adapter2);

        uploadData();

        //recherche = view.findViewById(R.id.recherche);
      /* recherche.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });*/







        searchView = view.findViewById(R.id.recherch);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filter(s);
                return false;          }
        });















        return view;
    }

    private void filter(String text) {
        ArrayList<Produit> filteredList = new ArrayList<>();

        for (Produit item : produitArrayList2) {
            if (item.getNamp().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        adapter2.filterList(filteredList);
    }


    public void uploadData (){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UP2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        produitArrayList2.clear();
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
                                    String tel = object.getString("tel").trim();
                                    String latitude = object.getString("latitude").trim();
                                    String longitude = object.getString("longitude").trim();

                                    Produit produit2 = new Produit(id, namep,imagep, quentity, prix, tel, place, "iduser", Double.valueOf(latitude) ,Double.valueOf(longitude));
                                    produitArrayList2.add(produit2);
                                    adapter2.notifyDataSetChanged();


                                }
                                MainActivity.cst=0;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(mn2,"Eroor1"+e.toString(),Toast.LENGTH_SHORT).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mn2,"Eroor2"+error.toString(),Toast.LENGTH_SHORT).show();

                    }
                });


        RequestQueue requestQueue = Volley.newRequestQueue(mn2);
        requestQueue.add(stringRequest);

    }
}
