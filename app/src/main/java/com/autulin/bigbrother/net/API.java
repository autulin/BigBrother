package com.autulin.bigbrother.net;

import com.autulin.bigbrother.model.picture.Picture;
import com.autulin.library.http.HttpRes;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.DELETE;
import retrofit2.http.GET;

/**
 * Created by autulin on 7/31/17.
 */

public interface API {

    @GET("pictures")
    Flowable<HttpRes<List<Picture>>> getAllPictures();

    @GET("picture")
    Flowable<HttpRes<Picture>> getCurrentPicture();

    @DELETE("pictures")
    Flowable<HttpRes<String>> deleteAllPictures();
}
