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
import com.quiz.ansopedia.R;
import com.quiz.ansopedia.ReadQuestionsActivity;
import com.quiz.ansopedia.Utility.Utility;
import com.quiz.ansopedia.models.Chapters;

import java.util.ArrayList;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Chapters> arrayList = new ArrayList<>();
    private String subject;
    public ChapterAdapter(Context context, ArrayList<Chapters> arrayList, String subject) {
        this.context = context;
        this.arrayList = arrayList;
        this.subject = subject;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvChapter.setText(Utility.toCapitalizeFirstLetter(arrayList.get(holder.getAdapterPosition()).getChapter_name()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String chapter = new Gson().toJson(arrayList.get(holder.getAdapterPosition()));
                context.startActivity(new Intent(context, ReadQuestionsActivity.class)
                        .putExtra("chapter", chapter)
                        .putExtra("subject", subject));
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvChapter;
        ImageView ivChecked;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChapter = itemView.findViewById(R.id.tvChapter);
            ivChecked = itemView.findViewById(R.id.ivChecked);
        }
    }
}
