package com.udacity.jwdnd.course1.cloudstorage.model;

public class NoteForm {
    private String notetitle;
    private String notedescription;

    public NoteForm(String notetitle, String notedescription) {
        this.notetitle = notetitle;
        this.notedescription = notedescription;
    }

    public String getNotetitle() {
        return notetitle;
    }

    public void setNotetitle(String notetitle) {
        this.notetitle = notetitle;
    }

    public String getNotedescription() {
        return notedescription;
    }

    public void setNotedescription(String notedescription) {
        this.notedescription = notedescription;
    }
}
