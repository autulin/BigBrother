package com.autulin.bigbrother.ui.main;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.autulin.bigbrother.R;
import com.autulin.bigbrother.model.picture.Picture;
import com.autulin.bigbrother.model.picture.PictureDataSource;
import com.autulin.library.rxbus.RxBus;

import java.util.Date;
import java.util.List;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements MainContract.View{

    private MainContract.Presenter presenter;
    private TextView textView;
    private ImageView imageView;

    private RecyclerView mList;
    private MyAdapter myAdapter;

    private SwipeRefreshLayout mSwipeLayout;

    private ProgressDialog mPd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        presenter = new MainPresenter(this, new PictureDataSource());

        textView = (TextView) findViewById(R.id.text);
        imageView = (ImageView) findViewById(R.id.image);

        findViewById(R.id.delete_btn).setOnClickListener(v ->
            presenter.deleteAllPictures()
        );
        findViewById(R.id.refresh_btn).setOnClickListener(v ->
            presenter.loadCurrentPicture()
        );

        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeLayout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeLayout.setOnRefreshListener(() -> RxBus.getBus().send(new Events.ReloadEvent()));

        mList = (RecyclerView) findViewById(R.id.list_view);
        mList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        myAdapter = new MyAdapter();
        mList.setAdapter(myAdapter);


        //init progress
        mPd = new ProgressDialog(this);
        mPd.setMessage("please wait...");

    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.subscribe();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.unSubscribe();
    }

    @Override
    public void onLoading() {
        mSwipeLayout.setRefreshing(true);
    }

    @Override
    public void onLoadSuccess(List<Picture> pictures) {
        myAdapter.updateDate(pictures);
    }

    @Override
    public void onLoadError(String msg) {
        mSwipeLayout.setRefreshing(false);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        Log.e("main", "onLoadError: " + msg);
    }

    @Override
    public void onLoadFinish() {
        mSwipeLayout.setRefreshing(false);
    }

    @Override
    public void onAction() {
        mPd.show();
    }

    @Override
    public void onActionSuccess(Picture picture) {
        textView.setText(new Date(picture.getDate()).toString());
        imageView.setImageBitmap(base64Str2Bitmap(picture.getPicBase64()));
        mPd.dismiss();
    }

    private Bitmap base64Str2Bitmap(String s) {
        Bitmap bitmap=null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(s, Base64.DEFAULT);
            bitmap= BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    public void onActionError(String msg) {
        mPd.dismiss();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        Log.e("main", "onLoadError: " + msg);
    }

    @Override
    public void onActionFinish() {
        mPd.dismiss();
        Snackbar.make(mList,"操作成功",Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        Timber.d("setPresenter()");
    }


    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private List<Picture> pictures;

        public void updateDate(List<Picture> pictures) {
            this.pictures = pictures;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate( R.layout.item, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (pictures != null) {
                Picture picture = pictures.get(position);
                holder.imageView.setImageBitmap(base64Str2Bitmap(picture.getPicBase64()));
                holder.textView.setText(new Date(picture.getDate()).toString());
            }

        }

        @Override
        public int getItemCount() {
            return pictures.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            TextView textView;
            ImageView imageView;

            public ViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.item_tv);
                imageView = (ImageView) itemView.findViewById(R.id.item_iv);
            }
        }
    }
}
