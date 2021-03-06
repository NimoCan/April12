package com.example.graduationprojectgallery.presentation.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.graduationprojectgallery.R;
import com.example.graduationprojectgallery.base.BaseFragment;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;

import static androidx.navigation.fragment.NavHostFragment.findNavController;


public class SearchFragment extends BaseFragment {

    Toolbar toolbar;
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        toolbar = getActivity().findViewById(R.id.app_toolbar);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    public void setNav() {
        BottomNavigationItemView photos = getActivity().findViewById(R.id.photos);
        BottomNavigationItemView albums = getActivity().findViewById(R.id.albums);
        BottomNavigationItemView search = getActivity().findViewById(R.id.search);


        photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (findNavController(SearchFragment.this).getCurrentDestination().getId() != R.id.photosFragment) {
                    findNavigationController().navigate(R.id.action_searchFragment_to_photosFragment);
                }
            }
        });
        // TODO: fix navigation bug :D


        albums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (findNavController(SearchFragment.this).getCurrentDestination().getId() != R.id.albumsFragment) {

                    findNavigationController().navigate(R.id.action_searchFragment_to_albumsFragment);
                }
            }
        });
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setTitle(R.string.search);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    }


        @Override
    public void onResume() {
        super.onResume();
    }
}


