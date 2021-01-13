package br.com.nicoletti.comeja.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import br.com.nicoletti.comeja.model.WrapObjToNetwork;


/**
 * Created by viniciusthiengo on 7/26/15.
 */
public class NetworkConnection {
    private static NetworkConnection instance;
    private Context mContext;
    private RequestQueue mRequestQueue;
    private String url = "www.comeja.com.br";


    public NetworkConnection(Context c){
        mContext = c;
        mRequestQueue = getRequestQueue();
    }


    public static NetworkConnection getInstance( Context c ){
        if( instance == null ){
            instance = new NetworkConnection( c.getApplicationContext() );
        }
        return( instance );
    }


    public RequestQueue getRequestQueue(){
        if( mRequestQueue == null ){
            mRequestQueue = Volley.newRequestQueue(mContext);
        }
        return(mRequestQueue);
    }


    public <T> void addRequestQueue( Request<T> request ){
        getRequestQueue().add(request);
    }


    public void execute(final Transaction transaction, final String tag, final String json) throws JSONException {
        WrapObjToNetwork obj = transaction.doBefore();

        if( obj == null ){
            return;
        }




        JsonObjectRequest req = new JsonObjectRequest("http://"+url+"/service/"+obj.getComplemento()+"/"+obj.getMethod(), new JSONObject(json),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray json = new JSONArray();
                        json.put(response);
                            transaction.doAfter(json);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        }){
            @Override
            public String getBodyContentType() {
                return String.format("application/json; charset=utf-8");
            }
        };

        req.setTag(tag);
        req.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        addRequestQueue(req);
    }


    public void executeGET( final Transaction transaction, final String tag){
        WrapObjToNetwork obj = transaction.doBefore();
    Log.e("Entrou no GET","GET");

        if( obj == null ){
            return;
        }


        CustomRequest request = new CustomRequest(Request.Method.GET,
                "http://"+url+"/service/"+obj.getComplemento()+"/"+obj.getMethod(),
                obj.getParams(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.i("LOG", tag+" ---> "+response);
                        transaction.doAfter(response);
                        Log.e("GET","Sucesso");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("LOG", "onErrorResponse(): "+error.getMessage());
                        transaction.doAfter(null);
                        Log.e("GET","Falso");
                    }
                });

        request.setTag(tag);
        request.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        addRequestQueue(request);
    }


}
