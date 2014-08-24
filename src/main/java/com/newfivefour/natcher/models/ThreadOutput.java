package com.newfivefour.natcher.models;

import java.util.List;

public class ThreadOutput {
    private List<String> tags;
    private String id;
    private String subject;
    private String author;
    private List<PostOutput> posts;
    private int numPosts;
    private long creation;
    private long modification;
    private long rootPostId;
    private long latestPostId;
}
