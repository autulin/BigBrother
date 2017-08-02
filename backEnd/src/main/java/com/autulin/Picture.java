package com.autulin;

import lombok.Data;

import java.util.Date;

@Data
public class Picture {
    private Date date;
    private String picBase64;

    public Picture(Date date, String picBase64) {
        this.date = date;
        this.picBase64 = picBase64;
    }
}
