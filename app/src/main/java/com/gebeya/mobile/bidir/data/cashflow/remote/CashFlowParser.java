package com.gebeya.mobile.bidir.data.cashflow.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.cashflow.CashFlow;
import com.google.gson.JsonObject;

/**
 * Contract class for  {@link CashFlow} parser.
 */

public interface CashFlowParser {
    /**
     * Parse the given JsonObject and return a {@link CashFlow} object.
     *
     * @param object JsonObject to parseResponse from.
     * @return Parsed CashFlow Item object.
     * @throws Exception thrown if there was an error during the parsing.
     */
    CashFlow parse(@NonNull JsonObject object, @NonNull String referenceId, @NonNull String type,
                   @NonNull String classification) throws Exception;
}
