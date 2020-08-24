package com.esprit.corental.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.esprit.corental.Entities.Evenement;
import com.esprit.corental.R;
import com.esprit.corental.Retrofit.INodeJS;
import com.esprit.corental.Utils.database.AppDataBase;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EvenementFaAdapter extends RecyclerView.Adapter<EvenementFaAdapter.myViewHolder> {

    Context mContext;
    private List<Evenement> mData;
    FragmentManager fragmentManager;
    SharedPreferences sharedPreferences;
    ImageView favori;
    RoundedImageView bk;
    INodeJS myAPI;
    CardView det ;
    TextView titleCamp, dateDebut, dateFin, price, location;
    private AppDataBase database ;


    public EvenementFaAdapter(Context mContext, List<Evenement> mDataa) {
        this.mContext = mContext;
        this.mData = mDataa;
    }






    @NonNull
    @Override
    public EvenementFaAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.card_item,parent,false);
        EvenementFaAdapter.myViewHolder vv = new EvenementFaAdapter.myViewHolder(v);
        return vv;
    }

    @Override
    public void onBindViewHolder(@NonNull EvenementFaAdapter.myViewHolder holder, int position) {
        final Evenement evenement = mData.get(position);
        favori =holder.favori;
        favori.setImageResource(R.drawable.logol);
        titleCamp.setText(evenement.getNom_evenement());
        dateDebut.setText(evenement.getDate_debut_evenement());
        dateFin.setText(evenement.getDate_fin_evenement());
        price.setText(evenement.getPrix_evenement()+" DT");
        location.setText(evenement.getLieux_evenement()+", tunisia");


       // String a = (evenement.getPhoto_evenement());


        bk = holder.background_img;
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        myAPI = retrofit.create(INodeJS.class);
        Call<ResponseBody> calll = myAPI.getImage(evenement.getPhoto_evenement());
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
        TextView titre = holder.pr_title;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class myViewHolder extends RecyclerView.ViewHolder {
        public TextView pr_title;
        public RoundedImageView background_img;
        public ImageView favori;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            background_img = itemView.findViewById(R.id.background_img);
            det = itemView.findViewById(R.id.det);
            dateDebut = itemView.findViewById(R.id.dateDebut);
            dateFin = itemView.findViewById(R.id.dateFin);
            favori = itemView.findViewById(R.id.favori);
            price = itemView.findViewById(R.id.price);
            location = itemView.findViewById(R.id.location);
            titleCamp = itemView.findViewById(R.id.titleCamp);


            favori.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    database = AppDataBase.getAppDatabase(mContext);
                    int position = getAdapterPosition(); // gets item position
                    if (position != RecyclerView.NO_POSITION) {
                        sharedPreferences = v.getContext().getSharedPreferences("Evenement", Context.MODE_PRIVATE);
                        Evenement ce = mData.get(position);
                        database.produitDao().delete(ce);
                        Toast.makeText(mContext, "evenement supprimer", Toast.LENGTH_LONG).show();
                       // Fragment fragg = new EvenementFavoris();
                        //((AppCompatActivity) v.getContext()).getSupportFragmentManager().beginTransaction().replace(R.id., fragg).commit();
                    }

                }
            });


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition(); // gets item position
                    if (position != RecyclerView.NO_POSITION) {
                        sharedPreferences = v.getContext().getSharedPreferences("Evenement", Context.MODE_PRIVATE);
                        Evenement ce = mData.get(position);
                        String aa = ce.getNom_evenement();


                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("evenement_nom", ce.getNom_evenement());
                        editor.putString("evenement_type", ce.getType_evenement());
                        editor.putInt("evenement_prix", ce.getPrix_evenement());
                        editor.putString("evenement_img", ce.getPhoto_evenement());
                        editor.putString("evenement_desc", ce.getDescription_evenement());
                        editor.putString("date_debut", ce.getDate_debut_evenement());
                        editor.putInt("evenement_dif", ce.getDifficulte_evenement());
                        editor.putInt("evenement_info", ce.getInfoline());


                        editor.apply();
                        //det.setVisibility(View.VISIBLE);
                        //test.setText(ce.getNom_evenement());


                       // Fragment fragg = new DetailEvent();
                       // ((AppCompatActivity) v.getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.details, fragg).commit();
                    }
                }

            });


        }}
}
