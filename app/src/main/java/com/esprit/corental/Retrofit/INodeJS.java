package com.esprit.corental.Retrofit;


import com.esprit.corental.Entities.Participants;
import com.esprit.corental.Entities.Article;
import com.esprit.corental.Entities.User;
import com.esprit.corental.Entities.Evenement;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface INodeJS {
    @POST("register")
    @FormUrlEncoded
    Observable<String> registerUser (@Field("email") String email,
                                     @Field("name") String name,
                                     @Field("prenom") String prenom,
                                     @Field("tel_user") String tel_user,
                                     @Field("password") String password);
    @POST("login")
    @FormUrlEncoded
    Observable<String> loginUser (@Field("email") String email,
                                  @Field("password") String password);

    @GET("/user/{email}")
    Call<User> getUser(@Path("email") String email);
    @GET("/GetUE/{id_user}")
    Call<User> getUserE(@Path("id_user") int id_evenement);

    @GET("/GetEvenementUser/{id_user}")
    Call<List<Evenement>> getEvenemenetUser(@Path("id_user") int id_user);


    @POST("/evenement/add")
    @FormUrlEncoded
    Observable<String> addEvenement (@Field("nom_evenement") String nom_evenement,
                                     @Field("type_evenement") String type_evenement,
                                     @Field("date_debut_evenement") String date_debut_evenement,
                                     @Field("date_fin_evenement") String date_fin_evenement,
                                     @Field("distance_evenement") int distance_evenement,
                                     @Field("lieux_evenement") String lieux_evenement,
                                     @Field("infoline") int infoline,
                                     @Field("difficulte_evenement") int difficulte_evenement,
                                     @Field("prix_evenement") int prix_evenement,
                                     @Field("id_user") int id_user,
                                     @Field("description_evenement") String description_evenement,
                                     @Field("nbplace_evenement") int nbplace_evenement);



    //update evenement
    @PUT("/annuler/{id}")
    @FormUrlEncoded
    Call<Evenement> updateNbrPlace (@Path("id") int id,
                                    @Field("nbplace_evenement") int nbplace_evenement);




    @PUT("/UpdateEvenement/{id_evenement}")
    @FormUrlEncoded
    Call<Evenement> updateEvenement (@Path("id_evenement") int id_evenement,
                                     @Field("nom_evenement") String nom_evenement,
                                     @Field("type_evenement") String type_evenement,
                                     @Field("date_debut_evenement") String date_debut_evenement,
                                     @Field("date_fin_evenement") String date_fin_evenement,
                                     @Field("distance_evenement") int distance_evenement,
                                     @Field("lieux_evenement") String lieux_evenement,
                                     @Field("infoline") int infoline,
                                     @Field("difficulte_evenement") int difficulte_evenement,
                                     @Field("prix_evenement") int prix_evenement,
                                     @Field("description_evenement") String description_evenement,
                                     @Field("nbplace_evenement") int nbplace_evenement);




    @GET("/GetArticleUser/{id_user}")
    Call<List<Article>> getArticleUser(@Path("id_user") int id_user);

    @Multipart
    @POST("/upload")
    Call<ResponseBody> postImage(@Part MultipartBody.Part image, @Part("upload") RequestBody name);

    @GET("/uploads/{upload}")
    Call<ResponseBody> getImage(@Path("upload") String n);


    //ajouter article
    @POST("/article/add")
    @FormUrlEncoded
    Observable<String> addArticle (@Field("titre_article") String titre_article,
                                   @Field("description_article") String description_article,
                                   @Field("location_article") String location_article,

                                   @Field("image_article") String image_article,
                                   @Field("prix_article") int prix_article,
                                   @Field("user_id") int user_id,
                                   @Field("vendre_article") String vendre_article,
                                   @Field("louer_article") String louer_article);
    //afficher articles
    @GET("/GetArticle/")
    Call<List<Article>> getArticleList();

    @GET("/GetEvents/")
    Call<List<Evenement>> getEventsList();

    @PUT("/UpdateProfil/{id}")
    @FormUrlEncoded
    Call<User> updateProfile (@Path("id") int id,
                              @Field("name") String type,
                              @Field("prenom") String model);







    //ajouter participant
    @POST("/participant/add")
    @FormUrlEncoded
    Observable<String> addParticipant (@Field("id_user") int id_user,
                                       @Field("id_evenement") int id_evenement);

    @GET("/GetParticipants/")
    Call<List<Participants>> getParticipantList();


}
