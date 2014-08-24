package com.newfivefour.natcher.models;

import java.util.List;

public class PostOutput {
    public long id;
    public String username;
    public String errorMessage;
    public String subject;
    public String content;
    public String threadId;
    public long creation;
    public long modification;
    public List<String> tags;
    public boolean adminEdited;
}
