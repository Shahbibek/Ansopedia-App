package com.quiz.ansopedia.fragments;

import static com.quiz.ansopedia.Utility.Constants.contents;
import static com.quiz.ansopedia.Utility.Constants.contentsList;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.quiz.ansopedia.R;
import com.quiz.ansopedia.Utility.Constants;
import com.quiz.ansopedia.Utility.Utility;
import com.quiz.ansopedia.adapter.CourseAdapter;
import com.quiz.ansopedia.api.ApiResponse;
import com.quiz.ansopedia.models.Branch;
import com.quiz.ansopedia.models.Contents;
import com.quiz.ansopedia.models.Subjects;
import com.quiz.ansopedia.retrofit.ContentApiImplementer;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    ImageSlider image_slider;
    TabLayout tabLayout, tabLayoutStream;
    RecyclerView rvContent;
    CourseAdapter courseAdapter;
    ImageView ivProgress;
//    View userIcons;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        image_slider = view.findViewById(R.id.image_slider);
        tabLayout = view.findViewById(R.id.tabLayout);
        tabLayoutStream = view.findViewById(R.id.tabLayoutStream);
        //userIcons = view.findViewById(R.id.userIcons);
        rvContent = view.findViewById(R.id.rvContent);
        ivProgress = view.findViewById(R.id.ivProgress);
