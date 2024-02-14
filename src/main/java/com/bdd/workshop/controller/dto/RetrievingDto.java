package com.bdd.workshop.controller.dto;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RetrievingDto {
    public RetrievingDto() {
        data = null;
    }
    public RetrievingDto(List<ReceptionDto> data) {
        this.data = data;
    }

    private List<ReceptionDto> data;

    public List<ReceptionDto> getData() {
        return data;
    }

    public void setData(List<ReceptionDto> data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        RetrievingDto that = (RetrievingDto) object;
        return Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }

    @Override
    public String toString() {
        return "RetrievingDto{" +
            "data=" + data +
            '}';
    }
}
