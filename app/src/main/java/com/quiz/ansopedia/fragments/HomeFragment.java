package com.quiz.ansopedia.fragments;

import static com.quiz.ansopedia.Utility.Constants.contents;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.tabs.TabLayout;
import com.quiz.ansopedia.R;
import com.quiz.ansopedia.Utility.Utility;
import com.quiz.ansopedia.adapter.CourseAdapter;
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
    TabLayout tabLayout;
    RecyclerView rvContent;
    CourseAdapter courseAdapter;
    ImageView ivProgress;
    View userIcons;
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
        userIcons = view.findViewById(R.id.userIcons);
        rvContent = view.findViewById(R.id.rvContent);
        ivProgress = view.findViewById(R.id.ivProgress);
//            ############################### progress gif start ########################
        Glide.with(getContext()).load(R.drawable.ansopedia_loader_new).into(ivProgress);
        setImage_slider();
        if (contents == null) {
            rvContent.setVisibility(View.GONE);
            ivProgress.setVisibility(View.VISIBLE);
            getContent();
        } else {
            setTabLayout(contents.getBranch().get(0).getBranch_name());
            setRecyclerView(getSubjects(contents.getBranch().get(0).getBranch_name()));
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

    private void setTabLayout(String branch_name) {
        ArrayList<String> tabList = new ArrayList<>();
        for (Branch branch : contents.getBranch()) {
            tabList.add(branch.getBranch_name());
        }

        for (String tab : tabList) {
            tabLayout.addTab(tabLayout.newTab().setText(tab));
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setRecyclerView(getSubjects(tab.getText().toString()));
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
                ContentApiImplementer.getContent(new Callback<List<Contents>>() {
                    @Override
                    public void onResponse(Call<List<Contents>> call, Response<List<Contents>> response) {
//                        Utility.dismissProgressGif();
                        rvContent.setVisibility(View.VISIBLE);
                        ivProgress.setVisibility(View.GONE);
                        if (response.code() == 200) {
                            if (response.body().size() != 0) {
                                contents = response.body().get(0);
                                setTabLayout(contents.getBranch().get(0).getBranch_name());
                                setRecyclerView(getSubjects(contents.getBranch().get(0).getBranch_name()));
                            }
                        }else if(response.code() == 500) {
                            Utility.showAlertDialog(getContext(), "Failed", "Server Error, Please Try Again");
                        }else{
                            Utility.showAlertDialog(getContext(), "Error", "Something went wrong, Please Try Again");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Contents>> call, Throwable t) {
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
            }
        } else {
//            Utility.dismissProgressGif();
            rvContent.setVisibility(View.VISIBLE);
            ivProgress.setVisibility(View.GONE);
            Utility.showAlertDialog(getContext(), "Error", "Please Connect to Internet");
        }
    }

    private ArrayList<Subjects> getSubjects(String branch_name) {
        ArrayList<Subjects> tempList = new ArrayList<>();
        for (Branch branch : contents.getBranch()) {
            if (branch.getBranch_name().equalsIgnoreCase(branch_name)) {
                tempList = (ArrayList<Subjects>) branch.getSubjects();
            }
        }
        return tempList;
    }

}