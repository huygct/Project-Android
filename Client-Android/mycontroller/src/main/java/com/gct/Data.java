package com.gct;

import java.io.Serializable;

/**
 * Created by thuynghi on 12/18/2014.
 */
public class Data implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    private String message;

    public Data () {

    }

    public Data(String id, String message) {
        this.id = id;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
