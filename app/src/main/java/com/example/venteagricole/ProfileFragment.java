package com.example.venteagricole;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private static final String TAG =MainActivity.class.getSimpleName() ;
    private static String URL_READ="https://karim2.000webhostapp.com/read_detail.php";
    private static String URL_EDIT="https://karim2.000webhostapp.com/edit_detail.php";
    private static String URL_UPLOAD="https://karim2.000webhostapp.com/upload.php";
    MainActivity mn;
    Bitmap bitmap;
   /* Uri imageuri;*/
    CircleImageView imgProfil;
    TextView name;
    EditText txtName,txtUser,txtEmail,txtId;
    Menu action;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mn = (MainActivity) getActivity();

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);
         name = (TextView) view.findViewById(R.id.txt_namep);
        /*name.setText(mn.extraname);*/
         txtName = view.findViewById(R.id.txtName);
        /*txtName.setText(mn.extraname);*/
         txtUser = view.findViewById(R.id.txtUser);
       /* txtUser.setText(mn.extraUser);*/
        txtEmail = view.findViewById(R.id.txtEmail);
        /*txtEmail.setText(mn.extraEmail);*/
        txtId = view.findViewById(R.id.txtId);
        //txtId.setText(mn.extraId);*/


        //getUserDetail();


        txtName.setFocusableInTouchMode(false);
        txtEmail.setFocusableInTouchMode(false);
        txtUser.setFocusableInTouchMode(false);
        txtId.setFocusableInTouchMode(false);


        ImageView img_dec =  view.findViewById(R.id.img_deconnxion);
        img_dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 /****code*/

                 mn.sessionManager.logout();
                Toast.makeText(mn,"Déconnexion",Toast.LENGTH_SHORT).show();
                /*mn.finish();
                Intent intent=new Intent(mn,Login.class);
                startActivity(intent);*/

            }
        });


         imgProfil = view.findViewById(R.id.img_profil);
        imgProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();

            }
        });






        ImageView imgsave = view.findViewById(R.id.img_save);
        imgsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDetail();
               txtName.setFocusableInTouchMode(false);
                txtEmail.setFocusableInTouchMode(false);
                txtUser.setFocusableInTouchMode(false);
                txtId.setFocusableInTouchMode(false);


            }
        });

         final ImageView imgmodifier =view.findViewById(R.id.img_modifier);
        imgmodifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mn,"Modification possible",Toast.LENGTH_SHORT).show();
                txtName.setFocusableInTouchMode(true);
                txtEmail.setFocusableInTouchMode(true);
                txtUser.setFocusableInTouchMode(true);
                txtId.setFocusableInTouchMode(true);
                InputMethodManager imm = (  InputMethodManager) mn.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(txtName,InputMethodManager.SHOW_IMPLICIT);



            }
        });
        return view;
    }
    public void chooseFile(){
        //imgProfil.setImageBitmap(null);
        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(gallery,"Sellect Picture"), 1);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == 1 && resultCode == mn.RESULT_OK && data != null && data.getData() != null){

            Uri filePath= data.getData();
            try {
                InputStream inputStream = mn.getContentResolver().openInputStream(filePath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                imgProfil.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }

            UploadPicture(mn.extraId,getStringImage(bitmap));

            super.onActivityResult(requestCode, resultCode, data);
        }
    }



    public String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageByteArray,Base64.DEFAULT);
        return encodedImage;

    }


    public void getUserDetail(){
        //Toast.makeText(mn," Entrer",Toast.LENGTH_SHORT).show();
final ProgressDialog progressDialog = new ProgressDialog(mn);
progressDialog.setMessage("Loading...");
progressDialog.show();



        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // imgProfil.setImageResource(R.drawable.ic_fullname);
                       progressDialog.dismiss();
                        Log.i(TAG, response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("read");
                            if (success.equals("1")){
                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String strname = object.getString("name").trim();
                                    String stemail= object.getString("email").trim();
                                    String sttel= object.getString("tel").trim();
                                    String struser = object.getString("username").trim();
                                    String strImage = object.getString("image").trim();

                                    name.setText(strname);
                                    txtName.setText(strname);
                                    txtEmail.setText(stemail);
                                    txtId.setText(sttel);
                                    txtUser.setText(struser);
                                    Picasso.get().load(strImage).into(imgProfil);

                                }

                            }else{

                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                         //   Toast.makeText(mn," Error!1  "+e.toString(),Toast.LENGTH_SHORT).show();
                        }
                        /****nouveaou*/


                        /*****fin */

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                      //  Toast.makeText(mn," Error!2  "+error.toString(),Toast.LENGTH_SHORT).show();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id",mn.extraId);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mn);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserDetail();
    }

    private void UploadPicture(final String id, final String photo) {

        final ProgressDialog progressDialog = new ProgressDialog(mn);
        progressDialog.setMessage(" Uploading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPLOAD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i(TAG,response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){

                                Toast.makeText(mn," Success",Toast.LENGTH_SHORT).show();

                                getUserDetail();

                            }else{

                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(mn," Error!1 Try Again "+e.toString(),Toast.LENGTH_SHORT).show();

                        }
                        /****nouveaou*/


                        /*****fin */

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(mn," Error!2  Try Again "+error.toString(),Toast.LENGTH_SHORT).show();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id",id);
                params.put("photo",photo);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mn);
        requestQueue.add(stringRequest);



    }


  public void  saveDetail(){
      final String name=txtName.getText().toString().trim();
      final String email=txtEmail.getText().toString().trim();
      final String tel=txtId.getText().toString().trim();
      final String username=txtUser.getText().toString().trim();
      final String id= mn.extraId;

      final ProgressDialog progressDialog = new ProgressDialog(mn);
      progressDialog.setMessage("Saving...");
      progressDialog.show();

      StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT,
              new Response.Listener<String>() {
                  @Override
                  public void onResponse(String response) {
                      progressDialog.dismiss();
                      try {
                          JSONObject jsonObject = new JSONObject(response);
                          String success = jsonObject.getString("success");

                          if (success.equals("1")){

                              Toast.makeText(mn," Modification fait",Toast.LENGTH_SHORT).show();
                              mn.sessionManager.createSession(id,name,email,tel,username);
                              getUserDetail();
                          }else{

                          }
                      }catch (JSONException e) {
                          e.printStackTrace();
                          progressDialog.dismiss();
                          Toast.makeText(mn," Error!1  ",Toast.LENGTH_SHORT).show();

                      }
                      /****nouveaou*/


                      /*****fin */

                  }
              },
              new Response.ErrorListener() {
                  @Override
                  public void onErrorResponse(VolleyError error) {
                      progressDialog.dismiss();
                      Toast.makeText(mn," Error  ",Toast.LENGTH_SHORT).show();

                  }
              })
      {
          @Override
          protected Map<String, String> getParams() throws AuthFailureError {
              Map<String,String> params = new HashMap<>();
              params.put("id",mn.extraId);
              params.put("name",name);
              params.put("email",email);
              params.put("tel",tel);
              params.put("user",username);

              return params;
          }
      };
      RequestQueue requestQueue = Volley.newRequestQueue(mn);
      requestQueue.add(stringRequest);
  }




}
