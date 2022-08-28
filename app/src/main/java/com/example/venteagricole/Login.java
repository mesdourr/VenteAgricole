package com.example.venteagricole;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity implements View.OnClickListener {
    WebView webView;
    private EditText txtUsername;
    private EditText txtPassword;
    private Button btnLogin;
    private TextView txtSingUp;
    private ProgressBar loading;
    private static String URL_LOGIN="https://karim2.000webhostapp.com/login.php";
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        webView = findViewById(R.id.web_view);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        ConnectivityManager connectivityManager = (ConnectivityManager)
                getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo ==null || !networkInfo.isConnected() || !networkInfo.isAvailable()){
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.alert_dialog);
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations =
                    android.R.style.Animation_Dialog;

            Button btTryAgain = dialog.findViewById(R.id.bt_try_again);
            btTryAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recreate();
                }
            });
            dialog.show();

        }else{
            // Intent intent = new Intent(NoInternet.this,Login.class);
            // startActivity(intent);
        }



        findViews();
        sessionManager = new SessionManager(this);

    }


    /**
     * Find the Views in the layout
     */
    private void findViews() {
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        loading=findViewById(R.id.loading);
        txtSingUp = (TextView) findViewById(R.id.txtSingUp2);
        btnLogin.setOnClickListener(this);
        txtSingUp.setOnClickListener(this);
    }
    /**
     * Handle button click events
     */
    @Override
    public void onClick(View v) {
        if ( v == btnLogin ) {
            // Handle clicks for btnLogin
            String user=txtUsername.getText().toString().trim();
            String mpass=txtPassword.getText().toString().trim();
            if (user.isEmpty() ){
                txtUsername.setError("Veuillez mettez quelque chose dans l'espace");

            }else {if (mpass.isEmpty()){
                txtPassword.setError("Veuillez mettez quelque chose dans l'espace");
            }else{
                login(user,mpass);}

            }

        }

        if ( v == txtSingUp ) {
            Intent intent=new Intent(Login.this,signUp.class);
            startActivity(intent);
            // Handle clicks for btnLogin
        }
    }

public void login(final String user, final String mpass)
{
    btnLogin.setVisibility(View.GONE);
    loading.setVisibility(View.VISIBLE);
    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                       JSONArray jsonArray = jsonObject.getJSONArray("login");

                        if (success.equals("1")){
                           for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                               String id = object.getString("id").trim();
                                String name = object.getString("name").trim();
                                String email = object.getString("email").trim();
                               String tel = object.getString("tel").trim();
                               String user = object.getString("username").trim();


                                sessionManager.createSession(id,name,email,tel,user);


                                Toast.makeText(Login.this,"login Success",Toast.LENGTH_SHORT).show();
                               Intent intent=new Intent(Login.this,MainActivity.class);
                               intent.putExtra("id",id);
                               intent.putExtra("name",name);
                               intent.putExtra("email",email);
                               intent.putExtra("tel",tel);
                               intent.putExtra("username",user);

                               startActivity(intent);
                               finish();


                            btnLogin.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.GONE);}
                        }if (success.equals("0")){
                            Toast.makeText(Login.this,"Mot de passe incorrect",Toast.LENGTH_SHORT).show();
                            btnLogin.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.GONE);
                        }
                        if (success.equals("-1")){
                            Toast.makeText(Login.this,"Nom d'utilisateur invalide",Toast.LENGTH_SHORT).show();
                            btnLogin.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(Login.this,"Eroor",Toast.LENGTH_SHORT).show();
                        btnLogin.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Login.this,"Eroor",Toast.LENGTH_SHORT).show();
                    btnLogin.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.GONE);
                }
            })
    {
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String,String> params = new HashMap<>();
            params.put("user",user);
            params.put("pwd",mpass);
            return params;
        }
    };

    RequestQueue requestQueue = Volley.newRequestQueue(this);
    requestQueue.add(stringRequest);
}


}
