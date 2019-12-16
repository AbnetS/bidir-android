package com.gebeya.mobile.bidir.ui.questions.acat.acatcostestimation;

/**
 * Created by abuti on 6/4/2018.
 */

import com.gebeya.mobile.bidir.data.acatitemvalue.ACATItemValue;
import com.gebeya.mobile.bidir.data.acatitemvalue.local.ACATItemValueLocalSource;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import javax.inject.Inject;




public class ACATUtilities {
    @Inject static ACATItemValueLocalSource acatItemValueLocalSource;

    public ACATUtilities(){
        Tooth.inject(this, Scopes.SCOPE_REMOTE_SOURCES);
    }

    public static double computeTotalPrice(double unitPrice, double quantity){
        return (unitPrice * quantity);
    }

//    public static boolean updateCostItemValues(ACATItemValue acatItemValue, String remark){
//        //Save the value on the acatItemValue
//        acatItemValueLocalSource.put(acatItemValue)
//                .flatMap(acatItemValueData ->
//                            acatItemValueData.
//
//
//
//
//
//    }

    public static String returnSectionId(String costItemId){
        return null;

    }

    public static boolean updateCostItemTotPrice(String costItemId, double totalPrice, double sectionSubTot){
        return false;
    }
}
