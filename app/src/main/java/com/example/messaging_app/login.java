package com.example.messaging_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.TableLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class login extends AppCompatActivity {
    MaterialToolbar toolbar;
    ViewPager2 vp2;
    FragmentStateAdapter pagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        vp2= findViewById(R.id.viewPager);
        pagerAdapter = new ScreenSlidePagerAdapter(this);
        vp2.setAdapter(pagerAdapter);
        TabLayout tabs;
        tabs= findViewById(R.id.tabs);
        new TabLayoutMediator(tabs, vp2,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override public void onConfigureTab(TabLayout.Tab tab, int position) {
                        if(position==0) tab.setText("Chats");
                        else tab.setText("People");
                    }
                }).attach();



    }

    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            if(position==0) return new InboxFragment();
            else return new PeopleFragment();}

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}