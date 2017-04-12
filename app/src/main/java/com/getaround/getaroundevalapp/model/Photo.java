package com.getaround.getaroundevalapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ronaldbjork on 4/11/17.
 */

public class Photo {

    @JsonProperty("id")
    public long getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(long id) {
        this.id = id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }


    @JsonProperty("image_url")
    public String getImage_url() {
        return image_url;
    }

    @JsonProperty("image_url")
    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }


    private String name;
    private String image_url;
    private long id;
}
