package edu.uga.cs.mobliefinalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.os.Bundle;

import android.widget.TableLayout;
import android.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private TabLayout tabLayout;

    private ViewPager2 vp;

    private MyFragmentAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tabLayout);


        vp = findViewById(R.id.viewPager);

        FragmentManager fragmentManager = getSupportFragmentManager();
        adapter = new MyFragmentAdapter(fragmentManager, getLifecycle());
        vp.setAdapter(adapter);



        new TabLayoutMediator(tabLayout, vp,
                ((tab, position) -> {
                    if(position == 0){
                        tab.setText("RIDES");
                    } else if (position == 1) {
                        tab.setText("REQUESTS");
                    }
                    else{
                        tab.setText("PROFILE");
                    }
                })).attach();
    }


    }

     class MyFragmentAdapter extends FragmentStateAdapter{

        public MyFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 0) {
                return OfferFragment.newInstance(position);
            } else if (position == 1) {
                return RidesFragment.newInstance(position);
            }

            return ProfileFragment.newInstance(position);

        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }






