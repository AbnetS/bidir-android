package com.gebeya.mobile.bidir.data.prerequisite;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;

/**
 * ObjectBox model for a single {@link com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestion}
 * prerequisite object.
 */
@Entity
public class Prerequisite {

    @Id
    public long id;

    @Index
    public String _id;

    @Index
    public String questionId;

    @Index
    public String parentQuestionId;

    public String answer;
}