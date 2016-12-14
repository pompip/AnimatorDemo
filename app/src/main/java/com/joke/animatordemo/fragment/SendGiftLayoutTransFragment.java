package com.joke.animatordemo.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.joke.animatordemo.BR;
import com.joke.animatordemo.R;
import com.joke.animatordemo.bean.GiftBean;

import java.util.ArrayList;

/**
 * Created by SHF on 2016/8/17.
 */
public class SendGiftLayoutTransFragment extends Fragment {


    private static ArrayList<GiftBean> giftList;
    private ScrollView sv_send_gift_back;
    private LinearLayout animator_container;
    private View ll_send_gift;

    static {
        giftList = new ArrayList<>();
        GiftBean bean0 = new GiftBean(0, "巧克力", R.mipmap.chocolate);
        GiftBean bean1 = new GiftBean(1, "蛋糕", R.mipmap.cake);
        GiftBean bean2 = new GiftBean(2, "钻石", R.mipmap.diamond);
        GiftBean bean3 = new GiftBean(3, "跑车", R.mipmap.car);
        giftList.add(bean0);
        giftList.add(bean1);
        giftList.add(bean2);
        giftList.add(bean3);
    }


    public static SendGiftLayoutTransFragment newInstance() {

        Bundle args = new Bundle();
        SendGiftLayoutTransFragment fragment = new SendGiftLayoutTransFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_send_gift, container, false);
        ll_send_gift = inflate.findViewById(R.id.ll_send_gift);
        sv_send_gift_back = (ScrollView) inflate.findViewById(R.id.sv_send_gift_back);
        animator_container = (LinearLayout) inflate.findViewById(R.id.animator_container);

        inflate.findViewById(R.id.gift0).setOnClickListener(click);
        inflate.findViewById(R.id.gift1).setOnClickListener(click);
        inflate.findViewById(R.id.gift2).setOnClickListener(click);
        inflate.findViewById(R.id.gift3).setOnClickListener(click);
        createLayoutTrans();
        return inflate;
    }

    private void createLayoutTrans() {
        LayoutTransition transition = new LayoutTransition();
        transition.setAnimator(LayoutTransition.APPEARING, createAppearAni(null));
        transition.setAnimator(LayoutTransition.DISAPPEARING, createDismissAni(null));
        animator_container.setLayoutTransition(transition);

    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private LayoutTransition createChildLayoutTrans() {
        LayoutTransition transition = new LayoutTransition();
        transition.setAnimator(LayoutTransition.CHANGING, createScaleAni(null));
        transition.enableTransitionType(LayoutTransition.CHANGING);
        return transition;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ll_send_gift.setOnTouchListener(touch);
        sv_send_gift_back.setOnTouchListener(touch);

    }

    @Override
    public void onDestroy() {
        handler.removeMessages(1, null);
        animator_container.removeAllViews();
        super.onDestroy();
    }


    View.OnTouchListener touch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (v.getId() == R.id.sv_send_gift_back && event.getAction() == MotionEvent.ACTION_UP) {
                getFragmentManager().popBackStack();
            }
            return true;
        }
    };

    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.gift0:
                    startAni(giftList.get(0));
                    break;
                case R.id.gift1:
                    startAni(giftList.get(1));
                    break;
                case R.id.gift2:
                    startAni(giftList.get(2));
                    break;
                case R.id.gift3:
                    startAni(giftList.get(3));
                    break;
            }
        }
    };


    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            endAni(msg.what);
            return true;
        }
    });

    /**
     * 添加View
     *
     * @param gift
     */
    private void startAni(final GiftBean gift) {
        View view = animator_container.findViewWithTag(gift.getGiftid());//通过ID获取该礼物,并判断动画是否正在执行
        if (view != null) {  //如果该礼物动画存在,则执行放大动画
            gift.setGiftNum(gift.getGiftNum() + 1);

        } else { //如果该View不存在,创建新View 并执行弹出动画

            ViewDataBinding dataBinding = DataBindingUtil
                    .inflate(LayoutInflater.from(getContext()), R.layout.item_gift_animator, animator_container, false);
            gift.setGiftNum(1);
            dataBinding.setVariable(BR.gift, gift);

            View root = dataBinding.getRoot();
            root.setTag(gift.getGiftid());
            ((ViewGroup) root).setLayoutTransition(createChildLayoutTrans());
            animator_container.addView(root); //View 添加到容器中
        }


        handler.removeMessages(gift.getGiftid());
        Message message = handler.obtainMessage(gift.getGiftid());
        handler.sendMessageDelayed(message, 2000);

    }


    /**
     * 移除
     */
    private void endAni(final int giftid) {
        animator_container.removeView(animator_container.findViewWithTag(giftid));
    }

    /**
     * 放大动画
     */
    private Animator createScaleAni(View view) {
        final ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1, 3, 1);
        final ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1, 3, 1);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleX).with(scaleY);
        return animatorSet;
    }

    /**
     * 弹出动画
     */
    private Animator createAppearAni(View view) {
        return ObjectAnimator.ofFloat(view, "translationX", -600, 0);
    }

    /**
     * 消失动画
     */
    private Animator createDismissAni(View view) {
        final ObjectAnimator endAni = ObjectAnimator.ofFloat(view, "translationY", 0, -100);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 1, 0.2f, 0);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(endAni).with(alpha);
        return animatorSet;
    }

}
