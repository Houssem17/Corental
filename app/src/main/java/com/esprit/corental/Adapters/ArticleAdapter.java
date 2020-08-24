package com.esprit.corental.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.esprit.corental.Entities.Article;
import com.esprit.corental.R;
import com.esprit.corental.Retrofit.INodeJS;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.myViewHolder>
        implements Filterable {

    Context mContext;
    private List<Article> mData;
    private List<Article> contactListFiltered;
    private ArticleAdapterListener listener;
    INodeJS myAPI;
    TextView titreA,descA,location,prixA;
    Switch louer,vendre;
    ImageView bk;

    public class myViewHolder extends RecyclerView.ViewHolder {
        public TextView titreA;
        public TextView descA;
        public ImageView background_img;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            background_img = itemView.findViewById(R.id.card_background);
            titreA = itemView.findViewById(R.id.titreA);
            descA = itemView.findViewById(R.id.descA);





        }
    }


    public ArticleAdapter(Context mContext, List<Article> mDataa) {
        this.mContext = mContext;

        this.mData = mDataa;


        this.contactListFiltered = mDataa;
    }






    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.card_article,parent,false);
        ArticleAdapter.myViewHolder vv = new ArticleAdapter.myViewHolder(v);
        return vv;
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        final Article article = mData.get(position);

try{        titreA.setText(article.getTitre_article());
        descA.setText(article.getDescription_article());}
catch (NullPointerException ignored){}
        //location.setText(article.getLocation_article());
       // prixA.setText(article.getPrix_article()+"DT");
        //if (article.getLouer_article().equals("on")){
           // louer.setChecked(true);
       // }else {
          //  louer.setChecked(false);
       // }
       // if (article.getVendre_article().equals("on")){
           // vendre.setChecked(true);
       // }else {
           // vendre.setChecked(false);
       // }

        bk = holder.background_img;
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
       return contactListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = mData;
                } else {
                    List<Article> filteredList = new ArrayList<>();
                    for (Article row : mData) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getTitre_article().toLowerCase().contains(charString.toLowerCase()) || row.getDescription_article().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<Article>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public interface ArticleAdapterListener {
       // void onArticleSelected(Article contact);
    }

}
