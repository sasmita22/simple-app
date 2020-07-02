package com.example.simpleapp.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.simpleapp.R;
import com.example.simpleapp.adapter.MovieAdapter;
import com.example.simpleapp.model.Movie;
import com.example.simpleapp.model.ResponseResult;
import com.example.simpleapp.viewmodel.MovieViewModel;

import java.util.List;

public class LikedFragment extends Fragment {

    private MovieViewModel viewModel;
    private MovieAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(getActivity()).get(MovieViewModel.class);
        initRv();
        observeLiveData();
    }

    private void observeLiveData() {
        viewModel.fetchLikedMovie();

        viewModel.getLiveData().observe(getViewLifecycleOwner(), new Observer<ResponseResult<List<Movie>>>() {
            @Override
            public void onChanged(ResponseResult<List<Movie>> result) {
                if (result.getResult() != null){
                    adapter.updateMovieList(result.getResult());
                }else{
                    Toast.makeText(getContext(), result.getError().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initRv() {
        adapter = new MovieAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
}