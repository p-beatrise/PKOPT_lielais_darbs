package org.acme.employeescheduling.domain;

import java.time.LocalDate;

public class SickLeave {

    private LocalDate start;
    private LocalDate end;

    public SickLeave() {
    }


    public SickLeave(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }


    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }
    
    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }
    
    public boolean includes(LocalDate date) {
        return !date.isBefore(start) && !date.isAfter(end);
    }

}
