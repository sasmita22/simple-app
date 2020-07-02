package com.example.simpleapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.simpleapp.R;
import com.example.simpleapp.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    public enum ActionType {
        LIKE("true"),
        UNLIKE("false");

        private String string;

        ActionType(String string) {
            this.string = string;
        }
    }

    private List<Movie> movieList;
    private final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w500/";
    private Context context;
    private OnLikeListener listener;

    public MovieAdapter(Context context) {
        this.context = context;
        movieList = new ArrayList<>();
    }

    public void updateMovieList(List<Movie> movieList){
        this.movieList.clear();
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = movieList.get(position);

        holder.textTitle.setText(movie.getTitle());
        holder.textVote.setText(movie.getVoteAverage());
        Glide.with(context)
                .load(BASE_IMAGE_URL + movie.getPosterPath())
                .centerCrop()
                .into(holder.imageView);

        setMovieLikeOrNot(holder, movie.isLiked());


        holder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((ImageButton) v).getContentDescription().equals(ActionType.LIKE.string)){
                    if (listener != null) {
                        setMovieLikeOrNot(holder, false);
                        listener.onLikeClick(movie, ActionType.UNLIKE);
                    }
                }else{
                    if (listener != null) {
                        setMovieLikeOrNot(holder, true);
                        listener.onLikeClick(movie, ActionType.LIKE);
                    }
                }
            }
        });
    }

    private void setMovieLikeOrNot(ViewHolder viewHolder,boolean isLiked){
        String description;
        @DrawableRes int resId;

        if (isLiked){
            resId = R.drawable.ic_filled_star;
            description = ActionType.LIKE.string;
        } else {
            resId = R.drawable.ic_outline_star;
            description = ActionType.UNLIKE.string;
        }

        viewHolder.btnLike.setContentDescription(description);
        viewHolder.btnLike.setImageDrawable(context.getResources().getDrawable(resId));
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textTitle;
        TextView textVote;
        ImageButton btnLike;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.movie_image);
            textTitle = itemView.findViewById(R.id.tv_title);
            textVote = itemView.findViewById(R.id.tv_vote);
            btnLike = itemView.findViewById(R.id.btn_like);
        }
    }

    public void setOnLikeListener(OnLikeListener listener){
        this.listener = listener;
    }

    public interface OnLikeListener{
        void onLikeClick(Movie movie, ActionType actionType);
    }
}
