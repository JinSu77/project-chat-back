package com.example.api.services.Mercure.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.example.api.services.Mercure.http.exceptions.ForbiddenException;
import com.example.api.services.Mercure.http.exceptions.NotFoundException;
import com.example.api.services.Mercure.http.exceptions.UnauthorizedException;

// TODO: Credits to vitorvillar : https://github.com/vitorluis/java-mercure
public class MercureClient {
    private HttpClient httpClient;
    private String url;
    private String authorizationToken;


    public MercureClient(String url, String authorizationToken) {
        this.httpClient = HttpClients.createDefault();
        this.url = url;
        this.authorizationToken = authorizationToken;
    }

        public String sendRequest(Map<String, String> postData) throws UnauthorizedException, ForbiddenException,
            NotFoundException {
        var responseContent = "";
        var request = new HttpPost(this.url);
        request.addHeader("Authorization", "Bearer " + this.authorizationToken);
        request.addHeader("Content-Type", "application/x-www-form-urlencoded");

        List<NameValuePair> parameters = new ArrayList<>();

        for (var entry : postData.entrySet()) {
            parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        try {
            request.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));

            var response = this.httpClient.execute(request);
            if (response.getEntity() != null) {
                responseContent = EntityUtils.toString(response.getEntity());
            }
            switch (response.getStatusLine().getStatusCode()) {
                case HttpStatus.SC_UNAUTHORIZED:
                    throw new UnauthorizedException(responseContent);
                case HttpStatus.SC_FORBIDDEN:
                    throw new ForbiddenException(responseContent);
                case HttpStatus.SC_NOT_FOUND:
                    throw new NotFoundException();
            }
        } catch (IOException e) {
            throw new NotFoundException();
        }

        return responseContent;
    }
}
