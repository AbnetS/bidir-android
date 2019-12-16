package com.gebeya.mobile.bidir.data.section.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestionRequest;
import com.gebeya.mobile.bidir.data.section.Section;
import com.gebeya.mobile.bidir.data.section.SectionRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete implementation for the {@link SectionParser} parser.
 */
public class BaseSectionParser implements SectionParser {

    @Override
    public Section parse(@NonNull JsonObject object, @NonNull String referenceId, @NonNull String referenceType) throws Exception {
        try {
            final Section section = new Section();

            section._id = object.get("_id").getAsString();
            section.referenceId = referenceId;
            section.referenceType = referenceType;
            section.questionIds = getQuestionIds(object.get("questions").getAsJsonArray());
            section.number = object.get("number").getAsInt();
            section.title = object.get("title").getAsString();
            section.createdAt = new DateTime(object.get("date_created").getAsString());
            section.updatedAt = new DateTime(object.get("last_modified").getAsString());

            return section;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error parsing Section: " + e.getMessage());
        }
    }

    @Override
    public List<String> getQuestionIds(@NonNull JsonArray array) {
        final List<String> ids = new ArrayList<>();
        final int length = array.size();
        for (int i = 0; i <length; i++) {
            final JsonObject object = array.get(i).getAsJsonObject();
            final String id = object.get("_id").getAsString();
            ids.add(id);
        }
        return ids;
    }
}
