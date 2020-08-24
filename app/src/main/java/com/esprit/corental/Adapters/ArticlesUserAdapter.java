package com.esprit.corental.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.esprit.corental.Entities.Article;
import com.esprit.corental.R;
import com.esprit.corental.Retrofit.INodeJS;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArticlesUserAdapter extends RecyclerView.Adapter<ArticlesUserAdapter.myViewHolder>{

    Context mContext;
    private List<Article> mAData;
    FragmentManager fragmentManager;
    SharedPreferences sharedPreferences;
    TextView titreA,descA,location,prixA;
    Switch louer,vendre;
    ImageView bk;
    INodeJS myAPI;


    public ArticlesUserAdapter(Context mContext, List<Article> mDataa) {
        this.mContext = mContext;
        this.mAData = mDataa;
    }






    @NonNull
    @Override
    public ArticlesUserAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.card_article,parent,false);
        ArticlesUserAdapter.myViewHolder vv = new ArticlesUserAdapter.myViewHolder(v);
        return vv;
    }

    @Override
    public void onBindViewHolder(@NonNull ArticlesUserAdapter.myViewHolder holder, int position) {
        final Article article = mAData.get(position);

        titreA.setText(article.getTitre_article());
        descA.setText(article.getDescription_article());
        location.setText(article.getLocation_article());
        prixA.setText(article.getPrix_article()+"DT");
        if (article.getLouer_article().equals("on")){
            louer.setChecked(true);
        }else {
            louer.setChecked(false);
        }
        if (article.getVendre_article().equals("on")){
            vendre.setChecked(true);
        }else {
            vendre.setChecked(false);
        }


        bk = holder.background_img;
        //titre.setText(evenement.getNom_evenement());

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        myAPI = retrofit.create(INodeJS.class);
        Call<ResponseBody> calll = myAPI.getImage(article.getImage_article());
        calll.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> calll, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        // display the image data in a ImageView or save it
                        Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                        bk.setImageBitmap(bmp);
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



    }

    @Override
    public int getItemCount() {
        return mAData.size();
    }


    public class myViewHolder extends RecyclerView.ViewHolder {
        public TextView pr_title;
        public ImageView background_img;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            background_img = itemView.findViewById(R.id.card_background);
            titreA = itemView.findViewById(R.id.titreA);
            descA = itemView.findViewById(R.id.descA);
            location = itemView.findViewById(R.id.location);
            prixA = itemView.findViewById(R.id.prixA);
            louer = itemView.findViewById(R.id.louer);
            vendre = itemView.findViewById(R.id.vendre);


        }
    }
}
