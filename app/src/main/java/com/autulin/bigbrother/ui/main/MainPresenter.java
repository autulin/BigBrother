package com.autulin.bigbrother.ui.main;

import android.provider.CalendarContract;
import android.support.annotation.NonNull;

import com.autulin.bigbrother.model.picture.Picture;
import com.autulin.bigbrother.model.picture.PictureDataSource;
import com.autulin.library.http.HttpSchedulersTransformer;
import com.autulin.library.rxbus.RxBus;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by autulin on 7/31/17.
 */

public class MainPresenter implements MainContract.Presenter {
    @NonNull
    private final MainContract.View mView;

    @NonNull
    private final PictureDataSource pictureDataSource;

    private CompositeDisposable compositeDisposable;

    public MainPresenter(@NonNull MainContract.View mView, @NonNull PictureDataSource pictureDataSource) {
        this.mView = mView;
        this.mView.setPresenter(this);
        this.pictureDataSource = pictureDataSource;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void loadCurrentPicture() {
        compositeDisposable.add(
                pictureDataSource.getCurrentPicture()
                            .compose(new HttpSchedulersTransformer<Picture>())
                            .doOnSubscribe(new Consumer<Subscription>() {
                                @Override
                                public void accept(Subscription subscription) throws Exception {
                                    Timber.d("method: %s, thread: %s_%s", "doOnSubscribe()", Thread.currentThread().getName(), Thread.currentThread().getId());
                                    Timber.d("doOnSubscribe()");
                                    mView.onAction();
                                }
                            })
                            .subscribe(new Consumer<Picture>() {
                                @Override
                                public void accept(Picture picture) throws Exception {
                                    Timber.d("onNext()");
                                    Timber.e(picture.toString());
                                    mView.onActionSuccess(picture);
                                    Timber.d("method: %s, thread: %s_%s", "onNext()", Thread.currentThread().getName(), Thread.currentThread().getId());
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    Timber.d("method: %s, thread: %s_%s", "onError()", Thread.currentThread().getName(), Thread.currentThread().getId());
                                    Timber.e(throwable);
                                    mView.onActionError(throwable.getMessage());
                                }
                            }, new Action() {
                                @Override
                                public void run() throws Exception {
                                    Timber.d("method: %s, thread: %s_%s", "onComplete()", Thread.currentThread().getName(), Thread.currentThread().getId());
                                    Timber.d("onComplete()");
                                    mView.onActionFinish();
                                }
                            }));
    }

    @Override
    public void loadAllPictures() {
        compositeDisposable.add(
                pictureDataSource.getAllPictures()
                        .compose(new HttpSchedulersTransformer<List<Picture>>())
                        .doOnSubscribe(new Consumer<Subscription>() {
                            @Override
                            public void accept(Subscription subscription) throws Exception {
                                Timber.d("method: %s, thread: %s_%s", "doOnSubscribe()", Thread.currentThread().getName(), Thread.currentThread().getId());
                                Timber.d("doOnSubscribe()");
                                mView.onLoading();
                            }
                        })
                        .subscribe(new Consumer<List<Picture>>() {
                            @Override
                            public void accept(List<Picture> pictures) throws Exception {
                                Timber.d("onNext()");
                                Timber.e(pictures.toString());
                                mView.onLoadSuccess(pictures);
                                Timber.d("method: %s, thread: %s_%s", "onNext()", Thread.currentThread().getName(), Thread.currentThread().getId());
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Timber.d("method: %s, thread: %s_%s", "onError()", Thread.currentThread().getName(), Thread.currentThread().getId());
                                Timber.e(throwable);
                                mView.onLoadError(throwable.getMessage());
                            }
                        }, new Action() {
                            @Override
                            public void run() throws Exception {
                                Timber.d("method: %s, thread: %s_%s", "onComplete()", Thread.currentThread().getName(), Thread.currentThread().getId());
                                Timber.d("onComplete()");
                                mView.onLoadFinish();
                            }
                        }));
    }

    @Override
    public void deleteAllPictures() {
        compositeDisposable.add(
                pictureDataSource.deleteAllPicture()
                        .compose(new HttpSchedulersTransformer<String>())
                        .doOnSubscribe(new Consumer<Subscription>() {
                            @Override
                            public void accept(Subscription subscription) throws Exception {
                                Timber.d("method: %s, thread: %s_%s", "doOnSubscribe()", Thread.currentThread().getName(), Thread.currentThread().getId());
                                Timber.d("doOnSubscribe()");
                                mView.onAction();
                            }
                        })
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                Timber.d("onNext()");
//                                mView.onActionSuccess(null);
                                Timber.d("method: %s, thread: %s_%s", "onNext()", Thread.currentThread().getName(), Thread.currentThread().getId());
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Timber.d("method: %s, thread: %s_%s", "onError()", Thread.currentThread().getName(), Thread.currentThread().getId());
                                Timber.e(throwable);
                                mView.onActionError(throwable.getMessage());
                            }
                        }, new Action() {
                            @Override
                            public void run() throws Exception {
                                Timber.d("method: %s, thread: %s_%s", "onComplete()", Thread.currentThread().getName(), Thread.currentThread().getId());
                                Timber.d("onComplete()");
                                mView.onActionFinish();
                            }
                        }));
    }

    @Override
    public void subscribe() {
        if (null == compositeDisposable) {
            compositeDisposable = new CompositeDisposable();
        }
        initRxBus();
        loadAllPictures();
        loadCurrentPicture();
    }

    private void initRxBus() {
        compositeDisposable.add(RxBus.getBus()
                .toObserverable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (o instanceof Events.ReloadEvent) {
                        loadAllPictures();
                    }
                }));
    }

    @Override
    public void unSubscribe() {
        mView.onLoadFinish();
        compositeDisposable.clear();
        mView.onActionFinish();
    }
}
