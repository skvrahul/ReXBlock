package com.skvrahul.rexblock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by skvrahul on 29/7/17.
 */

public class CallBarring extends BroadcastReceiver {
    private String number;
    Realm realm ;
    List<Rule> rules = new ArrayList<>();
    @Override
    public void onReceive(Context context, Intent intent) {
        if(!intent.getAction().equals("android.intent.action.PHONE_STATE")){
            return;
        }else{
            Realm.init(context);
            realm = Realm.getDefaultInstance();
            fetchRules();

            number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            Log.i("CallBarring", "onReceive: The phone number is "+number);
            if(block(number)){
                disconnectPhoneItelephony(context);
                Log.i("CallBarring", "onReceive: Call Blocked");
                return;
            }
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void disconnectPhoneItelephony(Context context)
    {
        ITelephony telephonyService;
        TelephonyManager telephony = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        try
        {
            Class c = Class.forName(telephony.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            telephonyService = (ITelephony) m.invoke(telephony);
            telephonyService.endCall();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public boolean block(String number){
        if(rules.size()!=0){
            for(int i=0;i<rules.size();i++){
                if(number.matches(rules.get(i).getRegex())&&rules.get(i).isActive()){
                    return true;
                }
            }
        }
        return false;
    }
    public void fetchRules(){
        RealmResults<Rule> rulesRealmResult = realm.where(Rule.class).findAll();
        rules = realm.copyFromRealm(rulesRealmResult);

    }
}

