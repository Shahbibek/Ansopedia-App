package com.quiz.ansopedia.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.quiz.ansopedia.R;

import java.util.ArrayList;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> arrayList = new ArrayList<>();

    public CourseAdapter(Context context, ArrayList<String> arrayList) {
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
