package com.gebeya.apps.framework.base;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gebeya.apps.framework.util.Loggable;

/**
 * Base {@link DialogFragment} class that is used from the framework level.
 *
 * <br />
 * This class automatically logs its lifecycle and has helpful UI retrieval methods.
 */
public abstract class AppBaseDialog extends DialogFragment implements Loggable {

    protected static Typeface normal;

    protected View root;
    protected AlertDialog.Builder builder;

    /**
     * Build and create/return the <code>AlertDialog</code> all in one step.
     */
    protected AlertDialog createDialog() {
        if (builder == null) {
            final Activity activity = getActivity();
            if (activity == null) throw new NullPointerException("Activity cannot be null");

            builder = new AlertDialog.Builder(activity);
        }
        builder.setView(root);
        return builder.create();
    }

    /**
     * Inflate the root <code>View</code> from the parent's {@link LayoutInflater}
     */
    protected void inflateRoot(int id) {
        root = getInflater().inflate(id, null);
    }

    /**
     * Retrieve the parent <code>Activity</code>'s LayoutInflater
     */
    protected LayoutInflater getInflater() {
        final Activity activity = getActivity();
        if (activity == null) throw new NullPointerException("Activity cannot be null");

        return activity.getLayoutInflater();
    }

    /**
     * Retrieve the parent <code>Activity</code>.
     */
    public AppBaseActivity getParent() {
        return (AppBaseActivity) getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        d("< -------------------- onCreate(Bundle) -------------------- >");
    }

    @Override
    public void onStart() {
        super.onStart();
        d("< -------------------- onStart() -------------------- >");
    }

    @Override
    public void onResume() {
        super.onResume();
        d("< -------------------- onResume() -------------------- >");
    }

    @Override
    public void onPause() {
        d("< -------------------- onPause() -------------------- >");
        super.onPause();
    }

    @Override
    public void onStop() {
        d("< -------------------- onStop() -------------------- >");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        d("< -------------------- onDestroy() -------------------- >");
        super.onDestroy();
    }

    /**
     * Helper for the View.findViewById(int) method (because laziness is good!).
     */
    protected <T extends View> T getView(int id) {
        return root.findViewById(id);
    }

    /**
     * Retrieves a <code>TextView</code> with the default normal <code>Typeface</code> font applied.
     */
    protected TextView getTv(int id) {
        return getTv(id, normal);
    }

    /**
     * Retrieves a <code>TextView</code> with the provided <code>Typeface</code> font.
     */
    protected TextView getTv(int id, Typeface font) {
        final TextView tv = root.findViewById(id);
        if (tv != null) {
            tv.setTypeface(font);
        }
        return tv;
    }

    /**
     * Retrieves a <code>TextView</code> with the default normal <code>Typeface</code> font and <code>View</code>
     * as the root to search the view on.
     */
    public static TextView getTv(int id, View root) {
        return getTv(id, normal, root);
    }

    /**
     * Retrieves a <code>TextView</code> with the provided <code>Typeface</code> font and <code>View</code>
     * as the root to search the view on.
     */
    public static TextView getTv(int id, Typeface font, View root) {
        final TextView textView = root.findViewById(id);
        if (textView != null) {
            textView.setTypeface(font);
        }
        return textView;
    }

    /**
     * Retrieves a <code>EditText</code> with the default normal font.
     */
    protected EditText getEd(int id) {
        return getEd(id, normal);
    }

    /**
     * Retrieves a <code>EditText</code> with the provided font.
     */
    protected EditText getEd(int id, Typeface font) {
        final EditText editText = root.findViewById(id);
        if (editText != null) {
            editText.setTypeface(font);
        }
        return editText;
    }

    /**
     * Retrieves a <code>EditText</code> with the normal font, searching on the given <code>View</code>
     * as the root.
     */
    public static EditText getEd(int id, View root) {
        return getEd(id, normal, root);
    }

    /**
     * Retrieves a <code>EditText</code> with the provided font, searching on the given <code>View</code>
     * as the root.
     */
    public static EditText getEd(int id, Typeface font, View root) {
        final EditText editText = root.findViewById(id);
        if (editText != null) {
            editText.setTypeface(font);
        }
        return editText;
    }

    /**
     * Retrieves a <code>Button</code> with the normal font, searching on the given <code>View</code>
     * as the root.
     */
    protected Button getBt(int id) {
        return getBt(id, normal);
    }

    /**
     * Retrieves a <code>Button</code> with a default font.
     */
    protected Button getBt(int id, Typeface font) {
        final Button button = root.findViewById(id);
        if (button != null) {
            button.setTypeface(font);
        }
        return button;
    }

    /**
     * Retrieves a <code>Button</code> with a default font and the <code>View</code> as the search
     * root.
     */
    public static Button getBt(int id, View root) {
        return getBt(id, normal, root);
    }

    /**
     * Retrieves a <code>Button</code> with the given font and the <code>View</code> as the search
     * root.
     */
    public static Button getBt(int id, Typeface font, View root) {
        final Button button = root.findViewById(id);
        if (button != null) {
            button.setTypeface(font);
        }
        return button;
    }

    /**
     * Show a simple {@link Toast} <code>String ResId</code> message resource, with a default
     * {@link Toast#LENGTH_SHORT} length.
     */
    public void toast(int resId) {
        toast(getString(resId));
    }

    /**
     * Show a simple {@link Toast} <code>String</code> message with a default
     * {@link Toast#LENGTH_SHORT} length.
     */
    public void toast(String message) {
        toast(message, Toast.LENGTH_SHORT);
    }

    /**
     * Show a simple {@link Toast} <code>String ResId</code> message resource, with the given
     * duration length.
     */
    public void toast(int resId, int duration) {
        toast(getString(resId), duration);
    }

    /**
     * Show a simple {@link Toast} <code>String</code> message, with the given duration length.
     */
    public void toast(String message, int duration) {
        getParent().toast(message, duration);
    }
}
