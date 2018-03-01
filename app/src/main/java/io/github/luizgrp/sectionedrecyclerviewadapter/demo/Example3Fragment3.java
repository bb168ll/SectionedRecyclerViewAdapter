package io.github.luizgrp.sectionedrecyclerviewadapter.demo;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import io.github.luizgrp.sectionedrecyclerviewadapter2.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter2.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter2.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter2.sectionedrecyclerviewadapter.ViewHolder;


public class Example3Fragment3 extends Fragment {

    private Handler mHandler = new Handler();

    private SectionedRecyclerViewAdapter sectionAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ex3, container, false);

        sectionAdapter = new SectionedRecyclerViewAdapter();

        NewsSection worldNews = new NewsSection(NewsSection.WORLD);
        NewsSection bizNews = new NewsSection(NewsSection.BUSINESS);
        NewsSection techNews = new NewsSection(NewsSection.TECHNOLOGY);
        NewsSection sportsNews = new NewsSection(NewsSection.SPORTS);

        sectionAdapter.addSection(worldNews);
        sectionAdapter.addSection(bizNews);
        sectionAdapter.addSection(techNews);
        sectionAdapter.addSection(sportsNews);

        loadNews(worldNews);
        loadNews(bizNews);
        loadNews(techNews);
        loadNews(sportsNews);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(sectionAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getActivity() instanceof AppCompatActivity) {
            AppCompatActivity activity = ((AppCompatActivity) getActivity());
            if (activity.getSupportActionBar() != null)
                activity.getSupportActionBar().setTitle(R.string.nav_example3);
        }
    }

    @Override
    public void onDetach() {
        mHandler.removeCallbacksAndMessages(null);

        super.onDetach();
    }

    private void loadNews(final NewsSection section) {
        int timeInMills = new Random().nextInt((7000 - 3000) + 1) + 3000;

        section.setState(Section.State.LOADING);
        section.setHasFooter(false);
        sectionAdapter.notifyDataSetChanged();

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int failed = new Random().nextInt((3 - 1) + 1) + 1;

                if (failed == 1) {
                    section.setState(Section.State.FAILED);
                }
                else {
                    int arrayResource;
                    switch (section.getTopic()) {
                        case NewsSection.WORLD:
                            arrayResource = R.array.news_world;
                            break;
                        case NewsSection.BUSINESS:
                            arrayResource = R.array.news_biz;
                            break;
                        case NewsSection.TECHNOLOGY:
                            arrayResource = R.array.news_tech;
                            break;
                        case NewsSection.SPORTS:
                            arrayResource = R.array.news_sports;
                            break;
                        default:
                            throw new IllegalStateException("Invalid topic");
                    }
                    section.setList(getNews(arrayResource));
                    section.setState(Section.State.LOADED);
                    section.setHasFooter(true);
                }

                sectionAdapter.notifyDataSetChanged();
            }
        }, timeInMills);
    }

    private List<String> getNews(int arrayResource) {
        return new ArrayList<>(Arrays.asList(getResources().getStringArray(arrayResource)));
    }

    private class NewsSection<T> extends Section<T> {

        final static int WORLD = 0;
        final static int BUSINESS = 1;
        final static int TECHNOLOGY = 2;
        final static int SPORTS = 3;

        final int topic;

        int imgPlaceholderResId;

        NewsSection(int topic) {
            super(new SectionParameters.Builder(R.layout.section_ex3_item)
                    .headerResourceId(R.layout.section_ex3_header)
                    .footerResourceId(R.layout.section_ex3_footer)
                    .failedResourceId(R.layout.section_ex3_failed)
                    .loadingResourceId(R.layout.section_ex3_loading)
                    .setData(Collections.emptyList())
                    .build());

            this.topic = topic;

            switch (topic) {
                case WORLD:
                    this.mHeadTitle = getString(R.string.world_topic);
                    this.imgPlaceholderResId = R.drawable.ic_public_black_48dp;
                    break;
                case BUSINESS:
                    this.mHeadTitle = getString(R.string.biz_topic);
                    this.imgPlaceholderResId = R.drawable.ic_business_black_48dp;
                    break;
                case TECHNOLOGY:
                    this.mHeadTitle = getString(R.string.tech_topic);
                    this.imgPlaceholderResId = R.drawable.ic_devices_other_black_48dp;
                    break;
                case SPORTS:
                    this.mHeadTitle = getString(R.string.sports_topic);
                    this.imgPlaceholderResId = R.drawable.ic_directions_run_black_48dp;
                    break;
            }

        }

        int getTopic() {
            return topic;
        }

        public void setList(List<T> list) {
            this.mDatas = list;
        }

        @Override
        public void onBindItemViewHolder(final ViewHolder holder, T t, int position) {
            String bean = (String) t;
            String[] item = bean.split("\\|");
            holder.setText(R.id.tvHeader, item[0]);
            holder.setText(R.id.tvDate, item[1]);
            holder.setImageResource(R.id.imgItem, imgPlaceholderResId);

            holder.setOnItemClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), String.format("Clicked on position #%s of Section %s", sectionAdapter.getPositionInSection(holder.getAdapterPosition()), mHeadTitle), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onBindHeaderViewHolder(ViewHolder holder) {
            holder.setText(R.id.tvTitle, mHeadTitle);
        }

        @Override
        public void onBindFooterViewHolder(ViewHolder holder) {
            holder.setOnItemClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), String.format("Clicked on footer of Section %s", mHeadTitle), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onBindFailedViewHolder(ViewHolder holder) {
            holder.setOnItemClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadNews(Example3Fragment3.NewsSection.this);
                }
            });
        }
    }

}
