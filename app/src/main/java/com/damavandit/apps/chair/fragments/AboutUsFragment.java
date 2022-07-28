package com.damavandit.apps.chair.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.damavandit.apps.chair.R;

public class AboutUsFragment extends Fragment {

    //    private SectionsPagerAdapter mSectionsPagerAdapter;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private Animation animationLeft, animationRight;
    private Button mButtonTest;

    private ViewFlipper mViewFlipper, mViewFlipperTwo;
    private ViewPager mViewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private int[] layouts;

    private Context mContext;

    public AboutUsFragment() {
        // Required empty public constructor
    }

    public static AboutUsFragment newInstance() {
        AboutUsFragment fragment = new AboutUsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_about_us, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        mSectionsPagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager = view.findViewById(R.id.tab_about_container);
//        mViewPager.setAdapter(mSectionsPagerAdapter);

        layouts = new int[]{
                R.layout.about_slide1,
                R.layout.about_slide2,
                R.layout.about_slide3};

        myViewPagerAdapter = new MyViewPagerAdapter();
        mViewPager.setAdapter(myViewPagerAdapter);
        mViewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        animationLeft = AnimationUtils.loadAnimation(getContext(), R.anim.left_to_right);
        mViewFlipper = view.findViewById(R.id.slideShow_about_first);
        mViewFlipper.setAnimation(animationLeft);
        mViewFlipper.setAutoStart(true);
        mViewFlipper.setFlipInterval(3000);
        mViewFlipper.startFlipping();

        mViewFlipperTwo = view.findViewById(R.id.slideShow_about_two);
        mViewFlipperTwo.setAnimation(animationLeft);
        mViewFlipperTwo.setAutoStart(true);
        mViewFlipperTwo.setFlipInterval(3000);
        mViewFlipperTwo.startFlipping();

        Typeface face = Typeface.createFromAsset(getContext().getAssets(), "Font/Sans.ttf");
        TextView more_info = view.findViewById(R.id.more_info);
        more_info.setTypeface(face);

        TextView text_focus = view.findViewById(R.id.text_focus);
        text_focus.setTypeface(face);

        TextView text_focus_detail = view.findViewById(R.id.text_focus_detail);
        text_focus_detail.setTypeface(face);

        TextView text_star = view.findViewById(R.id.text_star);
        text_star.setTypeface(face);

        TextView text_star_detail = view.findViewById(R.id.text_star_detail);
        text_star_detail.setTypeface(face);

        TextView text_update = view.findViewById(R.id.text_update);
        text_update.setTypeface(face);

        TextView text_update_detail = view.findViewById(R.id.text_update_detail);
        text_update_detail.setTypeface(face);

        TextView text_trust = view.findViewById(R.id.text_trust);
        text_trust.setTypeface(face);

        TextView text_comment = view.findViewById(R.id.text_comment);
        text_comment.setTypeface(face);
    }

