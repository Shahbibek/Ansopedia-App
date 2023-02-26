package com.quiz.ansopedia.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.quiz.ansopedia.ChaptersActivity;
import com.quiz.ansopedia.R;
import com.quiz.ansopedia.Utility.Constants;
import com.quiz.ansopedia.models.Subjects;
import com.quiz.ansopedia.sqlite.CoursesHelper;

import java.util.ArrayList;

public class DatabaseSubjectAdapter extends RecyclerView.Adapter<DatabaseSubjectAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Subjects> arrayList = new ArrayList<>();

    public DatabaseSubjectAdapter(Context context, ArrayList<Subjects> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.courses, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvCourses.setText(arrayList.get(position).getSubject_name());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.COLOR = arrayList.get(holder.getAdapterPosition()).getColor();
                String subject = new Gson().toJson(arrayList.get(holder.getAdapterPosition()));
                context.startActivity(new Intent(context, ChaptersActivity.class)
                        .putExtra("subject", subject));
            }
        });
        holder.ivChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CoursesHelper db = new CoursesHelper(context);
                db.deleteData(arrayList.get(holder.getAdapterPosition()).getSubject_name());
                Constants.subjectsArrayList = new ArrayList<>();
                Constants.subjectsArrayList = db.readData(context);
                arrayList.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCourses;
        ImageView ivChecked;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCourses = itemView.findViewById(R.id.tvCourses);
            ivChecked = itemView.findViewById(R.id.ivChecked);
        }
    }
}
