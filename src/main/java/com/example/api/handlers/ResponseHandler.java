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
        if (Arrays.asList(handledStatus).contains(status.toString())) {
            Map<String, Object> map = new HashMap<String, Object>();

            map.put("data", responseObj);

            return new ResponseEntity<Object>(map,status);
        }

        return new ResponseEntity<Object>(status);
    }
}
