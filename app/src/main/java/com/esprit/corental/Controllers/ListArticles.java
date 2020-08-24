package com.esprit.corental.Controllers;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esprit.corental.Entities.Article;
import com.esprit.corental.Adapters.ArticleAdapter;
import com.esprit.corental.R;
import com.esprit.corental.Retrofit.INodeJS;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListArticles extends Fragment {

    List<Article> articlesList ;
    RecyclerView recyclerView;
    INodeJS myAPI;
    Context mContext;


    public ListArticles() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        GetListArticles();
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_list_articles, container, false);

        recyclerView = rootView.findViewById(R.id.publications);


        return rootView;
    }

    public void GetListArticles() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        myAPI = retrofit.create(INodeJS.class);
        Call<List<Article>> call = myAPI.getArticleList();
        call.enqueue(new Callback<List<Article>>() {
            @Override
            public void onResponse(Call<List<Article>> call, Response<List<Article>> response) {
                articlesList = response.body();
                Log.d("test2", String.valueOf(response.body()));
                //recyclerView = (R.id.publications);
                ArticleAdapter adapter = new ArticleAdapter(mContext, articlesList);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

            }

            @Override
            public void onFailure(Call<List<Article>> call, Throwable t) {

            }
        });
    }

}
