package io.github.luizgrp.sectionedrecyclerviewadapter.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
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


public class Example5Fragment5 extends Fragment {

    private SectionedRecyclerViewAdapter sectionAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ex5, container, false);

        sectionAdapter = new SectionedRecyclerViewAdapter();

        sectionAdapter.addSection(new MovieSection(getString(R.string.top_rated_movies_topic), getTopRatedMoviesList()));
        sectionAdapter.addSection(new MovieSection(getString(R.string.most_popular_movies_topic), getMostPopularMoviesList()));

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);

        GridLayoutManager glm = new GridLayoutManager(getContext(), 2);
        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch(sectionAdapter.getSectionItemViewType(position)) {
                    case SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER:
                        return 2;
                    default:
                        return 1;
                }
            }
        });
        recyclerView.setLayoutManager(glm);
        recyclerView.setAdapter(sectionAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getActivity() instanceof AppCompatActivity) {
            AppCompatActivity activity = ((AppCompatActivity) getActivity());
            if (activity.getSupportActionBar() != null)
                activity.getSupportActionBar().setTitle(R.string.nav_example5);
        }
    }

    private List<Movie> getTopRatedMoviesList() {
        List<String> arrayList = new ArrayList<>(Arrays.asList(getResources()
                .getStringArray(R.array.top_rated_movies)));

        List<Movie> movieList = new ArrayList<>();

        for (String str : arrayList) {
            String[] array = str.split("\\|");
            movieList.add(new Movie(array[0], array[1]));
        }

        return movieList;
    }

    private List<Movie> getMostPopularMoviesList() {
        List<String> arrayList = new ArrayList<>(Arrays.asList(getResources()
                .getStringArray(R.array.most_popular_movies)));

        List<Movie> movieList = new ArrayList<>();

        for (String str : arrayList) {
            String[] array = str.split("\\|");
            movieList.add(new Movie(array[0], array[1]));
        }

        return movieList;
    }

    private class MovieSection<T> extends StatelessSection<T> {

        MovieSection(String title, List<Movie> list) {
            super(new SectionParameters.Builder(R.layout.section_ex5_item)
                    .headerResourceId(R.layout.section_ex5_header)
                    .setTitle(title)
                    .setData(list)
                    .build());
        }

        @Override
        public void onBindItemViewHolder(final ViewHolder holder, T t, int position) {
            Movie movie = (Movie) t;
            holder.setText(R.id.tvItem, movie.getName());
            holder.setText(R.id.tvSubItem, movie.getCategory());
            holder.setOnItemClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), String.format("Clicked on position #%s of Section %s",
                            sectionAdapter.getPositionInSection(holder.getAdapterPosition()), mHeadTitle),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onBindHeaderViewHolder(ViewHolder holder) {
            holder.setText(R.id.tvTitle, mHeadTitle);
            holder.setOnClickListener(R.id.btnMore, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), String.format("Clicked on more button from the header of Section %s",
                            mHeadTitle),
                            Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    private class Movie {
        String name;
        String category;

        Movie(String name, String category) {
            this.name = name;
            this.category = category;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }
    }
}
