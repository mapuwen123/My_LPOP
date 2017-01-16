package com.mapuw.lpop.ui.userhome;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;

import com.bumptech.glide.Glide;
import com.mapuw.lpop.R;
import com.mapuw.lpop.base.BaseActivity;
import com.mapuw.lpop.databinding.ActivityUserHomeBinding;
import com.mapuw.lpop.ui.userhome.adapter.UserHomeFragmentAdapter;
import com.mapuw.lpop.ui.userhome.fragment.OriginalFragment;
import com.mapuw.lpop.ui.userhome.fragment.PictureFragment;
import com.mapuw.lpop.ui.userhome.fragment.all.AllFragment;
import com.sina.weibo.sdk.openapi.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserHomeActivity extends BaseActivity implements AllFragment.OnFragmentInteractionListener,
                                                                OriginalFragment.OnFragmentInteractionListener,
                                                                    PictureFragment.OnFragmentInteractionListener{

    private ActivityUserHomeBinding binding;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void dataBindingView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_home);
    }

    @Override
    protected void appBarInit() {
        user = (User) getIntent().getSerializableExtra("USER");
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        binding.collapsingToolbar.setTitle(user.screen_name);
        Glide.with(this)
                .load(user.cover_image_phone)
                .into(binding.backdrop);
    }

    @Override
    protected void dataInit() {
        List<Fragment> list = new ArrayList<>();
        AllFragment all = AllFragment.newInstance(user);
        OriginalFragment original = OriginalFragment.newInstance(user);
        PictureFragment picture = PictureFragment.newInstance(user);

        list.add(all);
        list.add(original);
        list.add(picture);

        binding.idViewpager.setAdapter(new UserHomeFragmentAdapter(getSupportFragmentManager(), list));
        binding.idTab.setupWithViewPager(binding.idViewpager);
    }

    @Override
    protected void eventInit() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_user_home_menu, menu);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
