package com.spearbothy.transitiondemo;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;

import com.spearbothy.transitiondemo.behavior.BehaviorActivity;
import com.spearbothy.transitiondemo.custom.ShareElementActivity;
import com.spearbothy.transitiondemo.ele.EleMainActivity;
import com.spearbothy.transitiondemo.transition.ColorTransitionActivity;
import com.spearbothy.transitiondemo.transition.SceneActivity;
import com.spearbothy.transitiondemo.transition.TransitionExampleActivity;

/**
 * Created by mahao on 17-5-15.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Intent mIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv_explode_transition).setOnClickListener(this);
        findViewById(R.id.tv_slide_transition).setOnClickListener(this);
        findViewById(R.id.tv_fade_transition).setOnClickListener(this);
        findViewById(R.id.tv_share_elements).setOnClickListener(this);
        findViewById(R.id.tv_reveal).setOnClickListener(this);
        findViewById(R.id.tv_custom_share).setOnClickListener(this);
        findViewById(R.id.tv_transition_example).setOnClickListener(this);
        findViewById(R.id.tv_ele_search).setOnClickListener(this);
        findViewById(R.id.tv_scene).setOnClickListener(this);
        findViewById(R.id.tv_transition_color).setOnClickListener(this);
        findViewById(R.id.tv_behavior).setOnClickListener(this);
        findViewById(R.id.tv_share_elements_vp).setOnClickListener(this);
        mIntent = new Intent();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_scene:
                //  基本的Transition动画
                startActivity(new Intent(this, SceneActivity.class));
                break;
            case R.id.tv_transition_example:
                // 延时动画
                startActivity(new Intent(this, TransitionExampleActivity.class));
                break;
            case R.id.tv_transition_color:
                startActivity(new Intent(this, ColorTransitionActivity.class));
                break;
            case R.id.tv_explode_transition:
                mIntent.setClass(this, TransitionActivity.class);
                mIntent.putExtra("transition", "explode");
                startActivity(mIntent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                break;
            case R.id.tv_slide_transition:
                mIntent.setClass(this, TransitionActivity.class);
                mIntent.putExtra("transition", "slide");
                startActivity(mIntent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                break;
            case R.id.tv_fade_transition:
                mIntent.setClass(this, TransitionActivity.class);
                mIntent.putExtra("transition", "fade");
                startActivity(mIntent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                break;
            case R.id.tv_share_elements:
                mIntent.setClass(this, ShareElementsActivity.class);
                ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(this
                        , Pair.create(findViewById(R.id.img_share), "share")
                        , Pair.create(findViewById(R.id.tv_share), "share_text"));
              /*  //5.0以下兼容
                ActivityOptionsCompat activityOptionsCompat1 = ActivityOptionsCompat.makeSceneTransitionAnimation(this
                        , Pair.create(findViewById(R.id.img_share), "share")
                        , Pair.create(findViewById(R.id.tv_share), "share_text"));*/
                startActivity(mIntent, transitionActivityOptions.toBundle());
                break;
            case R.id.tv_share_elements_vp:
                startActivity(new Intent(this, ShareElementViewPagerActivity.class));
                break;
            case R.id.tv_reveal:
                mIntent.setClass(this, RevealActivity.class);
                ActivityOptions transitionActivityOptions2 = ActivityOptions.makeSceneTransitionAnimation(this, findViewById(R.id.img_reveal_share), "share");
                //5.0以下兼容
                //ActivityOptionsCompat activityOptionsCompat2 = ActivityOptionsCompat.makeSceneTransitionAnimation(SceneActivity.this, findViewById(R.id.img_reveal_share), "share");
                startActivity(mIntent, transitionActivityOptions2.toBundle());
                break;
            case R.id.tv_custom_share:
                //mIntent.setClass(this, ShareElementsActivity.class);
                startActivity(new Intent(this, ShareElementActivity.class));
                break;
            case R.id.tv_ele_search:
                startActivity(new Intent(this, EleMainActivity.class));
                break;
            case R.id.tv_behavior:
                startActivity(new Intent(this, BehaviorActivity.class));
                break;
        }
    }
}
