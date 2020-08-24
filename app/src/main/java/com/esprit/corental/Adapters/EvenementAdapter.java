package com.esprit.corental.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.esprit.corental.Controllers.DetailsActivity;
import com.esprit.corental.Controllers.DetailsActivity1;
import com.esprit.corental.Entities.Evenement;
import com.esprit.corental.Entities.Participants;
import com.esprit.corental.Entities.User;
import com.esprit.corental.R;
import com.esprit.corental.Retrofit.INodeJS;

import com.esprit.corental.Utils.database.AppDataBase;
import com.esprit.corental.Map.MapActivity;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class EvenementAdapter extends RecyclerView.Adapter<EvenementAdapter.myViewHolder> {

    Context mContext;
    List<Participants> partList;
    private List<Evenement> mData;
    private List<Evenement> event_list = new ArrayList<>();
    FragmentManager fragmentManager;
    SharedPreferences sharedPreferences, sharedPreferencesUE, sharedPreferencesU;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ImageView favori;
    Button participer,commenter;
    RoundedImageView bk;
    INodeJS myAPI;
    int idus;
    CardView det ;
    TextView titleCamp, dateDebut, dateFin, price, location, username;
    private AppDataBase database ;


    public EvenementAdapter(Context mContext, List<Evenement> mDataa) {
        this.mContext = mContext;
        this.mData = mDataa;
    }






    @NonNull
    @Override
    public EvenementAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.card_item,parent,false);
        EvenementAdapter.myViewHolder vv = new EvenementAdapter.myViewHolder(v);

        sharedPreferencesUE =parent.getContext().getSharedPreferences("UserEvent", Context.MODE_PRIVATE);
        sharedPreferencesU =parent.getContext().getSharedPreferences("CurrentUser", Context.MODE_PRIVATE);
        idus = sharedPreferencesU.getInt("idUser", 0);

         database = AppDataBase.getAppDatabase(context);
        event_list = database.produitDao().getAll();

        return vv;
    }

    @Override
    public void onBindViewHolder(@NonNull EvenementAdapter.myViewHolder holder, int position) {
        final Evenement evenement = mData.get(position);

        favori = holder.favori;
        GetListParticipants(idus,evenement.getId_evenement());

        if (database.produitDao().check_item(evenement.getId_evenement()) != 0){
            favori.setImageResource(R.drawable.logol);
        }




        titleCamp.setText(evenement.getNom_evenement());
        dateDebut.setText(evenement.getDate_debut_evenement());
        dateFin.setText(evenement.getDate_fin_evenement());
        price.setText(evenement.getPrix_evenement()+" DT");
        location.setText(evenement.getLieux_evenement());



        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        myAPI = retrofit.create(INodeJS.class);
        Call<User> call = myAPI.getUserE(evenement.getId_user());
        System.out.println(call.toString()+"callllllllllll");
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();


                  // System.out.println(user.getName() + "teeesssssssssssssssssst");

                try{
                   SharedPreferences.Editor editor = sharedPreferencesUE.edit();
                   editor.putInt("idUser",user.getId());
                   editor.putInt("telUser",user.getTel_user());
                   editor.putString("nomUser",user.getName());
                   editor.putString("prenomUser",user.getPrenom());
                   editor.putString("EmailUser",user.getEmail());
                   editor.putString("imageUser",user.getImage_user());
                   username.setText(user.getName()+" "+user.getPrenom());
                   editor.apply();}
                catch (NullPointerException ignored){}



               // sharedPreferencesUE =getSharedPreferences("UserEvent", Context.MODE_PRIVATE);


            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
            }
        });




        bk = holder.background_img;
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

        public myViewHolder(@NonNull final View itemView) {
            super(itemView);
            background_img = itemView.findViewById(R.id.background_img);
            det = itemView.findViewById(R.id.det);
            dateDebut = itemView.findViewById(R.id.dateDebut);
            dateFin = itemView.findViewById(R.id.dateFin);
            favori = itemView.findViewById(R.id.favori);
            participer = itemView.findViewById(R.id.participer);
            commenter = itemView.findViewById(R.id.commenter);
            price = itemView.findViewById(R.id.price);
            location = itemView.findViewById(R.id.location);
            titleCamp = itemView.findViewById(R.id.titleCamp);
            username = itemView.findViewById(R.id.username);
            username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemView.getContext().startActivity(new Intent(itemView.getContext(), DetailsActivity1.class));

                }
            });

            commenter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                       itemView.getContext().startActivity(new Intent(itemView.getContext(), MapActivity.class));
                }
            });




            participer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition(); // gets item position
                    if (position != RecyclerView.NO_POSITION) {
                        final Evenement ce = mData.get(position);
                        sharedPreferencesU =v.getContext().getSharedPreferences("CurrentUser", Context.MODE_PRIVATE);
                        int idu = sharedPreferencesU.getInt("idUser", 0);
                        int idv = ce.getId_evenement();
                        addParticipant(idu,idv);
                        participer.setText("deja participé!");
                        }
                    }


                });






            favori.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    database = AppDataBase.getAppDatabase(v.getContext());
                    int position = getAdapterPosition(); // gets item position
                    if (position != RecyclerView.NO_POSITION) {
                        //sharedPreferences = v.getContext().getSharedPreferences("Evenement", Context.MODE_PRIVATE);
                        final Evenement ce = mData.get(position);
                        if(database.produitDao().check_item(ce.getId_evenement()) != 0){
                            Toast.makeText(v.getContext()," already exists",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            try {
                                database.produitDao().insertOne(ce);
                                favori.setImageResource(R.drawable.tfb);
                                Toast.makeText(v.getContext(),"inserted",Toast.LENGTH_LONG).show();


                            }catch (Exception e){
                                System.out.println(e.getMessage());
                            }
                        }
                    }

                }
            });



            itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                database = AppDataBase.getAppDatabase(v.getContext());
                                                int position = getAdapterPosition(); // gets item position
                                                if (position != RecyclerView.NO_POSITION) {
                                                    sharedPreferences = v.getContext().getSharedPreferences("Evenement", Context.MODE_PRIVATE);
                                                    sharedPreferencesUE =v.getContext().getSharedPreferences("UserEvent", Context.MODE_PRIVATE);
                                                    Evenement ce = mData.get(position);


                                                    System.out.println();
                                                    int aa = ce.getId_user();
                                                    System.out.println(aa+"aaaaaaa");
                                                   // test(aa);


                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    editor.putString("evenement_nom", ce.getNom_evenement());
                                                    editor.putString("evenement_type", ce.getType_evenement());
                                                    editor.putInt("evenement_prix", ce.getPrix_evenement());
                                                    editor.putString("evenement_img", ce.getPhoto_evenement());
                                                    editor.putString("evenement_desc", ce.getDescription_evenement());
                                                    editor.putString("date_debut", ce.getDate_debut_evenement());
                                                    editor.putInt("evenement_dif", ce.getDifficulte_evenement());
                                                    editor.putInt("id_evenement", ce.getId_evenement());
                                                    editor.putInt("id_user", ce.getId_user());
                                                    editor.putInt("evenement_info", ce.getInfoline());


                                                    editor.apply();
                                                    //det.setVisibility(View.VISIBLE);
                                                    //test.setText(ce.getNom_evenement());


                                                }


                                                itemView.getContext().startActivity(new Intent(itemView.getContext(), DetailsActivity.class));


                                            }
                                        });


            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    return false;
                }
            });


        }
    }

    public void GetListParticipants(final int idu,final int id_ev) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        myAPI = retrofit.create(INodeJS.class);
        Call<List<Participants>> call = myAPI.getParticipantList();
        call.enqueue(new Callback<List<Participants>>() {
            @Override
            public void onResponse(Call<List<Participants>> call, Response<List<Participants>> response) {
             try{   partList = response.body();
                Log.d("test2", String.valueOf(response.body()));

                int i;
                for (i=0;i<partList.size();i++){
                    if (partList.get(i).getId_evenement()==id_ev && partList.get(i).getId_user()==idu){
                        participer.setText("deja participé!");
                    }
                }}
 catch (NullPointerException ignored){}

            }

            @Override
            public void onFailure(Call<List<Participants>> call, Throwable t) {

            }
        });

    }

    public void addParticipant(int id_user, int id_evenement){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        Retrofit retrofit = builder.build();
        myAPI = retrofit.create(INodeJS.class);

        compositeDisposable.add(myAPI.addParticipant(id_user,id_evenement)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        participer.setText("deja participé!");
                        System.out.println("evenement ajouté");
                    }
                })
        );

    }





}







