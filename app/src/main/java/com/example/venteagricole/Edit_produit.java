package com.example.venteagricole;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Edit_produit extends AppCompatActivity {
    private EditText namep,quentity, prix, place;
    private Button btn;
    private int pos;
    ImageView back;
    private static String URL_EDIT="https://karim2.000webhostapp.com/edit_produit.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_produit);

       namep=findViewById(R.id.txt_namep);
        quentity=findViewById(R.id.txt_quentity);
        place = findViewById(R.id.txt_place);
        prix = findViewById(R.id.txt_prix);

        back = findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.cst=1;
                Intent intent = new Intent(Edit_produit.this,MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

        btn=findViewById(R.id.btn_edit);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Update_produit();

            }
        });
       Intent intent = getIntent();
        pos = intent.getExtras().getInt("position");
        namep.setText(MilieuFragment.produitArrayList.get(pos).getNamp());
        quentity.setText(MilieuFragment.produitArrayList.get(pos).getQuentiity());
        place.setText(MilieuFragment.produitArrayList.get(pos).getPlace());
        prix.setText(MilieuFragment.produitArrayList.get(pos).getPrix());

    }
    private void Update_produit() {
        final String n = namep.getText().toString().trim();
        final String q = quentity.getText().toString().trim();
        final String pl = place.getText().toString().trim();
        final String pr = prix.getText().toString().trim();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")){
                                Toast.makeText(Edit_produit.this,"Produit modifier",Toast.LENGTH_SHORT).show();
                                MainActivity.cst=1;
                                Intent intent = new Intent(Edit_produit.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                                progressDialog.dismiss();
                            }else{

                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Edit_produit.this,"Error!1  "+e.toString(),Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Edit_produit.this,"Error!2  "+error.toString(),Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id",MilieuFragment.produitArrayList.get(pos).getId());
                params.put("name",n);
                params.put("quentity",q);
                params.put("prix",pr);
                params.put("place",pl);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);



    }


}
