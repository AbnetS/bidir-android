package com.gebeya.mobile.bidir.data.acatform.remote;

import com.gebeya.mobile.bidir.data.acatcostsection.ACATCostSectionResponse;
import com.gebeya.mobile.bidir.data.acatform.ACATForm;
import com.gebeya.mobile.bidir.data.acatrevenuesection.ACATRevenueSectionResponse;
import com.gebeya.mobile.bidir.data.cashflow.CashFlow;
import com.gebeya.mobile.bidir.data.crop.Crop;


/**
 * Class for representing the ACAT Form pageResponse, that involves both the form and sections.
 */

public class ACATFormResponse {

    public ACATForm acatForm;

    public CashFlow estimatedNetCashFlow;
    public CashFlow actualNetCashFlow;

    public ACATCostSectionResponse costSection;

    //I am thinking the above may replace this one if the above parsing convince you
    //public List<ACATSection> sections;

    //Will be changed
    public ACATRevenueSectionResponse revenueSection;

    public Crop crop;





}
