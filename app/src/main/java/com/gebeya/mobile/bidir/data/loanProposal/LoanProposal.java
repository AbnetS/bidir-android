package com.gebeya.mobile.bidir.data.loanProposal;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;

/**
 * Object box model for representing Client's loan proposal info.
 *
 * samkura47@gmail.com
 */

@Entity
public class LoanProposal {

    @Id
    public long id;

    @Index
    public String _id;

    @Index
    public String clientId;

    @Index
    public String clientACATId;

    public double loanProposed;
    public double loanApproved;
    public double loanRequested;
    public double totalCost;
    public double totalRevenue;
    public double repayable;
    public double totalDeductible;
    public double totalCostOfLoan;
    public double cashAtHand;
    public double maxAmount;

    public boolean loanGranted;

    public String status;
}
