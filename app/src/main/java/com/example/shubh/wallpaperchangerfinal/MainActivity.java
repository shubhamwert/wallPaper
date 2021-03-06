package com.example.shubh.wallpaperchangerfinal;

import android.app.WallpaperManager;
import android.content.Context;
import android.media.Image;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
//TODO Add listner to recycler View
//TODO storing images from phone

public class MainActivity extends AppCompatActivity {
    WallImageAdapter wallImageAdapter;
    FloatingActionButton mFabButton;
    ViewPager viewPager;
    MyDataSet myDataSet;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDataSet=new MyDataSet();
        setViewPager();
        setRecyler();
        setFab();



    }

    private void setFab() {
        mFabButton=findViewById(R.id.mFabButton);

        mFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WallpaperManager myWallpaperManager = WallpaperManager
                        .getInstance(MainActivity.this);


                try {

                    myWallpaperManager.setResource(myDataSet.getId(viewPager.getCurrentItem()));

                    Toast.makeText(MainActivity.this,
                            "Wallpaper successfully changed", Toast.LENGTH_SHORT)
                            .show();

                } catch (IOException e) {
                    Toast.makeText(MainActivity.this,"error",Toast.LENGTH_SHORT).show();
                }
            }
        });}

    private void setRecyler() {
        recyclerView=findViewById(R.id.mrecycler);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        wallImageAdapter =new WallImageAdapter(this, MyDataSet.getmData());
        recyclerView.setAdapter(wallImageAdapter);
        recyclerView.setHasFixedSize(true);
        startPreparingData();
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
viewPager.setCurrentItem(position);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    private void setViewPager() {
        viewPager = findViewById(R.id.view_pager);
        MyPager adapter=new MyPager(this);
        viewPager.setAdapter(adapter);
    }

    private void startPreparingData() {


        wallImageAdapter.notifyDataSetChanged();
    }

    public static interface ClickListener{
        public void onClick(View view,int position);
        public void onLongClick(View view,int position);}

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener){

            this.clicklistener=clicklistener;
            gestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child=recycleView.findChildViewUnder(e.getX(),e.getY());
                    if(child!=null && clicklistener!=null){
                        clicklistener.onLongClick(child,recycleView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child=rv.findChildViewUnder(e.getX(),e.getY());
            if(child!=null && clicklistener!=null && gestureDetector.onTouchEvent(e)){
                clicklistener.onClick(child,rv.getChildAdapterPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

}
