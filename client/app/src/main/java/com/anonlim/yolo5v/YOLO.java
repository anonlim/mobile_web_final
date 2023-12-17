package com.anonlim.yolo5v;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class YOLO {

    private static final String testApi = "{\n" +
            "    \"title\": \"Sample Title\",\n" +
            "    \"text\": \"1 person, 2 bb, 3 bus\",\n" +
            "    \"created_date\": \"2023-12-13\",\n" +
            "    \"published_date\": \"2023-12-14\",\n" +
            "    \"image\": \"https://example.com/sample-image.jpg\"\n" +
            "}";


    public static Result getTestResult(){
        ObjectMapper objectMapper = new ObjectMapper();
        Result result;
        try {
            JsonNode dataNode = objectMapper.readTree(testApi);
            result = objectMapper.treeToValue(dataNode, Result.class);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static Result getResult(String url){

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        Result result;
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){
                String body = Objects.requireNonNull(response.body()).string();;
                JsonNode dataNode = objectMapper.readTree(body);
                result = objectMapper.treeToValue(dataNode.get(dataNode.size()-1), Result.class);
                return result;
            }
            else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
