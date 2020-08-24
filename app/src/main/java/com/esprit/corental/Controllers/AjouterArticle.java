package com.esprit.corental.Controllers;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.esprit.corental.R;
import com.esprit.corental.Retrofit.INodeJS;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class AjouterArticle extends Fragment {

    INodeJS myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    Button addA;
    EditText nomA,descA,lieuA,prixA;
    Switch louer,vendre;
    SharedPreferences sharedPreferencesU;
    String lou,ven;
   // private static final String[] cats = {"louer", "colouer", "xx", "x"};
   // Spinner cat;

    //upload imgae
    Uri picUri;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 107;
    private final static int IMAGE_RESULT = 200;
    public Button fabCamera, fabUpload;
    Bitmap mBitmap;
    TextView textView;
    ImageView imageView;


    public AjouterArticle() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_ajouter_article, container, false);

        sharedPreferencesU = getContext().getSharedPreferences("CurrentUser", Context.MODE_PRIVATE);
        final int idu = sharedPreferencesU.getInt("idUser", 0);

        addA = rootView.findViewById(R.id.addE);
        nomA = rootView.findViewById(R.id.nomA);
        descA = rootView.findViewById(R.id.descA);
        lieuA = rootView.findViewById(R.id.lieuA);

        prixA = rootView.findViewById(R.id.prixA);
        louer = rootView.findViewById(R.id.louer);
        vendre = rootView.findViewById(R.id.vendre);

        fabCamera = rootView.findViewById(R.id.fab);
        fabUpload = rootView.findViewById(R.id.fabUpload);
        imageView = rootView.findViewById(R.id.imageView);
        textView = rootView.findViewById(R.id.textView);


        //upload image



        //spinner
       // ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(),  android.R.layout.simple_spinner_item, cats);
       // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //cat.setAdapter(adapter);


        louer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (louer.isChecked()==true){
                     lou = louer.getTextOn().toString();
                }else lou = louer.getTextOff().toString();
                System.out.println(lou+"tesst");


            }
        });

        vendre.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (vendre.isChecked()==true){
                    ven = louer.getTextOn().toString();
                }else ven = louer.getTextOff().toString();

            }
        });






         addA.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 addArtic(nomA.getText().toString(),descA.getText().toString(),lieuA.getText().toString(),"test",15,idu, ven,lou);
                 System.out.println("article ajouter");
             }
         });













        return  rootView;
    }


    private void addArtic(final String titre_article, final String description_article, final String location_article,
                              final String image_article, final int prix_article, final int user_id, final String vendre_article, final String louer_article
    ) {


        System.out.println(user_id);
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        Retrofit retrofit = builder.build();
        myAPI = retrofit.create(INodeJS.class);

        compositeDisposable.add(myAPI.addArticle(titre_article,description_article,location_article,image_article,prix_article,user_id,vendre_article,louer_article)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Toast.makeText(getContext(),"article ajouté",Toast.LENGTH_SHORT).show();
                            System.out.println("article ajouté");
                    }
                })
        );

    }

}
