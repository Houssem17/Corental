package com.esprit.corental.Controllers;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.esprit.corental.Entities.Evenement;
import com.esprit.corental.Adapters.EvenementUserAdapter;
import com.esprit.corental.Entities.Article;
import com.esprit.corental.R;
import com.esprit.corental.Retrofit.INodeJS;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {

    INodeJS myAPI;
         SharedPreferences sharedPreferencesU;
    List<Evenement> evenementsList ;
    List<Article> articlesList ;
    RecyclerView recyclerView,articlesU;
    Context mContext;
    TextView nom,email_user,tel_user;
    ImageView imguser;


    public Profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);




        sharedPreferencesU = getContext().getSharedPreferences("CurrentUser", Context.MODE_PRIVATE);
        String email2 = sharedPreferencesU.getString("EmailUser","");
        String nomu = sharedPreferencesU.getString("nomUser", "");
        String prenomu = sharedPreferencesU.getString("prenomUser", "");
        int telu = sharedPreferencesU.getInt("telUser", 0);
        int idu = sharedPreferencesU.getInt("idUser", 0);




        nom = rootView.findViewById(R.id.nom);
        email_user = rootView.findViewById(R.id.email_user);
        tel_user = rootView.findViewById(R.id.telephone);
        recyclerView = rootView.findViewById(R.id.publications);

        imguser = rootView.findViewById(R.id.imguser);


        nom.setText(nomu+" "+prenomu);
        email_user.setText(email2);
        tel_user.setText(String.valueOf(telu));
        System.out.println(email2);

        getEvenemenetUser(idu);
       // getArticlesUser(idu);







        String imageu = sharedPreferencesU.getString("imageUser","");


        //get image
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        myAPI = retrofit.create(INodeJS.class);
        Call<ResponseBody> calll = myAPI.getImage(imageu);
        calll.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> calll, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        // display the image data in a ImageView or save it
                        Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                        imguser.setImageBitmap(bmp);
                    } else {
                        // TODO
                    }
                } else {
                    // TODO
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> calll, Throwable t) {
                // TODO
            }
        });



        Log.d("test2", String.valueOf(evenementsList));
        return rootView;
    }


    public void getEvenemenetUser(int id_user){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        myAPI = retrofit.create(INodeJS.class);
        Call<List<Evenement>> call = myAPI.getEvenemenetUser(id_user);
        call.enqueue(new Callback<List<Evenement>>() {
            @Override
            public void onResponse(Call<List<Evenement>> call, Response<List<Evenement>> response) {
                evenementsList = response.body();
                Log.d("test2", String.valueOf(response.body()));

                EvenementUserAdapter adapter = new EvenementUserAdapter(mContext, evenementsList);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                if(response.message().equals("nothing was found")){
                    Toast.makeText(getActivity(),"you have 0 event",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Evenement>> call, Throwable t) {

            }
        });
    }

/*
    public void getArticlesUser(int id_user){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        myAPI = retrofit.create(INodeJS.class);
        Call<List<Article>> call = myAPI.getArticleUser(id_user);
        call.enqueue(new Callback<List<Article>>() {
            @Override
            public void onResponse(Call<List<Article>> call, Response<List<Article>> response) {
                articlesList = response.body();
                Log.d("test2", String.valueOf(response.body()));

                ArticlesUserAdapter adapter = new ArticlesUserAdapter(mContext, articlesList);
//                articlesU.setAdapter(adapter);
              //  articlesU.setLayoutManager(new LinearLayoutManager(mContext));
                /*if(response.message().equals("nothing was found")){
                    Toast.makeText(getActivity(),"you have 0 event",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Article>> call, Throwable t) {

            }
        });*/






    }








