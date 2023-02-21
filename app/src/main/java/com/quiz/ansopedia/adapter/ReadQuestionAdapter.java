package com.quiz.ansopedia.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.quiz.ansopedia.R;
import com.quiz.ansopedia.ReadQuestionsActivity;
import com.quiz.ansopedia.Utility.Constants;
import com.quiz.ansopedia.models.Questions;

import java.util.ArrayList;

public class ReadQuestionAdapter extends RecyclerView.Adapter<ReadQuestionAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Questions> arrayList = new ArrayList<>();
    boolean isShowingAnswer = false;
    int count = 0;
    public ReadQuestionAdapter(Context context, ArrayList<Questions> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        count++;
        if (count <= arrayList.size()) {
            view = LayoutInflater.from(context).inflate(R.layout.read_question, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.previous_next, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int i = holder.getAdapterPosition();
        if (count <= arrayList.size()) {
            holder.colorOptions.getBackground().setTint(Color.parseColor(Constants.COLOR));
            holder.cvDescription.setVisibility(View.GONE);
            holder.option1.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.option2.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.option3.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.option4.setBackgroundColor(Color.parseColor("#FFFFFF"));

            holder.srNoQuestion.setText(holder.getAdapterPosition() + 1 + ". ");
            holder.quiz_questions.setText(arrayList.get(holder.getAdapterPosition()).getQuestion_title());
            holder.option1.setText("a. " + arrayList.get(i).getOptions().get(0).getOpt1());
            holder.option2.setText("b. " + arrayList.get(i).getOptions().get(0).getOpt2());
            holder.option3.setText("c. " + arrayList.get(i).getOptions().get(0).getOpt3());
            holder.option4.setText("d. " + arrayList.get(i).getOptions().get(0).getOpt4());
            holder.tvDescription.setText(arrayList.get(i).getOptions().get(0).getDescription());

            holder.ivDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.cvDescription.getVisibility() == View.GONE) {
                        holder.cvDescription.setVisibility(View.VISIBLE);
                    } else {
                        holder.cvDescription.setVisibility(View.GONE);
                    }
                }
            });

            holder.ivView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isShowingAnswer) {
                        isShowingAnswer = true;
                        if (arrayList.get(i).getOptions().get(0).getCorrectAnswer().trim().equalsIgnoreCase(arrayList.get(i).getOptions().get(0).getOpt1().trim())) {
                            holder.option1.setBackgroundColor(Color.parseColor("#C6D33C"));
                        } else if (arrayList.get(i).getOptions().get(0).getCorrectAnswer().trim().equalsIgnoreCase(arrayList.get(i).getOptions().get(0).getOpt2().trim())) {
                            holder.option2.setBackgroundColor(Color.parseColor("#C6D33C"));
                        } else if (arrayList.get(i).getOptions().get(0).getCorrectAnswer().trim().equalsIgnoreCase(arrayList.get(i).getOptions().get(0).getOpt3().trim())) {
                            holder.option3.setBackgroundColor(Color.parseColor("#C6D33C"));
                        } else if (arrayList.get(i).getOptions().get(0).getCorrectAnswer().trim().equalsIgnoreCase(arrayList.get(i).getOptions().get(0).getOpt4().trim())) {
                            holder.option4.setBackgroundColor(Color.parseColor("#C6D33C"));
                        }
                    } else {
                        isShowingAnswer = false;
                        holder.option1.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        holder.option2.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        holder.option3.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        holder.option4.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    }
                }
            });

            holder.ivShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Question");
                    intent.putExtra(Intent.EXTRA_TEXT, arrayList.get(i).getQuestion_title() +
                            "\n\na. " + arrayList.get(i).getOptions().get(0).getOpt1() +
                            "\nb. " + arrayList.get(i).getOptions().get(0).getOpt2() +
                            "\nc. " + arrayList.get(i).getOptions().get(0).getOpt3() +
                            "\nd. " + arrayList.get(i).getOptions().get(0).getOpt4() +
                            "\n\nAnswer: " + arrayList.get(i).getOptions().get(0).getCorrectAnswer() +
                            "\n\n\n" + "Share App: " + "https://play.google.com/store/apps/details?id=com.quiz.ansopedia"
                    );
                    context.startActivity(Intent.createChooser(intent, "choose one"));
                }
            });
        } else {
            holder.nextquestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ReadQuestionsActivity.lower = ReadQuestionsActivity.lower + 10;
                    ReadQuestionsActivity.upper = ReadQuestionsActivity.upper +10;
                    ReadQuestionsActivity.setRecyclerView1(ReadQuestionsActivity.lower, ReadQuestionsActivity.upper);
                }
            });

            holder.previousquestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ReadQuestionsActivity.lower = ReadQuestionsActivity.lower - 10;
                    ReadQuestionsActivity.upper = ReadQuestionsActivity.upper - 10;
                    ReadQuestionsActivity.setRecyclerView(ReadQuestionsActivity.lower, ReadQuestionsActivity.upper);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView srNoQuestion, quiz_questions, option1, option2, option3, option4, tvDescription;
        ImageView ivView, ivDescription, ivShare;
        CardView cvDescription;
        LinearLayout colorOptions;
        ImageView nextquestion, previousquestion;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            srNoQuestion = itemView.findViewById(R.id.srNoQuestion);
            quiz_questions = itemView.findViewById(R.id.quiz_questions);
            option1 = itemView.findViewById(R.id.option1);
            option2 = itemView.findViewById(R.id.option2);
            option3 = itemView.findViewById(R.id.option3);
            option4 = itemView.findViewById(R.id.option4);
            ivView = itemView.findViewById(R.id.ivView);
            ivShare = itemView.findViewById(R.id.ivShare);
            ivDescription = itemView.findViewById(R.id.ivDescription);
            cvDescription = itemView.findViewById(R.id.cvDescription);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            colorOptions = itemView.findViewById(R.id.colorOptions);
            previousquestion = itemView.findViewById(R.id.previousquestion);
            nextquestion = itemView.findViewById(R.id.nextquestion);
        }
    }
}
