package io.github.luizgrp.sectionedrecyclerviewadapter.demo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.github.luizgrp.sectionedrecyclerviewadapter2.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter2.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter2.sectionedrecyclerviewadapter.StatelessSection;
import io.github.luizgrp.sectionedrecyclerviewadapter2.sectionedrecyclerviewadapter.ViewHolder;

public class Example8Fragment8 extends Fragment {

    private static final Random RANDOM = new Random();

    private SectionedRecyclerViewAdapter sectionAdapter;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ex8, container, false);

        sectionAdapter = new SectionedRecyclerViewAdapter();

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

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(glm);
        recyclerView.setAdapter(sectionAdapter);

        addNewSectionToAdapter();

        addNewSectionToAdapter();

        view.findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewSectionToAdapter();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getActivity() instanceof AppCompatActivity) {
            AppCompatActivity activity = ((AppCompatActivity) getActivity());
            if (activity.getSupportActionBar() != null)
                activity.getSupportActionBar().setTitle(R.string.nav_example8);
        }
    }

    private void addNewSectionToAdapter() {
        String randomNumber = getRandomStringNumber();
        String sectionTag = String.format("section%sTag", randomNumber);

        NameSection section = new NameSection<Person>(sectionTag,
                getString(R.string.group_title, randomNumber));

        sectionAdapter.addSection(sectionTag, section);

        int sectionPos = sectionAdapter.getSectionPosition(sectionTag);

        sectionAdapter.notifyItemInserted(sectionPos);

        recyclerView.smoothScrollToPosition(sectionPos);
    }

    @NonNull
    private String getRandomStringNumber() {
        return String.valueOf(RANDOM.nextInt(100000));
    }

    private Person getRandomName() {
        String[] names = getResources().getStringArray(R.array.names);

        String[] randomName = names[RANDOM.nextInt(names.length)].split("\\|");

        return new Person(randomName[0], "ID #" + getRandomStringNumber());
    }

    private class NameSection<T> extends StatelessSection<T> {

        final String TAG;
        String title;
        List<Person> list;

        NameSection(String tag, String title) {
            super(new SectionParameters.Builder(R.layout.section_ex8_item)
                    .headerResourceId(R.layout.section_ex8_header)
                    .setTitle(title)
                    .setData(new ArrayList())
                    .build());

            this.TAG = tag;
            this.title = title;
            this.list = (List<Person>) mDatas;
        }

        @Override
        public int getContentItemsTotal() {
            return list.size();
        }

        @Override
        public void onBindItemViewHolder(final ViewHolder holder, T t, int position) {
            Person person = (Person) t;
            holder.setText(R.id.tvItem, person.getName());
            holder.setText(R.id.tvSubItem, person.getId());
            holder.setImageResource(R.id.imgItem,
                    person.getName().hashCode() % 2 == 0 ? R.drawable.ic_face_black_48dp : R.drawable.ic_tag_faces_black_48dp);

            holder.setOnItemClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = holder.getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        int positionInSection = sectionAdapter.getPositionInSection(adapterPosition);

                        list.remove(positionInSection);
                        sectionAdapter.notifyItemRemovedFromSection(TAG, positionInSection);
                    }
                }
            });
        }

        @Override
        public void onBindHeaderViewHolder(ViewHolder holder) {
            holder.setText(R.id.tvTitle, mHeadTitle);
            holder.setOnClickListener(R.id.btnAdd, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int positionToInsertItemAt = 0;

                    list.add(positionToInsertItemAt, getRandomName());

                    sectionAdapter.notifyItemInsertedInSection(TAG, positionToInsertItemAt);
                }
            });

            holder.setOnClickListener(R.id.btnClear, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int contentItemsTotal = getContentItemsTotal();

                    list.clear();

                    sectionAdapter.notifyItemRangeRemovedFromSection(TAG, 0, contentItemsTotal);
                }
            });
        }
    }

    private class Person {
        String name;
        String id;

        Person(String name, String id) {
            this.name = name;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
