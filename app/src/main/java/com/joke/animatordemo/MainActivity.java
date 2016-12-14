package com.joke.animatordemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.joke.animatordemo.anim.PraiseAnimatorManager;
import com.joke.animatordemo.fragment.SendGiftFragment;
import com.joke.animatordemo.fragment.SendGiftLayoutTransFragment;

public class MainActivity extends AppCompatActivity {

    private View fly_star;


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


        findViewById(R.id.send_gift_layout_trans).setOnClickListener(clickListener);
        findViewById(R.id.send_gift).setOnClickListener(clickListener);

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
}