    private int getItem(int i) {
        return mViewPager.getCurrentItem() + i;
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
            } else {
                // still pages are left
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            Typeface face = Typeface.createFromAsset(getContext().getAssets(), "Font/Sans.ttf");
            TextView chairTitle = view.findViewById(R.id.text_chair_first);
            if (chairTitle != null)
                chairTitle.setTypeface(face);
            TextView chairAboutText = view.findViewById(R.id.about_chair);
            if (chairAboutText != null)
                chairAboutText.setTypeface(face);
            Button buttonMoreInfo = view.findViewById(R.id.button_more_info_first);
            if (buttonMoreInfo != null)
                buttonMoreInfo.setTypeface(face);
            Button buttonCallAbout = view.findViewById(R.id.button_call_about);
            if (buttonCallAbout != null)
                buttonCallAbout.setTypeface(face);
            Button buttonVisitAbout = view.findViewById(R.id.button_visit_about);
            if (buttonVisitAbout != null)
                buttonVisitAbout.setTypeface(face);
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}

//    public static class PlaceholderFragment extends Fragment {
//        private static final String ARG_SECTION_NUMBER = "section_number";
//
//        public PlaceholderFragment() {
//        }
//
//        public static PlaceholderFragment newInstance(int sectionNumber) {
//            PlaceholderFragment fragment = new PlaceholderFragment();
//            Bundle args = new Bundle();
//            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//            fragment.setArguments(args);
//            return fragment;
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            View rootView = inflater.inflate(R.layout.fragment_inside_about, container, false);
//
//            Typeface face = Typeface.createFromAsset(getContext().getAssets(), "Font/Sans.ttf");
//            TextView chairTitle = rootView.findViewById(R.id.text_chair_first);
//            chairTitle.setTypeface(face);
//            TextView chairAboutText = rootView.findViewById(R.id.about_chair);
//            chairAboutText.setTypeface(face);
//            Button buttonMoreInfo = rootView.findViewById(R.id.button_more_info_first);
//            buttonMoreInfo.setTypeface(face);
//            ImageView imageGoogle = rootView.findViewById(R.id.image_google_about);
//            ImageView imageTwitter = rootView.findViewById(R.id.image_twitter_about);
//            ImageView imageFacebook = rootView.findViewById(R.id.image_facebook_about);
//            Button buttonCallAbout = rootView.findViewById(R.id.button_call_about);
//            buttonCallAbout.setTypeface(face);
//            Button buttonVisitAbout = rootView.findViewById(R.id.button_visit_about);
//            buttonCallAbout.setTypeface(face);
//
//            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
//                case 0:
//                    chairTitle.setVisibility(View.VISIBLE);
//                    chairTitle.setText(getResources().getString(R.string.happy_connect));
//                    chairAboutText.setVisibility(View.VISIBLE);
//                    chairAboutText.setText(getResources().getString(R.string.lorem));
//                    buttonMoreInfo.setVisibility(View.INVISIBLE);
//                    imageGoogle.setVisibility(View.INVISIBLE);
//                    imageTwitter.setVisibility(View.INVISIBLE);
//                    imageFacebook.setVisibility(View.INVISIBLE);
//                    buttonCallAbout.setVisibility(View.VISIBLE);
//                    buttonVisitAbout.setVisibility(View.VISIBLE);
//                    break;
//                case 1:
//                    chairTitle.setVisibility(View.VISIBLE);
//                    chairTitle.setText(getResources().getString(R.string.chair_ironwork));
//                    chairAboutText.setVisibility(View.VISIBLE);
//                    chairAboutText.setText(getResources().getString(R.string.about_chair_first));
//                    buttonMoreInfo.setVisibility(View.VISIBLE);
//                    imageGoogle.setVisibility(View.INVISIBLE);
//                    imageTwitter.setVisibility(View.INVISIBLE);
//                    imageFacebook.setVisibility(View.INVISIBLE);
//                    buttonCallAbout.setVisibility(View.INVISIBLE);
//                    buttonVisitAbout.setVisibility(View.INVISIBLE);
//                    break;
//                case 2:
//                    chairTitle.setVisibility(View.VISIBLE);
//                    chairTitle.setText(getResources().getString(R.string.happy_connect));
//                    chairAboutText.setVisibility(View.VISIBLE);
//                    chairAboutText.setText(getResources().getString(R.string.lorem));
//                    buttonMoreInfo.setVisibility(View.INVISIBLE);
//                    imageGoogle.setVisibility(View.INVISIBLE);
//                    imageTwitter.setVisibility(View.INVISIBLE);
//                    imageFacebook.setVisibility(View.INVISIBLE);
//                    buttonCallAbout.setVisibility(View.VISIBLE);
//                    buttonVisitAbout.setVisibility(View.VISIBLE);
//                    break;
//                case 3:
//                    chairTitle.setVisibility(View.VISIBLE);
//                    chairTitle.setText(getResources().getString(R.string.with_us_in_social_networks));
//                    chairAboutText.setVisibility(View.VISIBLE);
//                    chairAboutText.setText(getResources().getString(R.string.lorem));
//                    buttonMoreInfo.setVisibility(View.INVISIBLE);
//                    imageGoogle.setVisibility(View.VISIBLE);
//                    imageTwitter.setVisibility(View.VISIBLE);
//                    imageFacebook.setVisibility(View.VISIBLE);
//                    buttonCallAbout.setVisibility(View.INVISIBLE);
//                    buttonVisitAbout.setVisibility(View.INVISIBLE);
//                    break;
//            }
//            return rootView;
//        }
//    }
//
//    public class SectionsPagerAdapter extends FragmentPagerAdapter {
//
//        public SectionsPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            return PlaceholderFragment.newInstance(position +1 );
//        }
//
//        @Override
//        public int getCount() {
//            return 3;
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            switch (position) {
//                case 0:
//                    return "SECTION 1";
//                case 1:
//                    return "SECTION 2";
//                case 2:
//                    return "SECTION 3";
//                case 3:
//                    return "SECTION 4";
//            }
//            return null;
//        }
//    }
//}
