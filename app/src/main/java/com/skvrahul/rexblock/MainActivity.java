package com.skvrahul.rexblock;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    Realm realm;
    List<Rule> rules =  new ArrayList<>();
    int PERMISSION_CALLBACK = 69;
    RulesAdapter adapter;
    RecyclerView rulesRV;
    CardView noRulesCV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        noRulesCV = (CardView)findViewById(R.id.no_rules_card_view);
        getPermissions();

        //Initialize Realm
        Realm.init(getBaseContext());
        realm = Realm.getDefaultInstance();

        rulesRV = (RecyclerView)findViewById(R.id.rules_rv);
        RulesAdapter.DeleteClickListener deleteClickListener = new RulesAdapter.DeleteClickListener() {
            @Override
            public void onItemClick(Rule rule) {
                removeRule(rule);
            }
        };
        fetchRules();

        if(rules.size()==0){
            noRulesCV.setVisibility(View.VISIBLE);
        }else{
            noRulesCV.setVisibility(View.GONE);
        }
        adapter = new RulesAdapter(rules, deleteClickListener);
        rulesRV.setAdapter(adapter);
        rulesRV.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        rulesRV.setItemAnimator(new DefaultItemAnimator());
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater li = LayoutInflater.from(MainActivity.this);
                View regexPrompt = li.inflate(R.layout.regex_prompt, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        MainActivity.this);
                alertDialogBuilder.setView(regexPrompt);
                final EditText ruleUserInput = (EditText) regexPrompt
                        .findViewById(R.id.regex_edit_text);
                final EditText nameUserInput = (EditText) regexPrompt
                        .findViewById(R.id.regex_name_edit_text);
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        String regexRule = ruleUserInput.getText().toString();
                                        String regexName = nameUserInput.getText().toString();

                                        Rule rule = new Rule();
                                        rule.setActive(true);
                                        rule.setRegex(regexRule);
                                        rule.setName(regexName);
                                        addRule(rule);
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void addRule(Rule rule){
        int s = rules.size();
        rules.add(rule);
        adapter.notifyItemInserted(s);
        realm.beginTransaction();
        realm.copyToRealm(rule);
        realm.commitTransaction();
        if(rules.size()==0){
            noRulesCV.setVisibility(View.VISIBLE);
        }else{
            noRulesCV.setVisibility(View.GONE);
        }
        View parentLayout = findViewById(R.id.rules_rv);
        Snackbar sn = Snackbar.make(parentLayout,"Your rule has been added!",Snackbar.LENGTH_LONG);
        sn.show();
    }
    public void removeRule(Rule rule){
        int p = rules.indexOf(rule);
        rules.remove(rule);
        adapter.notifyItemRemoved(p);
        realm.beginTransaction();
        realm.where(Rule.class).findAll().deleteAllFromRealm();
        realm.copyToRealm(rules);
        realm.commitTransaction();
        if(rules.size()==0){
            noRulesCV.setVisibility(View.VISIBLE);
        }else{
            noRulesCV.setVisibility(View.GONE);
        }
        View parentLayout = findViewById(R.id.rules_rv);
        Snackbar sn = Snackbar.make(parentLayout,"Your rule has been removed!",Snackbar.LENGTH_LONG);
        sn.show();
    }
    public void getPermissions(){
        if(ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.READ_PHONE_STATE},PERMISSION_CALLBACK);
        }

    }
    public void fetchRules(){
        RealmResults<Rule> rulesRealmResult = realm.where(Rule.class).findAll();
        rules = realm.copyFromRealm(rulesRealmResult);
        if(rules.size()==0){
            noRulesCV.setVisibility(View.VISIBLE);
        }else{
            noRulesCV.setVisibility(View.GONE);
        }
    }
}
