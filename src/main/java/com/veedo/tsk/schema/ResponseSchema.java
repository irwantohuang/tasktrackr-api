package com.veedo.tsk.schema;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseSchema {

    @JsonProperty("error_schema")
    public ErrorSchema errorSchema;

    @JsonProperty("output_schema")
    public Object outputSchema;

    public ResponseSchema(ErrorSchema errorSchema) {
        this.errorSchema = errorSchema;
    }

    public ResponseSchema(Object outputSchema) {
        this.errorSchema = new ErrorSchema("TSK-00-000", "Berhasil", "Success");
        this.outputSchema = outputSchema;
    }

}
