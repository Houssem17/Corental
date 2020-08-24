package com.esprit.corental.Controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.esprit.corental.Entities.User;

import com.esprit.corental.R;
import com.esprit.corental.Retrofit.INodeJS;
import com.esprit.corental.Retrofit.RetrofitClient;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    INodeJS myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    EditText email,password;
    Button btn_register,btn_login;
    SharedPreferences sharedPreferences;
    //public static final String EXTRA_TEXT = "com.example.atelier1.EXTRA_TEXT";

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init API
        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJS.class);

        //View
        btn_login = findViewById(R.id.button);
        btn_register = findViewById(R.id.button2);
        email = findViewById(R.id.editText);
        password = findViewById(R.id.editText7);


        //sharedPreferences
        sharedPreferences = getSharedPreferences("testt", Context.MODE_PRIVATE);
        email.setText(sharedPreferences.getString("test", ""));
        password.setText(sharedPreferences.getString("test1", ""));




        //Button action
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String e = email.getText().toString();
                String p = password.getText().toString();
                if (e.equals("")||p.equals("")){
                    Toast.makeText(MainActivity.this, "Vérifier Vos Données",Toast.LENGTH_SHORT).show();
                }else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("test", e);
                    editor.putString("test1", p);
                    editor.apply();



                    loginUser(e,p);


                }}
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });
    }
    private void loginUser(final String email, String password) {
        compositeDisposable.add(myAPI.loginUser(email,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if (s.contains("encrypted_password")){
                            Toast.makeText(MainActivity.this,"Login Successfully" ,Toast.LENGTH_SHORT).show();
                            loadClientData();

                            Intent i = new Intent(MainActivity.this, Menu.class);
                            startActivity(i);}
                        else
                            Toast.makeText(MainActivity.this,""+s,Toast.LENGTH_SHORT).show(); //Show error from API

                    }
                })
        );
    }

    public void loadClientData(){
        sharedPreferences =getApplicationContext().getSharedPreferences("CurrentUser", Context.MODE_PRIVATE);
        String email2 = email.getText().toString();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        myAPI = retrofit.create(INodeJS.class);
        Call<User> call = myAPI.getUser(email2);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
               System.out.println(user.getId()+"teeesssssssssssssssssst");


                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("idUser",user.getId());
               editor.putInt("telUser",user.getTel_user());
                editor.putString("nomUser",user.getName());
                editor.putString("prenomUser",user.getPrenom());
                editor.putString("EmailUser",user.getEmail());
                editor.putString("imageUser",user.getImage_user());
               editor.apply();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
            }
        });
    }
}


