package com.quiz.ansopedia.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.quiz.ansopedia.ChaptersActivity;
import com.quiz.ansopedia.R;
import com.quiz.ansopedia.Utility.Constants;
import com.quiz.ansopedia.Utility.Utility;
import com.quiz.ansopedia.models.Subjects;

import java.util.ArrayList;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Subjects> arrayList = new ArrayList<>();

    public CourseAdapter(Context context, ArrayList<Subjects> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_categories, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.clCourse.setBackgroundColor(Color.parseColor(arrayList.get(position).getColor()));
        holder.tvCourseTitle.setText(Utility.toCapitalizeFirstLetter(arrayList.get(position).getSubject_name()));

        Glide.with(context).load(Uri.parse(arrayList.get(position).getImage())).into(holder.ivCourse);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.COLOR = arrayList.get(holder.getAdapterPosition()).getColor();
                String subject = new Gson().toJson(arrayList.get(holder.getAdapterPosition()));
                context.startActivity(new Intent(context, ChaptersActivity.class)
                        .putExtra("subject", subject));
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout clCourse;
        ImageView ivCourse;
        TextView tvCourseTitle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            clCourse = itemView.findViewById(R.id.clCourse);
            ivCourse = itemView.findViewById(R.id.ivCourse);
            tvCourseTitle = itemView.findViewById(R.id.tvCourseTitle);
        }
    }
}