//            ############################### progress gif start ########################
        Glide.with(getContext()).load(R.drawable.ansopedia_loader_new).into(ivProgress);
        setImage_slider();
        if (contentsList == null) {
            rvContent.setVisibility(View.GONE);
            ivProgress.setVisibility(View.VISIBLE);
            getContent();
        } else {
//            setTabLayout((ArrayList<Branch>) contents.getBranch());
            setTabLayoutStream(Utility.toCapitalizeFirstLetter(contentsList.get(0).getTitle()));
//            setRecyclerView(getSubjects(contents.getBranch().get(0).getBranch_name()));
        }
        return view;
    }


    private void setImage_slider() {
        ArrayList<SlideModel> imageList = new ArrayList<>(); // Create image list

        imageList.add(new SlideModel("https://res.cloudinary.com/ddhtmkllj/image/upload/v1676712377/samples/Slider_Mobile/Ansopedia_Variant_gpgwgr.png ", ScaleTypes.FIT));
        imageList.add(new SlideModel("https://res.cloudinary.com/ddhtmkllj/image/upload/v1676712377/samples/Slider_Mobile/Programming_Variant_slg06c.png ", ScaleTypes.FIT));
        imageList.add(new SlideModel("https://res.cloudinary.com/ddhtmkllj/image/upload/v1676712377/samples/Slider_Mobile/Medical_Variant_ppfuud.png ", ScaleTypes.FIT));

        image_slider.setImageList(imageList);
        image_slider.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemSelected(int i) {

            }
        });
    }

    private void setTabLayoutStream(String branch_name) {
        ArrayList<String> tabList = new ArrayList<>();
        if(contentsList.size() != 0){
            for (Contents contents1 : contentsList) {
                tabList.add(contents1.getTitle());
            }
        }

        for (String tab : tabList) {
            TabLayout.Tab newTab = tabLayoutStream.newTab();
            newTab.setCustomView(R.layout.inactive_stream);
            TextView stream = newTab.getCustomView().findViewById(R.id.tabInactive);
            stream.setText(Utility.toCapitalizeFirstLetter(tab));
            tabLayoutStream.addTab(newTab);
        }
        TextView textView = tabLayoutStream.getTabAt(0).getCustomView().findViewById(R.id.tabInactive);
        textView.setBackground(getResources().getDrawable(R.drawable.color_category_bg));
        textView.setTextColor(getResources().getColor(R.color.white));
        for (Contents content : contentsList) {
            if (content.getTitle().equalsIgnoreCase(textView.getText().toString())){
                if(content.getBranch().size() != 0){
                    setTabLayout((ArrayList<Branch>) content.getBranch());
                }
            }
        }
        tabLayoutStream.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView textView = tab.getCustomView().findViewById(R.id.tabInactive);
                textView.setBackground(getResources().getDrawable(R.drawable.color_category_bg));
                textView.setTextColor(getResources().getColor(R.color.white));
                for (Contents content : contentsList) {
                    if (content.getTitle().equalsIgnoreCase(textView.getText().toString())){
                        if(content.getBranch().size() != 0){
                            setTabLayout((ArrayList<Branch>) content.getBranch());
                        }
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView textView = tab.getCustomView().findViewById(R.id.tabInactive);
                textView.setBackground(getResources().getDrawable(R.drawable.gray_category_bg));
                textView.setTextColor(getResources().getColor(R.color.about_color));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    private void setTabLayout(ArrayList<Branch> branchList) {
        ArrayList<String> tabList = new ArrayList<>();
        for (Branch branch : branchList) {
            tabList.add(branch.getBranch_name());
        }
        tabLayout.removeAllTabs();
        for (String tab : tabList) {
            tabLayout.addTab(tabLayout.newTab().setText(tab));
        }
        String s = tabLayout.getTabAt(0).getText().toString();
        for (Branch branch : branchList) {
            if(branch.getSubjects() != null){
                if (branch.getBranch_name().equalsIgnoreCase(s)) {
                    setRecyclerView((ArrayList<Subjects>) branch.getSubjects());
                }
            }

        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String s = tab.getText().toString();
                for (Branch branch : branchList) {
                    if(branch.getSubjects().size() != 0){
                        if (branch.getBranch_name().equalsIgnoreCase(s)) {
                            setRecyclerView((ArrayList<Subjects>) branch.getSubjects());
                        }
                    }

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setRecyclerView(ArrayList<Subjects> subjects) {
        rvContent.setHasFixedSize(true);
        rvContent.setItemViewCacheSize(10);
        courseAdapter = new CourseAdapter(getContext(), subjects);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        rvContent.setLayoutManager(layoutManager);
        rvContent.setAdapter(courseAdapter);
    }

    private void getContent() {
//        Utility.showProgressGif(getContext());
        if (Utility.isNetConnected(getContext())) {
            try {
                ContentApiImplementer.getContent(new Callback<ApiResponse<List<Contents>>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<List<Contents>>> call, Response<ApiResponse<List<Contents>>> response) {
//                        Utility.dismissProgressGif();
                        rvContent.setVisibility(View.VISIBLE);
                        ivProgress.setVisibility(View.GONE);
                        if (response.isSuccessful()) {
                            if (response.body().getData().size() != 0) {
                                contents = (Contents) response.body().getData().get(0);
                                contentsList = new ArrayList<>();
                                contentsList = (ArrayList<Contents>) response.body().getData();
                                setTabLayoutStream(contentsList.get(0).getTitle());
//                                setTabLayout((ArrayList<Branch>) contents.getBranch());
//                                setRecyclerView(getSubjects(contents.getBranch().get(0).getBranch_name()));
                            }
                        }else{
                            Utility.showAlertDialog(getContext(), "Error", "Something went wrong, Please Try Again");
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<List<Contents>>> call, Throwable t) {
//                        Utility.dismissProgressGif();
                        rvContent.setVisibility(View.VISIBLE);
                        ivProgress.setVisibility(View.GONE);
                        t.printStackTrace();
                        Utility.showAlertDialog(getContext(), "Error", "Something went wrong, Please Try Again");
                    }
                });
            } catch (Exception e) {
//                Utility.dismissProgressGif();
                rvContent.setVisibility(View.VISIBLE);
                ivProgress.setVisibility(View.GONE);
                e.printStackTrace();
                FirebaseCrashlytics.getInstance().recordException(e);
            }
        } else {
//            Utility.dismissProgressGif();
            rvContent.setVisibility(View.VISIBLE);
            ivProgress.setVisibility(View.GONE);
            Utility.showAlertDialog(getContext(), "Error", "Please Connect to Internet");
        }
    }

    private ArrayList<Subjects> getSubjects(ArrayList<Branch> branchList) {
//        ArrayList<Subjects> tempList = new ArrayList<>();
//        for (Branch branch : branchList) {
//            if (branch.getBranch_name().equalsIgnoreCase(branch_name)) {
//                tempList = (ArrayList<Subjects>) branch.getSubjects();
//            }
//        }
        return (ArrayList<Subjects>) branchList.get(0).getSubjects();
    }

}