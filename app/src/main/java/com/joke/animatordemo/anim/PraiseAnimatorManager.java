package com.joke.animatordemo.anim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.PointF;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;


import com.joke.animatordemo.R;

import java.util.Random;

/**
 * Created by Administrator on 2016/6/8.
 */
public class PraiseAnimatorManager {
    private ViewGroup parent;
    private final Context context;
    private View anchorView;

    public PraiseAnimatorManager(Activity activity, View anchorView) {
        this.context = activity;
        this.anchorView = anchorView;
        parent = (ViewGroup) activity.getWindow().getDecorView();
    }

    float x = 0;
    float y = 1000;
    private Random random = new Random();
    private float distance = 1000;
    private int duration = 2000;


    public void setDistance(float distance) {
        this.distance = distance;
    }

    public ValueAnimator fly() {


        final ImageView target = new ImageView(context);
        target.setImageResource(R.mipmap.ic_launcher);

        int[] location = new int[2];
        anchorView.getLocationInWindow(location);
        x = location[0];
        y = location[1];

        PointF point0 = new PointF(x, y); //起点

        PointF point3 = new PointF(x, y - distance); //终点
//        float width1 = random.nextFloat() * 800;
//        float height1 = random.nextFloat() * 1000;
//        float width2 = random.nextFloat() * 800;
//        float height2 = random.nextFloat() * 1000;

        PointF point1 = new PointF(0, y * 2 / 3);
        PointF point2 = new PointF(2 * x, y / 3);
        BezierEvaluator evaluator = new BezierEvaluator(point1, point2);//贝塞尔曲线

        final ValueAnimator valueAnimator = ValueAnimator.ofObject(evaluator, point0, point3);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { //根据曲线数据更新透明度和位置
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                float animatedFraction = animation.getAnimatedFraction();
                float alpha;
                if (animatedFraction < 0.1) {
                    alpha = animatedFraction * 10;
                } else if (animatedFraction > 0.1) {
                    alpha = (1 - animatedFraction) * 10;
                } else {
                    alpha = 1;
                }

                PointF pointF = (PointF) animation.getAnimatedValue();
                target.setAlpha(alpha);
                target.setX(pointF.x);
                target.setY(pointF.y);
            }
        });
        AnimatorListenerAdapter listener = new AnimatorListenerAdapter() { //动画监听.动画开始添加view到parent,结束后移除.

            @Override
            public void onAnimationEnd(Animator animation) {
                parent.removeView(target);
                valueAnimator.removeAllUpdateListeners();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                parent.removeView(target);
                parent.addView(target, 100, 100);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                parent.removeView(target);
                valueAnimator.removeAllUpdateListeners();
            }
        };
        valueAnimator.addListener(listener);
        valueAnimator.setDuration(duration);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.start();
        return valueAnimator;
    }

    private static float bezier(float t, float p0, float p1, float p2, float p3) {
        return (1 - t) * (1 - t) * (1 - t) * p0
                + 3 * (1 - t) * (1 - t) * (t) * p1
                + 3 * t * t * (1 - t) * p2
                + t * t * t * p3;
    }

    private class BezierEvaluator implements TypeEvaluator<PointF> {


        PointF point1;
        PointF point2;

        BezierEvaluator(PointF point1, PointF point2) {
            this.point1 = point1;
            this.point2 = point2;
        }

        /**
         *  根据4个点,计算实际点.
         * @param fraction
         * @param startValue
         * @param endValue
         * @return
         */
        @Override
        public PointF evaluate(float fraction, PointF startValue, PointF endValue) {


            PointF point = new PointF();

            point.x = bezier(fraction, startValue.x, point1.x, point2.x, endValue.x);
            point.y = bezier(fraction, startValue.y, point1.y, point2.y, endValue.y);
            return point;
        }


    }
}
