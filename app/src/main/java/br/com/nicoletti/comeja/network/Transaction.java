package br.com.nicoletti.comeja.network;

import org.json.JSONArray;

import br.com.nicoletti.comeja.model.WrapObjToNetwork;


/**
 * Created by viniciusthiengo on 7/26/15.
 */
public interface Transaction {
    WrapObjToNetwork doBefore();

    void doAfter(JSONArray jsonArray);
}
