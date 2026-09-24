package com.example.spring_boot_example.controller;

public class QueryParameters {
    private int id;
    private String filter;

    public QueryParameters(int id, String filter) {
        this.id = id;
        this.filter = filter;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
