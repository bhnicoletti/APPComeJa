package br.com.nicoletti.comeja.model;

/**
 * Created by Nicoletti on 07/10/2016.
 */
public class TokenEvent {
    private String token;

    public TokenEvent(String token ){
        this.token = token;
    }

    public String getToken(){
        return token;
    }
}