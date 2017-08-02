package com.autulin.bigbrother.ui.main;

import com.autulin.bigbrother.model.picture.Picture;
import com.autulin.library.base.BasePresenter;
import com.autulin.library.base.BaseView;

import java.util.List;

/**
 * Created by autulin on 7/31/17.
 */

public class MainContract  {
    public interface Presenter extends BasePresenter {
        void loadCurrentPicture();
        void loadAllPictures();
        void deleteAllPictures();
    }

    public interface View extends BaseView<Presenter> {
        void onLoading();
        void onLoadSuccess(List<Picture> pictures);
        void onLoadError(String msg);
        void onLoadFinish();
        void onAction();
        void onActionSuccess(Picture picture);
        void onActionError(String msg);
        void onActionFinish();
    }
}
