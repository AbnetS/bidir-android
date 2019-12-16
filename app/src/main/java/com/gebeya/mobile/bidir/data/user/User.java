package com.gebeya.mobile.bidir.data.user;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;

/**
 * User model used both by ObjectBox and Retrofit.
 */
@Entity
public class User {
    /**
     * Used by ObjectBox as an internal ID value (must remain a long value for better performance)
     */
    @Id
    public long id;

    /**
     * _id value returned by MongoDB from the API. We need this for queries.
     */
    @Index
    public String _id;
    public String token;
    public String email;
    public String username;
    public String phoneNumber;
    public String firstName;
    public String lastName;
    public String status;
    public String branchId;
}