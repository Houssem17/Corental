package com.esprit.corental.Controllers;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.esprit.corental.R;
import com.esprit.corental.Retrofit.INodeJS;
import com.esprit.corental.Retrofit.RetrofitClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class AjouterEvenement extends Fragment implements View.OnClickListener {

        INodeJS myAPI;
        SeekBar slider;
        TextView diffV;
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        EditText nom,type,distance,lieu,prix,contact,datedebut,datefin,camp_desc;
        Button add;
        SharedPreferences sharedPreferences;
        int id_user;
        DatePickerDialog.OnDateSetListener dateSetListener1,dateSetListener2;

//map
public static final String MY_PREFS_NAME2 = "CurrentUser";

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
private static final String[] cats = {"For Rental", "For Corental"};
        Spinner cat;






public AjouterEvenement() {

        }




@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_ajouter_evenement, container, false);
        sharedPreferences = getContext().getSharedPreferences("CurrentUser", Context.MODE_PRIVATE);
        id_user = sharedPreferences.getInt("idUser", 0);
        // System.out.println(idu+"");
        nom = rootView.findViewById(R.id.camp_nom);
        //type = rootView.findViewById(R.id.camp_type);
        cat = rootView.findViewById(R.id.cat);
        distance = rootView.findViewById(R.id.camp_distanse);
        lieu = rootView.findViewById(R.id.camp_lieu);
        slider = rootView.findViewById(R.id.slider);
        prix = rootView.findViewById(R.id.camp_prix);
        contact = rootView.findViewById(R.id.camp_contact);
        add = rootView.findViewById(R.id.addE);
        datedebut = rootView.findViewById(R.id.dateDebut);
        datefin = rootView.findViewById(R.id.dateFin);
        camp_desc = rootView.findViewById(R.id.camp_desc);
        diffV = rootView.findViewById(R.id.diffV);

        //map

        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME2, MODE_PRIVATE);

        String addresse = prefs.getString("addresse","");

        lieu.setText(addresse);

        //upload image
        fabCamera = rootView.findViewById(R.id.fab);
        fabUpload = rootView.findViewById(R.id.fabUpload);
        imageView = rootView.findViewById(R.id.imageView);
        textView = rootView.findViewById(R.id.textView);
        fabCamera.setOnClickListener(this);
        fabUpload.setOnClickListener(this);


        //slider
        slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
@Override
public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        diffV.setText(String.valueOf(slider.getProgress()));

        }

@Override
public void onStartTrackingTouch(SeekBar seekBar) {


        }

@Override
public void onStopTrackingTouch(SeekBar seekBar) {

        }
        });




        //spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(),  android.R.layout.simple_spinner_item, cats);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cat.setAdapter(adapter);



        //Init API
        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJS.class);

        add.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        System.out.println("okkkk");
        String ss = String.valueOf(imageView);
        System.out.println(ss);
        addEvenement(nom.getText().toString(),cat.getSelectedItem().toString(),datedebut.getText().toString(),datefin.getText().toString(),Integer.parseInt(distance.getText().toString()),
        lieu.getText().toString(),Integer.parseInt(contact.getText().toString()),slider.getProgress(),Integer.parseInt(prix.getText().toString()),id_user,camp_desc.getText().toString(),1);

        }
        });
        Calendar calendar=Calendar.getInstance();
final int year=calendar.get(Calendar.YEAR);
final int month=calendar.get(Calendar.MONTH);
final int day=calendar.get(Calendar.DAY_OF_MONTH);

        datedebut.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        DatePickerDialog datePickerDialog=new DatePickerDialog(getActivity(),
        dateSetListener1
        ,year,month,day);
        datePickerDialog.getWindow();
        datePickerDialog.show();
        }
        });
        dateSetListener1 =new DatePickerDialog.OnDateSetListener() {
@Override
public void onDateSet(DatePicker view, int year, int month, int day) {
        month +=1;
        String d=day + "/" + month + "/" + year;
        datedebut.setText(d);
        }
        };

        datefin.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        DatePickerDialog datePickerDialog=new DatePickerDialog(getActivity(),
        dateSetListener2
        ,year,month,day);
        datePickerDialog.getWindow();
        datePickerDialog.show();

        }
        });
        dateSetListener2 =new DatePickerDialog.OnDateSetListener() {
@Override
public void onDateSet(DatePicker view, int year, int month, int day) {
        month +=1;
        String date=day + "/" + month + "/" + year;
        datefin.setText(date);
        }
        };




        return rootView;
        }

private void addEvenement(final String nom_evenement, final String type_evenement, final String date_debut_evenement,  final String date_fin_evenement, final int camp_distanse,
final String camp_lieu, final int camp_contact, final int camp_difficulter, final int camp_prix
        ,int id_user, final String description_evenement, final int nbplace_evenement) {


        System.out.println(id_user);
        Retrofit.Builder builder = new Retrofit.Builder()
        .baseUrl("http://10.0.2.2:3000")
        .addConverterFactory(ScalarsConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        Retrofit retrofit = builder.build();
        myAPI = retrofit.create(INodeJS.class);

        compositeDisposable.add(myAPI.addEvenement(nom_evenement,type_evenement,date_debut_evenement,date_fin_evenement,camp_distanse,camp_lieu,camp_contact,camp_difficulter,camp_prix,id_user,description_evenement,nbplace_evenement)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<String>() {
@Override
public void accept(String s) throws Exception {
        Toast.makeText(getContext(),"evenement ajouté",Toast.LENGTH_SHORT).show();
        System.out.println("evenement ajouté");
        }
        })
        );

        }


