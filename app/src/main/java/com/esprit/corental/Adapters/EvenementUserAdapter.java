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

//import com.example.coretal.home.ModifierEvenement;
import com.esprit.corental.Entities.Evenement;
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

public class EvenementUserAdapter extends RecyclerView.Adapter<EvenementUserAdapter.myViewHolder>{

    Context mContext;
    private List<Evenement> mData;
    FragmentManager fragmentManager;
    SharedPreferences sharedPreferences,sharedPreferencesUE;
    TextView titreA,descA,location,prixA;
    ImageView bk;
    INodeJS myAPI;


    public EvenementUserAdapter(Context mContext, List<Evenement> mDataa) {
        this.mContext = mContext;
        this.mData = mDataa;
    }






    @NonNull
    @Override
    public EvenementUserAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.card_eventuser,parent,false);
        EvenementUserAdapter.myViewHolder vv = new EvenementUserAdapter.myViewHolder(v);
        return vv;
    }

    @Override
    public void onBindViewHolder(@NonNull EvenementUserAdapter.myViewHolder holder, int position) {
        final Evenement evenement = mData.get(position);
        titreA.setText(evenement.getNom_evenement());
        descA.setText(evenement.getDescription_evenement());
        location.setText(evenement.getLieux_evenement());
        prixA.setText(evenement.getPrix_evenement()+"DT");

        bk = holder.background_img;
        //titre.setText(evenement.getNom_evenement());

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



    }

    @Override
    public int getItemCount() {
        return mData.size();
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
            sharedPreferences = itemView.getContext().getSharedPreferences("CurrentUser", Context.MODE_PRIVATE);
            int idUser = sharedPreferences.getInt("idUser",0);
            sharedPreferencesUE = itemView.getContext().getSharedPreferences("UserEvent", Context.MODE_PRIVATE);
            int idUE = sharedPreferencesUE.getInt("idUser",0);
if (idUser == idUE){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition(); // gets item position
                    if (position != RecyclerView.NO_POSITION) {
                        sharedPreferences = v.getContext().getSharedPreferences("Evenement", Context.MODE_PRIVATE);
                        Evenement ce = mData.get(position);
                        String aa = ce.getNom_evenement();
                        int a = ce.getId_evenement();
                        System.out.println(a+"aaaa");


                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("evenement_nom", ce.getNom_evenement());
                        editor.putString("evenement_type", ce.getType_evenement());
                        editor.putInt("evenement_prix", ce.getPrix_evenement());
                        editor.putString("evenement_img", ce.getPhoto_evenement());
                        editor.putString("evenement_desc", ce.getDescription_evenement());
                        editor.putString("date_debut", ce.getDate_debut_evenement());
                        editor.putString("date_fin", ce.getDate_fin_evenement());
                        editor.putString("lieu_evenement", ce.getLieux_evenement());
                        editor.putInt("evenement_dif", ce.getDifficulte_evenement());
                        editor.putInt("evenement_info", ce.getInfoline());
                        editor.putInt("userIdE", ce.getId_user());
                        editor.putInt("eventId", ce.getId_evenement());
                        editor.putInt("evenement_dis", ce.getDistance_evenement());



                        editor.apply();
                        //det.setVisibility(View.VISIBLE);
                        //test.setText(ce.getNom_evenement());


                       // Fragment fragg = new ModifierEvenement();
                       // ((AppCompatActivity) v.getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.details, fragg).commit();


                    }

                }
            });}


        }
    }
}
