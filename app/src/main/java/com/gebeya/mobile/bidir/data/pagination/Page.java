package com.gebeya.mobile.bidir.data.pagination;

import com.gebeya.mobile.bidir.data.base.local.ServiceTypeConverter;
import com.gebeya.mobile.bidir.data.base.remote.backend.Service;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;

/**
 * Model that represents the properties of an endpoint, in terms of pagination. This class is used
 * by remote classes (if necessary) in order to download data in a paginated form.
 */
@Entity
public class Page {

    @Id
    public long id;

    @Index
    @Convert(converter = ServiceTypeConverter.class, dbType = String.class)
    public Service type;

    public int totalPages;
    public int currentPage;
}
