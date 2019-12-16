package com.gebeya.mobile.bidir.ui.register;

import android.support.annotation.Nullable;

import java.io.File;

public class RegisterPicturesData {
    @Nullable public File personalPictureFile;
    @Nullable public File idCardPictureFile;

    public void reset() {
        personalPictureFile = null;
        idCardPictureFile = null;
    }
}
