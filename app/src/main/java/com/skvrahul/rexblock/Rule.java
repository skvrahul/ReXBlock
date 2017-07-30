package com.skvrahul.rexblock;

import io.realm.RealmObject;

/**
 * Created by skvrahul on 29/7/17.
 */

public class Rule extends RealmObject {
    private String regex;
    private boolean active;
    private String name;
    public Rule(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
