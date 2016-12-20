package com.joke.animatordemo;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.animation.TypeConverter;
import android.animation.TypeEvaluator;
import android.content.Intent;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.IntProperty;
import android.util.Property;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.joke.animatordemo.anim.PraiseAnimatorManager;
import com.joke.animatordemo.anim.TextViewWrapper;
import com.joke.animatordemo.fragment.SendGiftFragment;
import com.joke.animatordemo.fragment.SendGiftLayoutTransFragment;

public class MainActivity extends AppCompatActivity {

    private View fly_star;
    private TextView tv_ani;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fly_star = findViewById(R.id.fly_star);
        final PraiseAnimatorManager manager = new PraiseAnimatorManager(this, fly_star);
        manager.setDistance(2000);
        fly_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.fly();
            }
        });
        tv_ani = (TextView) findViewById(R.id.tv_ani);


        findViewById(R.id.send_gift_layout_trans).setOnClickListener(clickListener);
        findViewById(R.id.send_gift).setOnClickListener(clickListener);

        initTvAnimator();
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.send_gift:
                    getSupportFragmentManager().beginTransaction()
                            .addToBackStack("gift")
                            .add(android.R.id.content, SendGiftFragment.newInstance())
                            .commit();
                    break;
                case R.id.send_gift_layout_trans:
                    getSupportFragmentManager().beginTransaction()
                            .addToBackStack("gift")
                            .add(android.R.id.content, SendGiftLayoutTransFragment.newInstance())
                            .commit();
                    break;
            }

        }
    };



    private void initTvAnimator() {
        final TextViewWrapper textViewWrapper = new TextViewWrapper(tv_ani);
        tv_ani.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewWrapper.startAni("HELLO WORLD".toCharArray());
//                textViewWrapper.startAni(0,100);

            }
        });


    }



}
