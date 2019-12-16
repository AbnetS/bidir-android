package com.gebeya.mobile.bidir.data.groupedcomplexloan.remote;

import android.support.annotation.NonNull;
import android.widget.Toolbar;

import com.gebeya.mobile.bidir.data.complexloanapplication.ComplexLoanApplicationResponse;
import com.gebeya.mobile.bidir.data.complexloanapplication.remote.ComplexLoanApplicationParser;
import com.gebeya.mobile.bidir.data.complexloanapplication.remote.ComplexLoanApplicationRemote;
import com.gebeya.mobile.bidir.data.complexloanapplication.remote.ComplexLoanApplicationRemoteSource;
import com.gebeya.mobile.bidir.data.complexloanapplication.repo.ComplexLoanApplicationRepositorySource;
import com.gebeya.mobile.bidir.data.groupedcomplexloan.GroupedComplexLoan;
import com.gebeya.mobile.bidir.data.groupedcomplexscreening.GroupedComplexScreening;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.joda.time.DateTime;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Scope;

public class BaseGroupComplexLoanParser implements GroupedComplexLoanParser{

    @Inject ComplexLoanApplicationParser loanApplicationParser;


    @Inject
    public BaseGroupComplexLoanParser() {
        Tooth.inject(this, Scopes.SCOPE_LOCAL_SOURCES);
    }

    @Override
    public GroupedComplexLoan parse(@NonNull JsonObject object) throws Exception {
        try {
            final GroupedComplexLoan loan = new GroupedComplexLoan();

            loan._id = object.get("_id").getAsString();

            JsonObject groupObject = object.get("group").getAsJsonObject();
            loan.groupId = groupObject.get("_id").getAsString();
            JsonArray memberIds = groupObject.get("members").getAsJsonArray();

            loan.membersId = new ArrayList<>();
            for (int i = 0; i < memberIds.size(); i++) {
                loan.membersId.add(memberIds.get(i).getAsString());
            }

            String apiStatus = object.get("status").getAsString();
            loan.status = getLocalStatus(apiStatus);

            JsonArray loanIds = object.get("loans").getAsJsonArray();
            for (int i = 0; i < loanIds.size(); i++) {
                JsonObject loanObject = loanIds.get(i).getAsJsonObject();

                loan.complexLoans.add(loanApplicationParser.parse(loanObject));
            }

            final String createdAt = object.get("date_created").getAsString();
            loan.createdAt = new DateTime(createdAt);
            final String updatedAt = object.get("last_modified").getAsString();
            loan.updatedAt = new DateTime(updatedAt);

            return loan;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error parsing grouped Loan: " + e.getMessage());
        }
    }

    @Override
    public String getLocalStatus(@NonNull String apiStatus) {
        return getStatus(apiStatus, false);
    }

    @Override
    public String getApiStatus(@NonNull String localStatus) {
        return getStatus(localStatus, true);
    }

    private String getStatus(@NonNull String value, boolean localValue) {
        switch (value) {
            case STATUS_LOCAL_NEW:
            case STATUS_API_NEW:
                return localValue ? STATUS_API_NEW : STATUS_LOCAL_NEW;
            case STATUS_LOCAL_IN_PROGRESS:
            case STATUS_API_IN_PROGRESS:
                return localValue ? STATUS_API_IN_PROGRESS : STATUS_LOCAL_IN_PROGRESS;
            case STATUS_LOCAL_SUBMITTED:
            case STATUS_API_SUBMITTED:
                return localValue ? STATUS_API_SUBMITTED : STATUS_LOCAL_SUBMITTED;
            case STATUS_LOCAL_ACCEPTED:
            case STATUS_API_ACCEPTED:
                return localValue ? STATUS_API_ACCEPTED : STATUS_LOCAL_ACCEPTED;
            case STATUS_LOCAL_DECLINED_FINAL:
            case STATUS_API_DECLINED_FINAL:
                return localValue ? STATUS_API_DECLINED_FINAL : STATUS_LOCAL_DECLINED_FINAL;
            case STATUS_LOCAL_DECLINED_UNDER_REVIEW:
            case STATUS_API_DECLINED_UNDER_REVIEW:
                return localValue ? STATUS_API_DECLINED_UNDER_REVIEW : STATUS_LOCAL_DECLINED_UNDER_REVIEW;
            default:
                return STATUS_UNKNOWN;
        }
    }
}
