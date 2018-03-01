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
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter2.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter2.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter2.sectionedrecyclerviewadapter.StatelessSection;
import io.github.luizgrp.sectionedrecyclerviewadapter2.sectionedrecyclerviewadapter.ViewHolder;


public class Example1Fragment1 extends Fragment {

    private SectionedRecyclerViewAdapter sectionAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ex1, container, false);

        sectionAdapter = new SectionedRecyclerViewAdapter();

        for(char alphabet = 'A'; alphabet <= 'Z';alphabet++) {
            List<String> contacts = getContactsWithLetter(alphabet);
            if (contacts.size() > 0) {
                sectionAdapter.addSection(getContactsSection(contacts, String.valueOf(alphabet)));
            }
        }

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(sectionAdapter);

        return view;
    }

    public StatelessSection getContactsSection(List<String> contacts, String alphabet) {
        SectionParameters sectionParameters = new SectionParameters.Builder(R.layout.section_ex1_item)
                .headerResourceId(R.layout.section_ex1_header)
                .setData(contacts)
                .setTitle(String.valueOf(alphabet))
                .build();
        StatelessSection section = new StatelessSection<String>(sectionParameters) {
            @Override
            public void onBindHeaderViewHolder(ViewHolder holder) {
                holder.setText(R.id.tvTitle, mHeadTitle);
            }

            @Override
            public void onBindItemViewHolder(final ViewHolder holder, String t, int position) {
                holder.setText(R.id.tvItem, t);
                holder.setImageResource(R.id.imgItem, t.hashCode() % 2 == 0 ? R.drawable.ic_face_black_48dp : R.drawable.ic_tag_faces_black_48dp);
                holder.setOnItemClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), String.format("Clicked on position #%s of Section %s", sectionAdapter.getPositionInSection(holder.getAdapterPosition()), mHeadTitle), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        return section;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getActivity() instanceof AppCompatActivity) {
            AppCompatActivity activity = ((AppCompatActivity) getActivity());
            if (activity.getSupportActionBar() != null)
                activity.getSupportActionBar().setTitle(R.string.nav_example1);
        }
    }

    private List<String> getContactsWithLetter(char letter) {
        List<String> contacts = new ArrayList<>();

        for (String contact : getResources().getStringArray(R.array.names)) {
            if (contact.charAt(0) == letter) {
                contacts.add(contact);
            }
        }

        return contacts;
    }
}
