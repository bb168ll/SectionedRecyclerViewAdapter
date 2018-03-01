package io.github.luizgrp.sectionedrecyclerviewadapter.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter2.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter2.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter2.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter2.sectionedrecyclerviewadapter.StatelessSection;
import io.github.luizgrp.sectionedrecyclerviewadapter2.sectionedrecyclerviewadapter.ViewHolder;


public class Example7Fragment7 extends Fragment implements SearchView.OnQueryTextListener {

    private SectionedRecyclerViewAdapter sectionAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ex7, container, false);

        sectionAdapter = new SectionedRecyclerViewAdapter();

        for(char alphabet = 'A'; alphabet <= 'Z';alphabet++) {
            List<String> contacts = getContactsWithLetter(alphabet);

            if (contacts.size() > 0) {
                ContactsSection contactsSection = new ContactsSection(String.valueOf(alphabet), contacts);
                sectionAdapter.addSection(contactsSection);
            }
        }

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(sectionAdapter);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getActivity() instanceof AppCompatActivity) {
            AppCompatActivity activity = ((AppCompatActivity) getActivity());
            if (activity.getSupportActionBar() != null)
                activity.getSupportActionBar().setTitle(R.string.nav_example7);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_ex7, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextChange(String query) {

        // getSectionsMap requires library version 1.0.4+
        for (Section section : sectionAdapter.getSectionsMap().values()) {
            if (section instanceof FilterableSection) {
                ((FilterableSection)section).filter(query);
            }
        }
        sectionAdapter.notifyDataSetChanged();

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
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

    private class ContactsSection<T> extends StatelessSection<T> implements FilterableSection {

        List<String> filteredList;
        List<String> list;

        ContactsSection(String title, List<String> list) {
            super(new SectionParameters.Builder(R.layout.section_ex7_item)
                    .headerResourceId(R.layout.section_ex7_header)
                    .setTitle(title)
                    .setData(list)
                    .build());

            this.list = list;
            this.filteredList = new ArrayList<>(list);
        }

        @Override
        public int getContentItemsTotal() {
            return filteredList.size();
        }

        @Override
        public void onBindItemViewHolder(final ViewHolder holder, T t, int position) {
            String name = filteredList.get(position);
            holder.setText(R.id.tvItem, name);
            holder.setImageResource(R.id.imgItem,
                    name.hashCode() % 2 == 0 ? R.drawable.ic_face_black_48dp : R.drawable.ic_tag_faces_black_48dp);

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
        public void filter(String query) {
            if (TextUtils.isEmpty(query)) {
                filteredList = new ArrayList<>(list);
                this.setVisible(true);
            }
            else {
                filteredList.clear();
                for (String value : list) {
                    if (value.toLowerCase().contains(query.toLowerCase())) {
                        filteredList.add(value);
                    }
                }

                this.setVisible(!filteredList.isEmpty());
            }
        }
    }

    interface FilterableSection {
        void filter(String query);
    }
}
