package com.gebeya.mobile.bidir.data.marketdetails;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;

/**
 * Created by abuti on 5/15/2018.
 */

@Entity
public class MarketDetails {
    @Id
    public long id;

   @Index
   public String yieldConsumptionID;

   public String type; //to indicate whether it is ESTIMATED or ACTUAL

    public String toWhom;
    public double amount;

    //doable, but writing the logic might take some time
        //public double estimatedAmount;
        //public double actualAmount;
    public String remark;

}
