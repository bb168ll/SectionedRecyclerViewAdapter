package io.github.luizgrp.sectionedrecyclerviewadapter.demo;

import android.os.Bundle;
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
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter2.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter2.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter2.sectionedrecyclerviewadapter.StatelessSection;
import io.github.luizgrp.sectionedrecyclerviewadapter2.sectionedrecyclerviewadapter.ViewHolder;


public class Example2Fragment2 extends Fragment {

    private SectionedRecyclerViewAdapter sectionAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ex2, container, false);

        sectionAdapter = new SectionedRecyclerViewAdapter();

        sectionAdapter.addSection(new NewsSection(NewsSection.WORLD));
        sectionAdapter.addSection(new NewsSection(NewsSection.BUSINESS));
        sectionAdapter.addSection(new NewsSection(NewsSection.TECHNOLOGY));
        sectionAdapter.addSection(new NewsSection(NewsSection.SPORTS));

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
                activity.getSupportActionBar().setTitle(R.string.nav_example2);
        }
    }

    private class NewsSection extends StatelessSection {

        final static int WORLD = 0;
        final static int BUSINESS = 1;
        final static int TECHNOLOGY = 2;
        final static int SPORTS = 3;

        final int topic;

        int imgPlaceholderResId;

        NewsSection(int topic) {
            super(new SectionParameters.Builder(R.layout.section_ex2_item)
                    .headerResourceId(R.layout.section_ex2_header)
                    .footerResourceId(R.layout.section_ex2_footer)
                    .setData(new ArrayList<String>())
                    .build());

            this.topic = topic;

            switch (topic) {
                case WORLD:
                    this.mHeadTitle = getString(R.string.world_topic);
                    this.mDatas = getNews(R.array.news_world);
                    this.imgPlaceholderResId = R.drawable.ic_public_black_48dp;
                    break;
                case BUSINESS:
                    this.mHeadTitle = getString(R.string.biz_topic);
                    this.mDatas = getNews(R.array.news_biz);
                    this.imgPlaceholderResId = R.drawable.ic_business_black_48dp;
                    break;
                case TECHNOLOGY:
                    this.mHeadTitle = getString(R.string.tech_topic);
                    this.mDatas = getNews(R.array.news_tech);
                    this.imgPlaceholderResId = R.drawable.ic_devices_other_black_48dp;
                    break;
                case SPORTS:
                    this.mHeadTitle = getString(R.string.sports_topic);
                    this.mDatas = getNews(R.array.news_sports);
                    this.imgPlaceholderResId = R.drawable.ic_directions_run_black_48dp;
                    break;
            }

        }

        private List<String> getNews(int arrayResource) {
            return new ArrayList<>(Arrays.asList(getResources().getStringArray(arrayResource)));
        }

        @Override
        public void onBindItemViewHolder(final ViewHolder holder, Object o, int position) {
            String[] item = ((String)o).split("\\|");
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
    }

}
