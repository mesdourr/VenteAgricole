package com.example.venteagricole;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class signUp extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        findViews();
    }

    private EditText txtFullname;
    private EditText txtEmail;
    private EditText txtUser;
    private EditText txtPass;
    private EditText txtTel;
    private ProgressBar loading;
    private Button btnSignUp;
    private TextView txtSingUp;
   // private static String URL_REGIST="http://192.168.1.45/venteagricole/register.php";
    private static String URL_REGIST="https://karim2.000webhostapp.com/register.php";


    /**
     * Find the Views in the layout
     */
    private void findViews() {
        txtFullname = (EditText) findViewById(R.id.txtFullname);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtUser = (EditText) findViewById(R.id.txtUser);
        txtPass = (EditText) findViewById(R.id.txtPass);
        txtTel = findViewById(R.id.txtTel);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        loading=findViewById(R.id.loading);
        txtSingUp = (TextView) findViewById(R.id.txtSingUp2);
        txtSingUp.setOnClickListener(this);

        btnSignUp.setOnClickListener( this );
    }

    /**
     * Handle button click events
     */
    @Override
    public void onClick(View v) {
        if ( v == txtSingUp ) {
            finish();
            // Handle clicks for btnLogin
        }
        if ( v == btnSignUp ) {
            // Handle clicks for btnSignUp
            String user=txtUser.getText().toString().trim();
            String mpass=txtPass.getText().toString().trim();
            String fullname=txtFullname.getText().toString().trim();
            String email=txtEmail.getText().toString().trim();
            String tel = txtTel.getText().toString().trim();


            if (fullname.isEmpty() ){
                txtFullname.setError("Veuillez mettez quelque chose dans l'espace");

            }else {if (email.isEmpty()){
                txtEmail.setError("Veuillez mettez quelque chose dans l'espace");
            }else{if (tel.isEmpty()){
                txtTel.setError("Veuillez mettez quelque chose dans l'espace");
            }else {if (user.isEmpty()){
                txtUser.setError("Veuillez mettez quelque chose dans l'espace");
            }else{if (mpass.isEmpty()) {
                txtPass.setError("Veuillez mettez quelque chose dans l'espace");
            }else{
                Regist();
                finish();}
            }

            }
            }

            }

        }
    }

private  void Regist(){
        btnSignUp.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        final String name=txtFullname.getText().toString().trim();
         final String email=txtEmail.getText().toString().trim();
    final String tel=txtTel.getText().toString().trim();
         final String username=txtUser.getText().toString().trim();
          final String password=txtPass.getText().toString().trim();
    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGIST,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        if (success.equals("1")){
                            Toast.makeText(signUp.this,"Votre compte est crée  ",Toast.LENGTH_SHORT).show();
                            btnSignUp.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.GONE);
                        }else{

                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(signUp.this,"Error  ",Toast.LENGTH_SHORT).show();
                        btnSignUp.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);                   }
                    /****nouveaou*/


                   /*****fin */

                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                   Toast.makeText(signUp.this,"Error",Toast.LENGTH_SHORT).show();
                    btnSignUp.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.GONE);
                }
            })
    {
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String,String> params = new HashMap<>();
            params.put("name",name);
            params.put("email",email);
            params.put("tel",tel);
            params.put("user",username);
            params.put("pwd",password);
            return params;
        }
    };
    RequestQueue requestQueue = Volley.newRequestQueue(this);
    requestQueue.add(stringRequest);
}
}
