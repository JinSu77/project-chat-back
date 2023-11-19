package com.example.api.handlers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

public class ResponseHandler {
    private static final Integer[] handledStatus = {
        200,
        201,
        404,
        422,
        500,
    };

    private static final Integer[] errors = {
        403,
        404,
        422,
        500,
    };

    public static ResponseEntity<Object> generateResponse(Object httpStatusContainer, String resourceName, Object responseObj) {
        if (httpStatusContainer instanceof HttpStatus) {
            System.out.println("httpStatusContainer is instance of HttpStatus");
            HttpStatus statusCode = (HttpStatus) httpStatusContainer;

            return toFormat(statusCode.value(), resourceName, responseObj);
        }

        if (httpStatusContainer instanceof ResponseStatusException) {
            System.out.println("httpStatusContainer is instance of ResponseStatusException");
            ResponseStatusException responseStatusException = (ResponseStatusException) httpStatusContainer;

            return toFormat(responseStatusException.getStatusCode().value(), resourceName, responseStatusException.getReason());
        }
        
        return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static ResponseEntity<Object> toFormat(Integer httpStatusCode, String resourceName, Object responseObj) {
        if (! Arrays.asList(handledStatus).contains(httpStatusCode)) {
            return new ResponseEntity<Object>(
                Map.of("error", "This status code is not handled by ResponseHandler class."),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        Map<String, Object> response = new HashMap<String, Object>();

        if (Arrays.asList(errors).contains(httpStatusCode)) {
            return errorResponse(httpStatusCode, responseObj, response);
        }

        if (resourceName == null) {
            return successResponse(httpStatusCode, responseObj, response);
        }

        return successResponse(
            httpStatusCode, 
            Map.of(resourceName, responseObj),
            response
        );
    }

    private static ResponseEntity<Object> successResponse(Integer httpStatusCode, Object responseObj, Map<String, Object> response) {
        return new ResponseEntity<Object>(Map.of("data", responseObj), HttpStatus.valueOf(httpStatusCode));
    }

    private static ResponseEntity<Object> errorResponse(Integer httpStatus, Object responseObj, Map<String, Object> response) {
        Map<String, Object> error = Map.of("error", responseObj);

        response.put("data", error);

        return new ResponseEntity<Object>(response, HttpStatus.valueOf(httpStatus));
    }
}
