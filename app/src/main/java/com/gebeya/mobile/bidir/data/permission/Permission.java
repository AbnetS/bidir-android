package com.gebeya.mobile.bidir.data.permission;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;

/**
 * Model class for representing a single permission
 *
 * Created by Samuel K. on 12/20/2017.
 * <p>
 * samkura47@gmail.com
 */
@Entity
public class Permission {

    @Id
    public long id;

    @Index
    public String _id;
    public String entityModule;
    public String operation;
}