package com.example.api.handlers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseHandler {
    private static final String[] handledStatus = {
        HttpStatus.OK.toString(),
        HttpStatus.CREATED.toString(),
        HttpStatus.UNPROCESSABLE_ENTITY.toString()
    };

    public static ResponseEntity<Object> generateResponse(HttpStatus status, Object responseObj) {
        String statusString = status.toString();

        if (Arrays.asList(handledStatus).contains(statusString)) {
            Map<String, Object> map = new HashMap<String, Object>();

            if (statusString.equals(HttpStatus.UNPROCESSABLE_ENTITY.toString())) {
                Map<String, Object> error = new HashMap<String, Object>();

                error.put("error", responseObj);

                map.put("data", error);
            } else {
                map.put("data", responseObj);
            }


            return new ResponseEntity<Object>(map,status);
        }

        return new ResponseEntity<Object>(status);
    }
}
