package com.gebeya.mobile.bidir.data.cashflow;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;

/**
 * Model class for representing a single cash flow.
 */

@Entity
public class CashFlow {

    @Id
    public long id;

    @Index
    public String referenceId;

    //@Index
    //public String cashFlowType; //replaced by the following attribute to use consistent name

    @Index
    public String type; //determines whether the cash flow is being built on estimated or actual values

    @Index
    public String classification; //determines the classification of the cash flow (net, cumulative etc)

    public double january;
    public double february;
    public double march;
    public double april;
    public double may;
    public double june;
    public double july;
    public double august;
    public double september;
    public double october;
    public double november;
    public double december;
}
