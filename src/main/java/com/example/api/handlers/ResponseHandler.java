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
        HttpStatus.UNPROCESSABLE_ENTITY.toString(),
        HttpStatus.INTERNAL_SERVER_ERROR.toString(),
        HttpStatus.NOT_FOUND.toString(),
    };

    private static final String[] errors = {
        HttpStatus.UNPROCESSABLE_ENTITY.toString(),
        HttpStatus.INTERNAL_SERVER_ERROR.toString(),
        HttpStatus.NOT_FOUND.toString(),
    };

    public static ResponseEntity<Object> generateResponse(HttpStatus httpStatus, String resourceName, Object responseObj) {
        String status = httpStatus.toString();

        if (! Arrays.asList(handledStatus).contains(status)) {
            return new ResponseEntity<Object>(httpStatus);
        }

        Map<String, Object> response = new HashMap<String, Object>();

        if (Arrays.asList(errors).contains(status)) {
            return errorResponse(httpStatus, responseObj, response);
        }

        if (resourceName == null) {
            return successResponse(httpStatus, responseObj, response);
        }

        return successResponse(
            httpStatus, 
            Map.of(resourceName, responseObj), 
            response
        );
    }

    private static ResponseEntity<Object> successResponse(HttpStatus httpStatus, Object responseObj, Map<String, Object> response) {
        return new ResponseEntity<Object>(Map.of("data", responseObj), httpStatus);
    }

    private static ResponseEntity<Object> errorResponse(HttpStatus httpStatus, Object responseObj, Map<String, Object> response) {
        Map<String, Object> error = Map.of("error", responseObj);

        response.put("data", error);

        return new ResponseEntity<Object>(response, httpStatus);
    }
}
