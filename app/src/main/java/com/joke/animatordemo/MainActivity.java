package com.joke.animatordemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.joke.animatordemo.anim.PraiseAnimatorManager;

public class MainActivity extends AppCompatActivity {

    private View tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tv = findViewById(R.id.tv);

        final PraiseAnimatorManager manager = new PraiseAnimatorManager(this,tv);
        manager.setDistance(2000);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.fly();
            }
        });

    }
}
