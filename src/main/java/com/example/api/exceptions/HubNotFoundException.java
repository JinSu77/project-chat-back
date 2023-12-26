package com.example.api.exceptions;

public class HubNotFoundException extends Exception {
    public HubNotFoundException() {
        super("Mercure hub not found.");
    }
}
