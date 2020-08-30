package com.tronic.bot.rest;

import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.util.HashMap;

// see https://stackoverflow.com/questions/45295530/spark-cors-access-control-allow-origin-error
public class CORSFilter {
    private final HashMap<String, String> corsHeaders = new HashMap<>();

    public CORSFilter() {
        corsHeaders.put("Access-Control-Allow-Methods", "GET"); // Allowed Method
        corsHeaders.put("Access-Control-Allow-Origin", "*");
        corsHeaders.put("Access-Control-Allow-Headers", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,");
        corsHeaders.put("Access-Control-Allow-Credentials", "false");
    }

    public void apply() {
        Filter filter = (request, response) -> corsHeaders.forEach(response::header);
        Spark.after(filter);
    }
}
