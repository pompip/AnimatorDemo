package com.joke.animatordemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.joke.animatordemo.anim.PraiseAnimatorManager;
import com.joke.animatordemo.fragment.SendGiftFragment;

public class MainActivity extends AppCompatActivity {

    private View fly_star;
    private View send_gift;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fly_star = findViewById(R.id.fly_star);
        send_gift = findViewById(R.id.send_gift);


        final PraiseAnimatorManager manager = new PraiseAnimatorManager(this, fly_star);
        manager.setDistance(2000);
        fly_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.fly();
            }
        });

        send_gift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().addToBackStack("gift").add(android.R.id.content, SendGiftFragment.newInstance()).commit();
            }
        });

    }
}