//upload image
private void askPermissions() {
        permissions.add(CAMERA);
        permissions.add(WRITE_EXTERNAL_STORAGE);
        permissions.add(READ_EXTERNAL_STORAGE);
        permissionsToRequest = findUnAskedPermissions(permissions);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


        if (permissionsToRequest.size() > 0)
        requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
        }

private void initRetrofitClient() {
        OkHttpClient client = new OkHttpClient.Builder().build();

        myAPI = new Retrofit.Builder().baseUrl("http://10.0.2.2:3000").client(client).build().create(INodeJS.class);
        }


public Intent getPickImageChooserIntent() {

        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getActivity().getPackageManager();

        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
        Intent intent = new Intent(captureIntent);
        intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
        intent.setPackage(res.activityInfo.packageName);
        if (outputFileUri != null) {
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        }
        allIntents.add(intent);
        }

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
        Intent intent = new Intent(galleryIntent);
        intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
        intent.setPackage(res.activityInfo.packageName);
        allIntents.add(intent);
        }

        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
        if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
        mainIntent = intent;
        break;
        }
        }
        allIntents.remove(mainIntent);

        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));
        Log.d("testtttttttttttt", chooserIntent.toString());

        return chooserIntent;
        }


private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getActivity().getExternalFilesDir("");
        if (getImage != null) {
        outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        return outputFileUri;
        }

@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {


        if (requestCode == IMAGE_RESULT) {


        String filePath = getImageFilePath(data);
        if (filePath != null) {
        mBitmap = BitmapFactory.decodeFile(filePath);
        imageView.setImageBitmap(mBitmap);
        Log.d("image",filePath );
        }
        }

        }

        }


private String getImageFromFilePath(Intent data) {
        boolean isCamera = data == null || data.getData() == null;

        if (isCamera) return getCaptureImageOutputUri().getPath();

        else
        Log.d("haythem",getPathFromURI(data.getData()) );
        return getPathFromURI(data.getData());

        }

public String getImageFilePath(Intent data) {

        return getImageFromFilePath(data);
        }

private String getPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        Log.d("teeeeest", cursor.getString(column_index));
        return cursor.getString(column_index);

        }

@Override
public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("pic_uri", picUri);
        }

/*    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        picUri = savedInstanceState.getParcelable("pic_uri");
    }*/

private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
        if (!hasPermission(perm)) {
        result.add(perm);
        }
        }

        return result;
        }

private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        return (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED);
        }
        }
        return true;
        }

private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity());

        }

private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
        }

@TargetApi(Build.VERSION_CODES.M)
@Override
public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

        case ALL_PERMISSIONS_RESULT:
        for (String perms : permissionsToRequest) {
        if (!hasPermission(perms)) {
        permissionsRejected.add(perms);
        }
        }

        if (permissionsRejected.size() > 0) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
        showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
        new DialogInterface.OnClickListener() {
@Override
public void onClick(DialogInterface dialog, int which) {
        requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
        }
        });
        return;
        }
        }

        }

        break;
        }

        }

private void multipartImageUpload() {
        try {
        File filesDir = getActivity().getFilesDir();
        File file = new File(filesDir, "image" + ".png");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
        byte[] bitmapdata = bos.toByteArray();


        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bitmapdata);
        fos.flush();
        fos.close();


        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part image = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload");
        Retrofit.Builder builder = new Retrofit.Builder()
        .baseUrl("http://10.0.2.2:3000")
        .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        myAPI = retrofit.create(INodeJS.class);
        Call<ResponseBody> req = myAPI.postImage(image, name);
        req.enqueue(new Callback<ResponseBody>() {
@Override
public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

        if (response.code() == 200) {
        textView.setText("Uploaded Successfully!");
        textView.setTextColor(Color.BLUE);
        }

        Toast.makeText(getActivity(), response.code() + " ", Toast.LENGTH_SHORT).show();
        }

@Override
public void onFailure(Call<ResponseBody> call, Throwable t) {
        textView.setText("Uploaded Failed!");
        textView.setTextColor(Color.RED);
        Toast.makeText(getActivity(), "Request failed", Toast.LENGTH_SHORT).show();
        t.printStackTrace();
        }
        });


        } catch (FileNotFoundException e) {
        e.printStackTrace();
        } catch (IOException e) {
        e.printStackTrace();
        }
        }

@Override
public void onClick(View view) {
        switch (view.getId()) {
        case R.id.fab:
        startActivityForResult(getPickImageChooserIntent(), IMAGE_RESULT);
        break;

        case R.id.fabUpload:
        if (mBitmap != null)
        multipartImageUpload();
        else {
        Toast.makeText(view.getContext(), "Bitmap is null. Try again", Toast.LENGTH_SHORT).show();
        }
        break;
        }

        }

public static AjouterEvenement newInstance() {

final AjouterEvenement fragment = new AjouterEvenement();


        return fragment;
        }

        }
