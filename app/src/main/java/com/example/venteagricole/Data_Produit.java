package com.example.venteagricole;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Data_Produit extends AppCompatActivity {
    private EditText namep,quentity,prix,place;
    private Button btn;
    ImageView back,localisation,img_produit;
    private static String URL_SAVE="https://karim2.000webhostapp.com/add_produit.php";
    int PLACE_PICKER_REQUEST = 1;
    String latitude="1.1";
    String longitude="1.1";
    Bitmap bitmap;
    static  int k=0;
    static  int geo=0;
    static  int img=0;
    String photo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data__produit);
        namep = findViewById(R.id.txt_namep);
        quentity=findViewById(R.id.txt_quentity);
        prix = findViewById(R.id.txt_prix);
        place = findViewById(R.id.txt_place);
       btn=findViewById(R.id.btn_sav);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String p = namep.getText().toString().trim();
                String q=quentity.getText().toString().trim();
                String pr=prix.getText().toString().trim();
                String pl=place.getText().toString().trim();
                if (p.isEmpty() ){
                    namep.setError("Veuillez mettez quelque chose dans l'espace");

                }else {if (q.isEmpty()){
                    quentity.setError("Veuillez mettez quelque chose dans l'espace");
                }else{if (pr.isEmpty()){
                    prix.setError("Veuillez mettez quelque chose dans l'espace");
                }else {if (pl.isEmpty()){
                    place.setError("Veuillez mettez quelque chose dans l'espace");
                }else{if (geo==0) {
                    Toast.makeText(Data_Produit.this,"Il faut mettre geolocalisation",Toast.LENGTH_SHORT).show();
                }else{if (img==0){
                    Toast.makeText(Data_Produit.this,"Il faut mettre l'image",Toast.LENGTH_SHORT).show();

                }
                else{
                    insert();
                    }
                }

                }
                }

                }

            }}
        });

         back = findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity.cst=1;
                Intent intent = new Intent(Data_Produit.this,MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

        localisation = findViewById(R.id.img_place);
        localisation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {k=2;geo=1;
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(Data_Produit.this)
                            ,PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }

            }
        });
        img_produit = findViewById(R.id.img_produit);
        img_produit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               chooseFile();
img=1;
            }
        });

    }
   public void chooseFile(){
        k=1;
        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(gallery,"Sellect Picture"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

      if(k==2){
      if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                latitude = String.valueOf(place.getLatLng().latitude);
                longitude = String.valueOf(place.getLatLng().longitude);

            }}
        }if(k==1){
       if (requestCode == 1 && resultCode == RESULT_OK &&data != null && data.getData() != null){
            Uri filePath= data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                img_produit.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }
            photo = getStringImage(bitmap);
        }}}
    //}

    public String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageByteArray,Base64.DEFAULT);
        return encodedImage;

    }

    private void insert() {
        final String n=namep.getText().toString().trim();
        final String q = quentity.getText().toString().trim();
        final String pr=prix.getText().toString().trim();
        final String pl = place.getText().toString().trim();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SAVE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")){
                                Toast.makeText(Data_Produit.this,"Produit ajouté",Toast.LENGTH_SHORT).show();
                                MainActivity.cst=1;
                                Intent intent = new Intent(Data_Produit.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                                img=0;geo=0;
                                progressDialog.dismiss();
                            }else{

                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Data_Produit.this,"Produit non ajouté  ",Toast.LENGTH_SHORT).show();
                             progressDialog.dismiss();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Data_Produit.this,"Produit non ajouté  ",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
               params.put("namep", n);
                params.put("imagep",photo);
                params.put("quentity", q);
                params.put("prix", pr);
                params.put("place", pl);
                params.put("tel", MainActivity.extraTel);
                params.put("iduser", MainActivity.extraId);
                params.put("latitude", latitude);
                params.put("longitude", longitude);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);



    }

}
