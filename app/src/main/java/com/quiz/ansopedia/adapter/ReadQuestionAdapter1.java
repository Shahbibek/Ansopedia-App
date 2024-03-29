package com.quiz.ansopedia.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
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

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.quiz.ansopedia.R;
import com.quiz.ansopedia.ReadQuestionsActivity;
import com.quiz.ansopedia.Utility.Constants;
import com.quiz.ansopedia.models.Questions;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ReadQuestionAdapter1 extends RecyclerView.Adapter<ReadQuestionAdapter1.ViewHolder> {
    private Context context;
    private ArrayList<Questions> arrayList = new ArrayList<>();
    boolean isShowingAnswer = false;
    int count = 0;

    public ReadQuestionAdapter1(Context context, ArrayList<Questions> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ReadQuestionAdapter1.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        count++;
        if (count <= arrayList.size()) {
            view = LayoutInflater.from(context).inflate(R.layout.read_question, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.previous_next, parent, false);
        }
        return new ReadQuestionAdapter1.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReadQuestionAdapter1.ViewHolder holder, int position) {
        int i = holder.getAdapterPosition();
        int j = holder.getAdapterPosition() + ReadQuestionsActivity.lower + 1;
        if (count <= arrayList.size()) {
            List<TextView> tvOptionsList = new ArrayList<TextView>();
            tvOptionsList.add(holder.option1);
            tvOptionsList.add(holder.option2);
            tvOptionsList.add(holder.option3);
            tvOptionsList.add(holder.option4);
            tvOptionsList.add(holder.option5);
            for(TextView tvOption: tvOptionsList){
                tvOption.setBackgroundColor(Color.parseColor("#FFFFFF"));
                tvOption.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextView clickedTvOption = (TextView)view;
                        int clickedTvOptionTag = Integer.parseInt(view.getTag().toString());
                        updateOption(clickedTvOptionTag, tvOptionsList, i);
                    }
                });
            }


            holder.colorOptions.getBackground().setTint(Color.parseColor(Constants.COLOR));
//            holder.nextquestion.getBackground().setTint(Color.parseColor(Constants.COLOR));
//            holder.previousquestion.getBackground().setTint(Color.parseColor(Constants.COLOR));
            holder.cvDescription.setVisibility(View.GONE);
//            holder.option1.setBackgroundColor(Color.parseColor("#FFFFFF"));
//            holder.option2.setBackgroundColor(Color.parseColor("#FFFFFF"));
//            holder.option3.setBackgroundColor(Color.parseColor("#FFFFFF"));
//            holder.option4.setBackgroundColor(Color.parseColor("#FFFFFF"));

            holder.srNoQuestion.setText(j + ". ");
            holder.quiz_questions.setText(arrayList.get(holder.getAdapterPosition()).getQuestion_title());
