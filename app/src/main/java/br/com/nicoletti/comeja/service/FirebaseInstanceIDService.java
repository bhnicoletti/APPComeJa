package br.com.nicoletti.comeja.service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.greenrobot.eventbus.EventBus;
import br.com.nicoletti.comeja.model.TokenEvent;


/**
 * Created by Nicoletti on 08/09/2016.
 */
public class FirebaseInstanceIDService extends FirebaseInstanceIdService {



    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        TokenEvent tokenEvent = new TokenEvent( token );
        EventBus.getDefault().post(tokenEvent);
    }




}
