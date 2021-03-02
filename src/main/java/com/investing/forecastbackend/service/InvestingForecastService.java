package com.investing.forecastbackend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.investing.forecastbackend.model.ForecastRequest;
import com.investing.forecastbackend.model.ForecastResponse;
import com.investing.forecastbackend.model.InvestmentDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
@RequiredArgsConstructor
public class InvestingForecastService {

    public List<InvestmentDetail> getInvestmentOptions() throws IOException {
        // TODO read investment options from investment-details.json
        ObjectMapper objectMapper = new ObjectMapper();

        ArrayList read = (ArrayList) objectMapper.readValue(Paths
                .get("src/main/resources/data/investment-details.json")
                .toFile(), Map.class).get("Investments");
        System.out.println("read is " + read);

        String str = objectMapper.writeValueAsString(read);
        return objectMapper.readValue(str, new TypeReference<List<InvestmentDetail>>() {
        });
    }


    public ForecastResponse getInvestmentOptions(final ForecastRequest request) throws IOException {
        List<InvestmentDetail> details = getInvestmentOptions();
        System.out.println("HELLO BELOW");
        System.out.println(request);
        // TODO write algorithm to calculate investment forecast from request configuration
        List<Double> result = getForeCast(request.getRequest(), details);
        ForecastResponse response = new ForecastResponse();
        response.setResponse(result);
        return response;
    }

    public List<Double> getForeCast(Map<String, Double> userRequest, List<InvestmentDetail> details) {
        Map<Integer, Double> totalYearAmount = new HashMap<>();
        System.out.println(userRequest);

        Map<Integer, Double> returnForYear = new HashMap<Integer, Double>();

        for (InvestmentDetail i : details) {
            List<String> data = i.getData();
            double total = 0;
            for (int year = 0; year < data.size(); year++) {
                double weight = 0;
                if (year < 2) {
                    weight = 0.05;
                }
                else if (year < 8) {
                    weight = 0.1;
                }
                else {
                    weight = 0.15;
                }
                total += weight * Double.valueOf(data.get(year));
            }
            System.out.println(total);
            ArrayList<Double> newData = new ArrayList<>();
            newData.add(total);
            double currentInterest = total;
            for (int year = 1; year < 10; year++) {
                double percentDiff = (Double.valueOf(data.get(year)) -  Double.valueOf(data.get(year-1)))/Double.valueOf(data.get(year-1));
                currentInterest = currentInterest + currentInterest*percentDiff;
                newData.add(currentInterest);
            }
            System.out.println(newData);


            double userInvestmentPercentage = userRequest.get(i.getCategory());
            double userInvestmentDollars = (userInvestmentPercentage/100) * 10000;
            for (int year = 0; year < 10; year++) {
                double historicalReturn = newData.get(year);
                userInvestmentDollars = (historicalReturn/100)*userInvestmentDollars + userInvestmentDollars;

                Double currentYearTotal = returnForYear.getOrDefault(year, 0.0);
                returnForYear.put(year, currentYearTotal + userInvestmentDollars);
            }
        }
        return new ArrayList<>(returnForYear.values());
        

        // for (InvestmentDetail i : details) {
        //     //user input for category i
        //     // implement trend as well as weighted average 
        //     // 95% confidence interval 
        //     // std dev, subtract from most recent data point 
        //     // if cycles in data 
        //     // totalYearAmount is map: year, amount of total return
        //     System.out.println(i.getCategory());
        //     double userInvestmentPercentage = userRequest.get(i.getCategory());
        //     double userInvestmentDollars = (userInvestmentPercentage / 100) * 10000;
        //     for (int x = 0; x < 10; x++) {

        //         System.out.println("here: " + i.getData());
        //         //historical interest data for category i in year x
        //         double historicalInterest = Double.valueOf(i.getData().get(x));
        //         double currentInterest = (historicalInterest / 100) * userInvestmentDollars;

        //         userInvestmentDollars = userInvestmentDollars + currentInterest;

        //         Double currentYearTotal = totalYearAmount.getOrDefault(x, 0.0);
        //         //add total amount for category i in year x in Map<Integer, Double> totalYearAmount
        //         //continuously sum total for each investment i in year x
        //         totalYearAmount.put(x, currentYearTotal + userInvestmentDollars);
        //     }
        // }
        // System.out.println(totalYearAmount);
        // return new ArrayList<>(totalYearAmount.values());
    }
}