//            holder.option1.setText("a. " + arrayList.get(i).getOptions().get(0).getOpt1());
//            holder.option2.setText("b. " + arrayList.get(i).getOptions().get(0).getOpt2());
//            holder.option3.setText("c. " + arrayList.get(i).getOptions().get(0).getOpt3());
//            holder.option4.setText("d. " + arrayList.get(i).getOptions().get(0).getOpt4());
            try{
                if (arrayList.get(i).getOptions().get(0).getOpt1() != null) {
                    holder.option1.setVisibility(View.VISIBLE);
                    holder.option1.setText("a. " + arrayList.get(i).getOptions().get(0).getOpt1());
                }
                if (arrayList.get(i).getOptions().get(0).getOpt2() != null) {
                    holder.option2.setVisibility(View.VISIBLE);
                    holder.option2.setText("b. " + arrayList.get(i).getOptions().get(0).getOpt2());
                }
                if (arrayList.get(i).getOptions().get(0).getOpt3() != null) {
                    holder.option3.setVisibility(View.VISIBLE);
                    holder.option3.setText("c. " + arrayList.get(i).getOptions().get(0).getOpt3());
                }
                if (arrayList.get(i).getOptions().get(0).getOpt4() != null) {
                    holder.option4.setVisibility(View.VISIBLE);
                    holder.option4.setText("d. " + arrayList.get(i).getOptions().get(0).getOpt4());
                }
                if (arrayList.get(i).getOptions().get(0).getOpt5() != null) {
                    holder.option5.setVisibility(View.VISIBLE);
                    holder.option5.setText("e. " + arrayList.get(i).getOptions().get(0).getOpt5());
                }
            }catch (Exception e){
                e.printStackTrace();
                FirebaseCrashlytics.getInstance().recordException(e);
            }
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
                    try{
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
                            } else if (arrayList.get(i).getOptions().get(0).getCorrectAnswer().trim().equalsIgnoreCase(arrayList.get(i).getOptions().get(0).getOpt5().trim())) {
                                holder.option5.setBackgroundColor(Color.parseColor("#C6D33C"));
                            }
                        } else {
                            isShowingAnswer = false;
                            holder.option1.setBackgroundColor(Color.parseColor("#FFFFFF"));
                            holder.option2.setBackgroundColor(Color.parseColor("#FFFFFF"));
                            holder.option3.setBackgroundColor(Color.parseColor("#FFFFFF"));
                            holder.option4.setBackgroundColor(Color.parseColor("#FFFFFF"));
                            holder.option5.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        FirebaseCrashlytics.getInstance().recordException(e);
                    }
                }
            });


            holder.ivShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.ivShare.setClickable(false);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            holder.ivShare.setClickable(true);
                        }
                    }, 1000);
                    String shareString ;
                    if (arrayList.get(i).getOptions().get(0).getOpt5() != null) {
                        shareString = arrayList.get(i).getQuestion_title() +
                                "\n\na. " + arrayList.get(i).getOptions().get(0).getOpt1() +
                                "\nb. " + arrayList.get(i).getOptions().get(0).getOpt2() +
                                "\nc. " + arrayList.get(i).getOptions().get(0).getOpt3() +
                                "\nd. " + arrayList.get(i).getOptions().get(0).getOpt4() +
                                "\nd. " + arrayList.get(i).getOptions().get(0).getOpt5() +
                                "\n\nAnswer: " + arrayList.get(i).getOptions().get(0).getCorrectAnswer() +
                                "\n\n\n" + "Share App: " + "https://play.google.com/store/apps/details?id=com.quiz.ansopedia";
                    } else {
                        shareString = arrayList.get(i).getQuestion_title() +
                                "\n\na. " + arrayList.get(i).getOptions().get(0).getOpt1() +
                                "\nb. " + arrayList.get(i).getOptions().get(0).getOpt2() +
                                "\nc. " + arrayList.get(i).getOptions().get(0).getOpt3() +
                                "\nd. " + arrayList.get(i).getOptions().get(0).getOpt4() +
                                "\n\nAnswer: " + arrayList.get(i).getOptions().get(0).getCorrectAnswer() +
                                "\n\n\n" + "Share App: " + "https://play.google.com/store/apps/details?id=com.quiz.ansopedia";
                    }
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Question");
                    intent.putExtra(Intent.EXTRA_TEXT, shareString);
                    context.startActivity(Intent.createChooser(intent, "choose one"));
                }
            });
        } else {
            holder.nextquestion.getBackground().setTint(Color.parseColor(Constants.COLOR));
            holder.previousquestion.getBackground().setTint(Color.parseColor(Constants.COLOR));
            if (ReadQuestionsActivity.lower == 0) {
                holder.previousquestion.setVisibility(View.GONE);
//                holder.previousquestion.setEnabled(false);
            }
            if (ReadQuestionsActivity.upper >= ReadQuestionsActivity.questions.size()) {
                holder.nextquestion.setVisibility(View.GONE);
//                holder.nextquestion.setEnabled(false);
            }
            holder.nextquestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        ReadQuestionsActivity.lower = ReadQuestionsActivity.lower + 10;
                        ReadQuestionsActivity.diff = ReadQuestionsActivity.upper;
                        ReadQuestionsActivity.upper = ReadQuestionsActivity.upper + 10;
                        if (ReadQuestionsActivity.upper >= ReadQuestionsActivity.questions.size()) {
                            ReadQuestionsActivity.upper = ReadQuestionsActivity.questions.size();
                        }
                        ReadQuestionsActivity.setRecyclerView(ReadQuestionsActivity.lower, ReadQuestionsActivity.upper);
                    }catch (Exception e){
                        e.printStackTrace();
                        FirebaseCrashlytics.getInstance().recordException(e);
                    }
                }
            });

            holder.previousquestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        if ((ReadQuestionsActivity.upper - ReadQuestionsActivity.lower) == 10) {
                            ReadQuestionsActivity.lower = ReadQuestionsActivity.lower - 10;
                            ReadQuestionsActivity.upper = ReadQuestionsActivity.upper - 10;
                        } else {
                            ReadQuestionsActivity.upper = ReadQuestionsActivity.diff;
                            ReadQuestionsActivity.lower = ReadQuestionsActivity.upper - 10;
                        }
                        ReadQuestionsActivity.setRecyclerView(ReadQuestionsActivity.lower, ReadQuestionsActivity.upper);
                    }catch (Exception e){
                        e.printStackTrace();
                        FirebaseCrashlytics.getInstance().recordException(e);
                    }
                }
            });
        }

    }

    private void updateOption(int clickedTvOptionTag, List<TextView> tvOptionsList, int questionNo) {
        try{
            int index = 0;
            for (TextView tvOption : tvOptionsList) {
//            if(index < clickedTvOptionTag){
                if (index == clickedTvOptionTag) {
                    String correctAns = arrayList.get(questionNo).getOptions().get(0).getCorrectAnswer().trim();
                    String opt = tvOption.getText().toString().substring(2, tvOption.getText().length()).trim();
//                String opt1 = arrayList.get(questionNo).getOptions().get(0).getOpt1().trim();
//                String opt2 = arrayList.get(questionNo).getOptions().get(0).getOpt2().trim();
//                String opt3 = arrayList.get(questionNo).getOptions().get(0).getOpt3().trim();
//                String opt4 = arrayList.get(questionNo).getOptions().get(0).getOpt4().trim();
//                if(correctAns.equalsIgnoreCase(opt1)){
//                    tvOption.setBackgroundColor(Color.parseColor("#C6D33C"));
//                }
//                else if(correctAns.equalsIgnoreCase(opt2)){
//                    tvOption.setBackgroundColor(Color.parseColor("#C6D33C"));
//                }else if(correctAns.equalsIgnoreCase(opt3)){
//                    tvOption.setBackgroundColor(Color.parseColor("#C6D33C"));
//                }else if(correctAns.equalsIgnoreCase(opt4)){
//                    tvOption.setBackgroundColor(Color.parseColor("#C6D33C"));
//                }
                    if (correctAns.equals(opt)) {
                        tvOption.setBackgroundColor(Color.parseColor("#C6D33C"));
                    } else {
                        tvOption.setBackgroundColor(Color.parseColor("#D94E00"));
                    }
                } else {
                    tvOption.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                index++;
            }
        }catch (Exception e){
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView srNoQuestion, quiz_questions, option1, option2, option3, option4, option5, tvDescription;
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
            option5 = itemView.findViewById(R.id.option5);
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
