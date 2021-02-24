package com.investing.forecastbackend.model;

// TODO Model the data read from ../resources/data/investment-details.json
public class InvestmentDetail {
    String category;
    int minimum;
    double[] data;

    public InvestmentDetail(String category, String minimum, String[] data) {
        this.category = category;
        this.minimum = Integer.parseInt(minimum);
        this.data = new double[10];
        for (int i = 0; i < data.length; i++) {
            this.data[i] = Double.parseDouble(data[i]);
        }
    }
}
