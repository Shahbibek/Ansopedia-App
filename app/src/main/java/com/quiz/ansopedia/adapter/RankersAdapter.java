package com.quiz.ansopedia.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.quiz.ansopedia.R;
import com.quiz.ansopedia.models.UserDetail;

import java.util.ArrayList;

public class RankersAdapter extends RecyclerView.Adapter<RankersAdapter.ViewHolder> {
    private Context context;
    private ArrayList<UserDetail> arrayList = new ArrayList<>();

    public RankersAdapter(Context context, ArrayList<UserDetail> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public RankersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.leader_board, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RankersAdapter.ViewHolder holder, int position) {
        int i = holder.getAdapterPosition();
        try {
            holder.tvRankerName.setText(arrayList.get(i).getName());
            holder.tvRankerScore.setText(String.valueOf(arrayList.get(i).getCoins()));
            holder.tvRankCount.setText(String.valueOf(i + 1));
            if (!arrayList.get(i).getAvatar().substring(33).equalsIgnoreCase("undefined")) {
                try{
                    Glide.with(context).load(arrayList.get(i).getAvatar()).into(holder.ivRankerImage);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRankerScore, tvRankerName, tvRankCount;
        ImageView ivRankerImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRankCount = itemView.findViewById(R.id.tvRankCount);
            tvRankerScore = itemView.findViewById(R.id.tvRankerScore);
            tvRankerName = itemView.findViewById(R.id.tvRankerName);
            ivRankerImage = itemView.findViewById(R.id.ivRankerImage);
        }
    }
}
