package com.gebeya.mobile.bidir.data.client.remote;

import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.impl.util.Constants;
import com.google.gson.JsonObject;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Client service retrofit contract used for registration and retrieval of clients
 */
public interface ClientService {

    @Multipart
    @POST("create")
    Observable<JsonObject> register(
            @Part("first_name") RequestBody name,
            @Part("last_name") RequestBody fatherName,
            @Part("grandfather_name") RequestBody grandfatherName,
            @Part("gender") RequestBody gender,
            @Part("national_id_no") RequestBody nationalIdNo,
            @Part("date_of_birth") RequestBody dateOfBirth,
            @Part("civil_status") RequestBody maritalStatus,
            @Part("woreda") RequestBody woreda,
            @Part("kebele") RequestBody kebele,
            @Part("house_no") RequestBody houseNo,
            @Part("phone") RequestBody phone,
            @Part("household_members_count") RequestBody householdMemberCount,
            @Part("branch") RequestBody branch,
            @Part("created_by") RequestBody createdBy,
            @Part("spouse") RequestBody spouse,
            @Part("geolocation") RequestBody geolocation,
            @Part("national_id_card\"; filename=\"national_id_card.jpg") RequestBody nationalIdFile,
            @Part("picture\"; filename=\"picture.jpg") RequestBody userPhotoFile,
            @Part("for_group") RequestBody forGroup
    );

    @PUT("{id}")
    Observable<JsonObject> update(@Path("id") String clientId, @Body JsonObject request);

    @GET(Constants.PAGINATE_URL_SOURCE)
    Observable<JsonObject> getAll();

    @GET("{id}")
    Observable<JsonObject> getOne(@Path("id") String clientId);

    @GET("{id}")
    Observable<JsonObject> getClientLoanHistory(@Path("id") String clientId);
}