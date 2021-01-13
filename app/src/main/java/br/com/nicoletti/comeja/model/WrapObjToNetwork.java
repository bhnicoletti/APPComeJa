package br.com.nicoletti.comeja.model;

import java.util.HashMap;

/**
 * Created by viniciusthiengo on 7/26/15.
 */
public class WrapObjToNetwork {
    private String method;
    private HashMap<String, String> params;
    private String complemento;


    public WrapObjToNetwork(HashMap<String, String> params, String method, String complemento) {
        this.params = params;
        this.method = method;
        this.complemento = complemento;
    }


    public HashMap<String, String> getParams() {
        return params;
    }

    public void setParams(HashMap<String, String> params) {
        this.params = params;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }
}
