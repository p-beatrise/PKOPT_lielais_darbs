package org.acme.employeescheduling.domain;

import java.time.LocalTime;

public class Preference {

    private LocalTime time;

    public Preference() {
    }

    public Preference(LocalTime time) {
        this.time = time;
    }

    public LocalTime getPreference() {
        return time;
    }

    public void setPreference(LocalTime time) {
        this.time = time;
    }

}
