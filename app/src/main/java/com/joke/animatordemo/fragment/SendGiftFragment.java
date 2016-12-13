package com.joke.animatordemo.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.joke.animatordemo.R;
import com.joke.animatordemo.bean.GiftBean;

import java.util.ArrayList;

/**
 * Created by SHF on 2016/8/17.
 */
public class SendGiftFragment extends Fragment {


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


    public static SendGiftFragment newInstance() {

        Bundle args = new Bundle();
        SendGiftFragment fragment = new SendGiftFragment();
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

        return inflate;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ll_send_gift.setOnTouchListener(touch);
        sv_send_gift_back.setVisibility(View.VISIBLE);
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


    /**
     * 保存正在执行的动画的View
     */
    SparseArray<View> aniView = new SparseArray<>();

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            //what 判断礼物类型,不用于判断消息类型,because handler.removeMessage,只能接收what
            //arg1 礼物数量
            //arg2 无用
            //obj 礼物bean
            endAni(msg.what);
            return true;
        }
    });

    /**
     * 开始动画
     *
     * @param gift
     */
    private void startAni(final GiftBean gift) {
        View view = aniView.get(gift.getGiftid());//通过ID获取该礼物,并判断动画是否正在执行
        if (view != null) {  //如果该礼物动画存在,则执行放大动画
            TextView item_gift_num = (TextView) view.findViewById(R.id.item_gift_num);
            Object tag = item_gift_num.getTag();
            int num; //保存放大动画已经执行的次数
            if (tag != null && tag instanceof Integer) {
                num = (int) tag;
                num++;
            } else {
                num = 1;
            }
            item_gift_num.setText("X" + num);
            item_gift_num.setTag(num);

            //放大动画
            createScaleAni(item_gift_num).start();

            handler.removeMessages(gift.getGiftid());
            Message message = handler.obtainMessage(gift.getGiftid(), num, 0, gift);
            handler.sendMessageDelayed(message, 2000);


        } else { //如果该View不存在,创建新View 并执行弹出动画

            final View inflate = LayoutInflater.from(getContext()).inflate(R.layout.item_gift_animator, animator_container, false);
            ImageView item_gift_img = (ImageView) inflate.findViewById(R.id.item_gift_img);
            TextView item_gift_num = (TextView) inflate.findViewById(R.id.item_gift_num);
            item_gift_num.setTag(1);

            TextView item_gift_name = (TextView) inflate.findViewById(R.id.item_gift_name);

            item_gift_img.setImageResource(gift.picture);
            item_gift_name.setText(gift.name);
            item_gift_num.setText("X" + 1);
            Animator startAni = createTransAni(inflate, gift);

            startAni.addListener(new SimpleAnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    sv_send_gift_back.animate().alpha(1).start();
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    Message message = handler.obtainMessage(gift.getGiftid(), 1, 0, gift);
                    handler.sendMessageDelayed(message, 2000);
                }
            });
            startAni.start();

        }

    }


    /**
     * 结束动画,移除容器中的View
     */
    private void endAni(final int giftid) {
        final View view = aniView.get(giftid);
        Animator dismissAni = createDismissAni(view);
        dismissAni.addListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animator_container.removeView(view);
                aniView.remove(giftid);
                if (animator_container.getChildCount() == 0) {
                    sv_send_gift_back.animate().alpha(0).start();
                }
            }
        });
        dismissAni.start();
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
    private Animator createTransAni(View view, final GiftBean gift) {
        animator_container.addView(view); //View 添加到容器中
        aniView.put(gift.getGiftid(), view); //添加View到正在执行list中
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


    public static class SimpleAnimatorListener implements Animator.AnimatorListener {//精简代码用

        @Override
        public void onAnimationStart(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
        }

        @Override
        public void onAnimationCancel(Animator animation) {
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    }
}
