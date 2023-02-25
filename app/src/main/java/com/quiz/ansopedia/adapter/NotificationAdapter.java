package com.quiz.ansopedia.adapter;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.quiz.ansopedia.R;
import com.quiz.ansopedia.Utility.Utility;
import com.quiz.ansopedia.models.Notification;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Notification> arrayList = new ArrayList<>();

    public NotificationAdapter(Context context, ArrayList<Notification> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int i = holder.getAdapterPosition();
        holder.tvMainHead.setMovementMethod(LinkMovementMethod.getInstance());
        holder.tvBody.setMovementMethod(LinkMovementMethod.getInstance());
        holder.tvTime.setMovementMethod(LinkMovementMethod.getInstance());

        holder.tvMainHead.setText(Html.fromHtml(arrayList.get(i).getTitle()));
        holder.tvBody.setText(Html.fromHtml(arrayList.get(i).getMessage()));
        holder.tvTime.setText(arrayList.get(i).getTime());

        try{
            Instant now = Instant.now();
//        Instant then = Instant.parse("2023-02-25T18:17:20.055Z");
            Instant then = Instant.parse(arrayList.get(i).getTime());
            Duration duration = Duration.between(then, now);
            String diff = Utility.calculateDate(duration);
            holder.tvTime.setText(String.valueOf(diff));
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime, tvBody, tvMainHead;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvBody = itemView.findViewById(R.id.tvBody);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvMainHead = itemView.findViewById(R.id.tvMainHead);
        }
    }
}
