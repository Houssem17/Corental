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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.esprit.corental.R;
import com.esprit.corental.Retrofit.INodeJS;
import com.esprit.corental.Entities.Evenement;
import com.esprit.corental.Entities.Participants;

import java.util.List;

import androidx.fragment.app.Fragment;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailEvent extends Fragment {
    SharedPreferences sharedPreferencesV, sharedPreferencesU, sharedPreferencesUE;

    Button gotop, participer;
    List<Participants> partList;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ImageView imdu,img;
    TextView titre,lieu,username,prix, description, date, diff, info;
    INodeJS myAPI;


    public DetailEvent() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View rootView = inflater.inflate(R.layout.fragment_detail_event, container, false);

        sharedPreferencesV = getContext().getSharedPreferences("Evenement", Context.MODE_PRIVATE);
        sharedPreferencesU = getContext().getSharedPreferences("CurrentUser", Context.MODE_PRIVATE);
        sharedPreferencesUE = getContext().getSharedPreferences("UserEvent", Context.MODE_PRIVATE);
        String nom_evanement = sharedPreferencesV.getString("evenement_nom","");
        String lieu_evenement = sharedPreferencesV.getString("evenement_type","");
        String nom_user = sharedPreferencesUE.getString("nomUser","");
        String prenom_user = sharedPreferencesUE.getString("prenomUser","");
        String img_evenement = sharedPreferencesV.getString("evenement_img","");
        String desc_evenement = sharedPreferencesV.getString("evenement_desc","");
        String dateD = sharedPreferencesV.getString("date_debut","");
        int diff_evenement = sharedPreferencesV.getInt("evenement_dif",0);
        int infoline_evenement = sharedPreferencesV.getInt("evenement_info",0);
       final int id_cu = sharedPreferencesU.getInt("idUser",0);
        final int id_ev = sharedPreferencesV.getInt("id_evenement",0);
        System.out.println(nom_evanement+lieu_evenement);



        gotop = rootView.findViewById(R.id.gotoprofil);
        imdu = rootView.findViewById(R.id.imguser);
        titre = rootView.findViewById(R.id.title);
        lieu = rootView.findViewById(R.id.category);
        username = rootView.findViewById(R.id.username);
        prix = rootView.findViewById(R.id.price);
        img = rootView.findViewById(R.id.img);
        description = rootView.findViewById(R.id.description);
        date = rootView.findViewById(R.id.date);
        diff = rootView.findViewById(R.id.diff);
        info = rootView.findViewById(R.id.info);
        participer = rootView.findViewById(R.id.participer);
        participer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit.Builder builder = new Retrofit.Builder()
                        .baseUrl("http://10.0.2.2:3000")
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
                Retrofit retrofit = builder.build();
                myAPI = retrofit.create(INodeJS.class);

                compositeDisposable.add(myAPI.addParticipant(id_cu,id_ev)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                participer.setText("deja participer!");
                                System.out.println("evenement ajouté");
                            }
                        })
                );
                updateProfile(id_ev,0);
            }
        });


        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        myAPI = retrofit.create(INodeJS.class);
        Call<List<Participants>> call = myAPI.getParticipantList();
        call.enqueue(new Callback<List<Participants>>() {
            @Override
            public void onResponse(Call<List<Participants>> call, Response<List<Participants>> response) {
                partList = response.body();
                Log.d("test2", String.valueOf(response.body()));
                int i;
                try
                { for (i=0;i<partList.size();i++){
                    if (partList.get(i).getId_evenement()==id_ev && partList.get(i).getId_user()==id_cu){
                        participer.setText("deja participer!");
                   }
               }} catch (NullPointerException Ignored) {}


            }

            @Override
            public void onFailure(Call<List<Participants>> call, Throwable t) {

            }
        });


        lieu.setText(lieu_evenement);
        titre.setText(nom_evanement);
        username.setText(nom_user+" "+prenom_user);
        description.setText(desc_evenement);
        date.setText("à venir le :"  +dateD);
        diff.setText(diff_evenement+"/10");
        info.setText(String.valueOf(infoline_evenement));
        username.setText(nom_user+" "+prenom_user);
        System.out.println(nom_user+" "+prenom_user+"zzzzzzzzzzzzzzzzzz");




        //get image
/*        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:1337")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();*/
        myAPI = retrofit.create(INodeJS.class);
        Call<ResponseBody> calll = myAPI.getImage(img_evenement);
        calll.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> calll, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        // display the image data in a ImageView or save it
                        Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                        img.setImageBitmap(bmp);
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
        imdu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // Fragment fragg = new ProfilePub();
              //  ((AppCompatActivity)getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.navigation_home, fragg).commit();

            }
        });





       return rootView;
    }
    private void updateProfile(int id_ev, int nbplace_evenement){
        //sharedPreferences = getContext().getSharedPreferences("testt", Context.MODE_PRIVATE);
        //int userId = sharedPreferences.getInt("idUser",0);
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        myAPI = retrofit.create(INodeJS.class);
        Call<Evenement> call = myAPI.updateNbrPlace(id_ev, nbplace_evenement);
        call.enqueue(new Callback<Evenement>() {
            @Override
            public void onResponse(Call<Evenement> call, Response<Evenement> response) {

                Toast.makeText(getContext(),response.message(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Evenement> call, Throwable t) {
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });


    }




}
