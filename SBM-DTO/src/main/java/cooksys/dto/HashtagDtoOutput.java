package cooksys.dto;

import java.util.Date;

public class HashtagDtoOutput {

    private String label;

    private Date first;

    private Date last;

    public HashtagDtoOutput() {
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Date getFirst() {
        return first;
    }

    public void setFirst(Date first) {
        this.first = first;
    }

    public Date getLast() {
        return last;
    }

    public void setLast(Date last) {
        this.last = last;
    }
}
