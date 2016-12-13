package com.joke.animatordemo.bean;

import android.support.annotation.DrawableRes;

/**
 * Created by lkc on 2016/8/19.
 */
public class GiftBean {
    public int giftid;
    public String name;
    public
    @DrawableRes
    int picture;

    public GiftBean() {
    }

    public GiftBean(int giftid, String name, int picture) {
        this.giftid = giftid;
        this.name = name;
        this.picture = picture;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GiftBean bean = (GiftBean) o;

        return giftid == bean.giftid;

    }

    @Override
    public int hashCode() {
        return giftid;
    }

    public int getGiftid() {
        return giftid;
    }
}
