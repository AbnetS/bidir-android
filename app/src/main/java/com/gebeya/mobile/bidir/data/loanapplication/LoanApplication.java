package com.gebeya.mobile.bidir.data.loanapplication;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;

/**
 * Model class for representing a single Loan Application.
 */
@Entity
public class LoanApplication {

    @Id
    public long id;

    @Index
    public String _id;
    public String clientId;
    public String status;
    public String title;
    public String type;
}