package com.autulin.bigbrother.model.picture;

import com.autulin.bigbrother.net.API;
import com.autulin.library.http.HttpRes;
import com.autulin.library.http.HttpResFunction;
import com.autulin.library.http.HttpRetrofit;

import java.util.List;

import io.reactivex.Flowable;
import timber.log.Timber;

/**
 * Created by autulin on 7/31/17.
 */

public class PictureDataSource {
    public API getApi() {
        return HttpRetrofit.getInstance().getService(API.class);
    }

    public Flowable<List<Picture>> getAllPictures() {
        Timber.d("method: %s, thread: %s_%s", "getPictures()", Thread.currentThread().getName(), Thread.currentThread().getId());
        return getApi().getAllPictures().map(new HttpResFunction<List<Picture>>());
    }

    public Flowable<Picture> getCurrentPicture() {
        Timber.d("method: %s, thread: %s_%s", "getCurrentPicture()", Thread.currentThread().getName(), Thread.currentThread().getId());
        return getApi().getCurrentPicture().map(new HttpResFunction<Picture>());
    }

    public Flowable<String> deleteAllPicture() {
        Timber.d("method: %s, thread: %s_%s", "deleteAllPicture()", Thread.currentThread().getName(), Thread.currentThread().getId());
        return getApi().deleteAllPictures().map(new HttpResFunction<String>());
    }
}
