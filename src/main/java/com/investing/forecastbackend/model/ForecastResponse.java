package com.investing.forecastbackend.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

// TODO Model for the response for investment forecast
@Data
@Getter
@Setter
public class ForecastResponse {
    private List<Double> response;

    public void setResponse(List<Double> result) {
        this.response = result;
    }

    public List<Double> getResponse() {
        return response;
    }
}
