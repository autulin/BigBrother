package com.autulin.bigbrother.model.picture;

import java.util.Date;

/**
 * Created by autulin on 7/28/17.
 */

public class Picture {
    private String picBase64;
    private Long date;

    public String getPicBase64() {
        return picBase64;
    }

    public void setPicBase64(String picBase64) {
        this.picBase64 = picBase64;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Picture{" +
                "picBase64='" + picBase64.substring(picBase64.length() > 20 ? picBase64.length() - 20: 0) + '\'' +
                ", date=" + date +
                '}';
    }
}
