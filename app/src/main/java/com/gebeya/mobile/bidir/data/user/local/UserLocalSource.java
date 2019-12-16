package com.gebeya.mobile.bidir.data.user.local;

import com.gebeya.mobile.bidir.data.base.local.crud.base.DeleteMany;
import com.gebeya.mobile.bidir.data.base.local.crud.base.ReadOne;
import com.gebeya.mobile.bidir.data.base.local.crud.base.WriteOne;
import com.gebeya.mobile.bidir.data.user.User;

/**
 * Local interface definition contract for the User.
 */
public interface UserLocalSource extends ReadOne<User>, WriteOne<User>, DeleteMany {

}