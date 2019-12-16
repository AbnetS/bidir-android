package com.gebeya.mobile.bidir.data.cropacat.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.cropacat.CropACAT;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * Created by abuti on 5/17/2018.
 */

public interface CropACATParser {

    String STATUS_LOCAL_NEW = "NEW";
    String STATUS_API_NEW = "new";

    String STATUS_LOCAL_IN_PROGRESS = "IN_PROGRESS";
    String STATUS_API_IN_PROGRESS = "inprogress";

    String STATUS_LOCAL_SUBMITTED = "SUBMITTED";
    String STATUS_API_SUBMITTED = "submitted";

    String STATUS_LOCAL_COMPLETED = "COMPLETE";
    String STATUS_API_COMPLETED = "complete";

    String STATUS_LOCAL_AUTHORIZED = "AUTHORIZED";
    String STATUS_API_AUTHORIZED = "authorized";

    String STATUS_LOCAL_DECLINED_FOR_REVIEW = "DECLINED_FOR_REVIEW";
    String STATUS_API_DECLINED_FOR_REVIEW = "declined_for_review";

    String STATUS_LOCAL_LOAN_PAID = "LOAN_PAID";
    String STATUS_API_LOAN_PAID = "loan_paid";

    String STATUS_UNKNOWN = "STATUS_UNKNOWN";


    /**
     * Parse the given JsonObject and return a {@link com.gebeya.mobile.bidir.data.groupedlist.GroupedList} object.
     *
     * @param object JsonObject to parseResponse from.
     * @param acatApplciationID the client acat Id to which the crop ACAT belongs to
     * @return Parsed CropACAT object.
     * @throws Exception thrown if there was an error during the parsing.
     */
    CropACAT parse(@NonNull JsonObject object, @NonNull String acatApplciationID) throws Exception;

    List<String> toList(@NonNull JsonArray array);

    String getLocalStatus(@NonNull String apiStatus);

    String getApiStatus(@NonNull String localStatus);
}
