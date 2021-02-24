package com.investing.forecastbackend.service;

import com.investing.forecastbackend.model.ForecastRequest;
import com.investing.forecastbackend.model.ForecastResponse;
import com.investing.forecastbackend.model.InvestmentDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@Service
@Slf4j
@RequiredArgsConstructor
public class InvestingForecastService {

    private static InvestmentDetail parseInvDetailsObject(JSONObject invDetail) {
        String category = (String) invDetail.get("category");
//        System.out.println(category);

        String minimum = (String) invDetail.get("minimum");
//        System.out.println(minimum);

        JSONArray jsonData = (JSONArray) invDetail.get("data");
        String[] data = new String[10];
        for(int i = 0; i < 10; i++){
            data[i] = (String)jsonData.get(i);
        }
//        for (String d : data) {
//            System.out.println(d);
//        }

        return new InvestmentDetail(category, minimum, data);
    }

    public List<InvestmentDetail> getInvestmentOptions() {
        List<InvestmentDetail> res = new ArrayList<>();

        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("/Users/erikatan/Projects/eng-possibilities/eng-possibilities-svcs-master/src/main/resources/data/investment-details.json"))
        {
            Object obj = jsonParser.parse(reader);

            JSONArray invDetailsList = (JSONArray) obj;

            invDetailsList.forEach( invDetail -> res.add(parseInvDetailsObject( (JSONObject) invDetail )) );

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return res;
    }

    public ForecastResponse getInvestmentOptions(final ForecastRequest request) {
        // TODO write algorithm to calculate investment forecast from request configuration
        return new ForecastResponse();
    }

}
