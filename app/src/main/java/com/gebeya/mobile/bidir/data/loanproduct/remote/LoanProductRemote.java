package com.gebeya.mobile.bidir.data.loanproduct.remote;

import com.gebeya.mobile.bidir.data.base.remote.BaseRemoteSource;
import com.gebeya.mobile.bidir.data.base.remote.backend.Service;
import com.gebeya.mobile.bidir.data.loanproduct.LoanProductResponse;
import com.gebeya.mobile.bidir.data.loanproductitem.LoanProductItem;
import com.gebeya.mobile.bidir.data.loanproductitem.remote.LoanProductItemParser;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;

/**
 * Created by abuti on 5/7/2018.
 */

public class LoanProductRemote extends BaseRemoteSource<LoanProductService>  implements LoanProductRemoteSource {

    @Inject
    LoanProductParser loanProductParser;
    @Inject
    LoanProductItemParser loanProductItemParser;

    public LoanProductRemote() {
        Tooth.inject(this,Scopes.SCOPE_REMOTE_SOURCES);
        setParams(Service.LOAN_PRODUCT, LoanProductService.class);
    }

    @Override
    public Observable<List<LoanProductResponse>> getAll(){
        return build().call(service.getAll())
                .map(object -> {
                    final List<LoanProductResponse> responses = new ArrayList<>();
                    final JsonArray docs = object.getAsJsonArray("docs");
                    final int size = docs.size();

                    for (int i = 0; i < size; i++) {
                        JsonObject loanProductObject = docs.get(i).getAsJsonObject();
                        LoanProductResponse response = new LoanProductResponse();
                        response.loanProduct = loanProductParser.parse(loanProductObject);
                        response.deductibles = parseLoanProdItemList(
                                loanProductObject.getAsJsonArray("deductibles"),
                                LoanProductItemParser.CATEGORY_DEDUCTIBLES
                        );
                        response.costOfLoan = parseLoanProdItemList(
                                loanProductObject.getAsJsonArray("cost_of_loan"),
                                LoanProductItemParser.CATEGORY_COST_OF_LOAN
                        );

                        responses.add(response);
                    }
                    return responses;
                });
    }

    @Override
    public List<LoanProductItem> parseLoanProdItemList(@NonNull JsonArray array, @NonNull String category) throws Exception{
        try {
            final List<LoanProductItem> loanProductItems = new ArrayList<>();
            final int length = array.size();
            for (int i = 0; i < length; i++) {
                final JsonObject object = array.get(i).getAsJsonObject();
                final LoanProductItem loanProductItem = loanProductItemParser.parse(object, category);
                loanProductItems.add(loanProductItem);
            }
            return loanProductItems;
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("Error parsing Deductibles/Cost of Loan List: " + e.getMessage());
        }

    }
}
