package com.joke.animatordemo.anim;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.util.Property;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

/**
 * Created by SHF on 2016/12/19.
 */

public class TextViewWrapper {
    private TextView textView;
    private int maxNum = 100;

    public TextViewWrapper(TextView textView) {
        this.textView = textView;
    }

    public void setText(char[] chars) {
        textView.setText(String.valueOf(chars));
    }

    public void setText(int integer){
        textView.setText(String.valueOf(integer));
    }

    public void setTextColor(int integer) {
        int color;
        if (integer <= maxNum / 3) {
            color = 0xff00ff00;
        } else if (integer > maxNum / 3 && integer < maxNum / 3 * 2) {
            color = 0xffffff00;
        } else {
            color = 0xffff0000;
        }
        textView.setTextColor(color);
    }

    private Property<TextView, Integer> property = new Property<TextView, Integer>(Integer.class, "text") {
        @Override
        public Integer get(TextView object) {
            return Integer.parseInt(object.getText().toString());
        }

        @Override
        public void set(TextView object, Integer value) {
            object.setText(String.valueOf(value));
        }

    };

    public void startAni(int startValue, int endValue) {
        maxNum = endValue - startValue;
        PropertyValuesHolder colorHolder = PropertyValuesHolder.ofInt("textColor", startValue, endValue);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(this, colorHolder);

        ObjectAnimator text = ObjectAnimator.ofInt(textView,property, startValue, endValue);
        AnimatorSet set = new AnimatorSet();
        set.play(text).with(objectAnimator);

        set.setDuration(2000);
        set.setInterpolator(new DecelerateInterpolator());
        set.start();
    }

    public void startAni(char[] end) {
        char[] start = new char[end.length];
        for (int i = 0; i < start.length; i++) {
            start[i] = 'A';
        }
        PropertyValuesHolder charHolder = PropertyValuesHolder.ofObject("text", new CharEvaluator(), start, end);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(this, charHolder);
        objectAnimator.setDuration(2000);
        objectAnimator.start();
    }


    private class CharEvaluator implements TypeEvaluator<char[]> {
        int max = 0;
        @Override
        public char[] evaluate(float fraction, char[] startValue, char[] endValue) {

            int length = endValue.length;
            char[] chars = new char[length];
            for (int i = 0; i < length; i++) {
                max = Math.max(max,endValue[i]);
                chars[i] =(char) Math.min( (startValue[i] + (max - startValue[i]) * fraction),endValue[i]);
            }

            return chars;
        }
    }
}
