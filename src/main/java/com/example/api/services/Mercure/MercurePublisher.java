package com.example.api.services.Mercure;

import java.util.HashMap;
import java.util.UUID;

import org.json.JSONObject;

import com.example.api.exceptions.HubNotFoundException;
import com.example.api.exceptions.PublishRejectedException;
import com.example.api.exceptions.UnauthorizedPublisherException;
import com.example.api.services.Mercure.http.MercureClient;
import com.example.api.services.Mercure.http.exceptions.ForbiddenException;
import com.example.api.services.Mercure.http.exceptions.NotFoundException;
import com.example.api.services.Mercure.http.exceptions.UnauthorizedException;

public class MercurePublisher {

    private MercureClient MercureHttpClient;

    public MercurePublisher(String mercureHub, String mercureToken) {
        this.MercureHttpClient = new MercureClient(mercureHub, mercureToken);
    }

    public MercureMessage create(String action, String resource, String topic, String type) {
            JSONObject json = new JSONObject();

            json.put("resource", resource);
            json.put("type", type);
            json.put("action", action);

            var mercureMessage = new MercureMessage(json.toString(), topic);

            return mercureMessage;
    }

    public MercureMessage publish(MercureMessage message) throws UnauthorizedPublisherException, PublishRejectedException,
            HubNotFoundException {
        var parameters = new HashMap<String, String>();
        parameters.put("data", message.getData());
        parameters.put("topic", message.getTopic());
        parameters.put("id", message.getId().toString());

        if (message.getPrivate()) {
            parameters.put("private", "on");
        }

        if (message.getType() != null && message.getType().isEmpty() == false) {
            parameters.put("type", message.getType());
        }

        try {
            var messageId = this.MercureHttpClient.sendRequest(parameters);
            message.setId(UUID.fromString(messageId));

            return message;
        } catch (UnauthorizedException e) {
            throw new UnauthorizedPublisherException(e.getMessage());
        } catch (ForbiddenException e) {
            throw new PublishRejectedException(e.getMessage());
        } catch (NotFoundException e) {
            throw new HubNotFoundException();
        }
    }
}
