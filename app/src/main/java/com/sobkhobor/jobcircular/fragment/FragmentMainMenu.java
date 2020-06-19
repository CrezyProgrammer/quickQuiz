package com.sobkhobor.jobcircular.fragment;


import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.sobkhobor.jobcircular.R;
import com.sobkhobor.jobcircular.activity.BookmarkList;
import com.sobkhobor.jobcircular.activity.InstructionActivity;
import com.sobkhobor.jobcircular.activity.LeaderBoardActivity;
import com.sobkhobor.jobcircular.activity.MainActivity_quiz;
import com.sobkhobor.jobcircular.activity.PrivacyPolicy;
import com.sobkhobor.jobcircular.activity.ProfileActivity;
import com.sobkhobor.jobcircular.activity.SettingActivity;
import com.sobkhobor.jobcircular.activity.SignActivity;
import com.sobkhobor.jobcircular.helper.SettingsPreferences;
import com.sobkhobor.jobcircular.helper.StaticUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class FragmentMainMenu extends Fragment implements View.OnClickListener {

    private View mSignIn;
    private View mSignOut;

    private Button Bookmark;
    CoordinatorLayout layout;
    private Listener mListener = null;
    private boolean mShowSignInButton = true;


    public interface Listener {
        // called when the user presses the `Easy` or `Okay` button; will pass in which via `hardMode`
        void onStartGameRequested();

        // called when the user presses the `Show Achievements` button
        void onShowAchievementsRequested();

        // called when the user presses the `Show Leaderboards` button
        void onShowLeaderboardsRequested();

        // called when the user presses the `Sign In` button
        void onSignInButtonClicked();

        // called when the user presses the `Sign Out` button
        void onSignOutButtonClicked();
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mainmenu, container, false);
        mSignIn = view.findViewById(R.id.sign_in_button);
        mSignOut = view.findViewById(R.id.sign_out_button);
        layout = (CoordinatorLayout) view.findViewById(R.id.layout);
        final int[] clickableIds = new int[]{
                R.id.sign_in_button,
                R.id.sign_out_button,
                R.id.instruction,
                R.id.setting1,
                R.id.start,
                R.id.achivments1,
                R.id.leaderbord1,
                R.id.imgPrivacy
        };

        for (int clickableId : clickableIds) {
            view.findViewById(clickableId).setOnClickListener(this);
        }
        TextView  notice= view.findViewById(R.id.tvnotice);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Admin Notice");
databaseReference.setValue("Welcome");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              notice .setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        updateUI();

        Bookmark =  view.findViewById(R.id.imgBookmark);
        Bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MainActivity_quiz.btnClick(view, getActivity());
                Intent intent = new Intent(getActivity(), BookmarkList.class);
                startActivity(intent);
            }
        });
        return view;
    }


    public void setListener(Listener listener) {
        mListener = listener;
    }


    private void updateUI() {

        mSignIn.setVisibility(mShowSignInButton ? View.VISIBLE : View.GONE);
        mSignOut.setVisibility(mShowSignInButton ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start:
                MainActivity_quiz.btnClick(view, getActivity());
                mListener.onStartGameRequested();
                break;
            case R.id.sign_in_button:
                Builder builder = new Builder(getActivity());
                builder.setTitle("Logout");
                builder.setMessage("Are you Sure");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getActivity(), SignActivity.class));
                    }
                });
                builder.setNegativeButton("cancel",null );
                builder.show();

                break;
            case R.id.sign_out_button:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), SignActivity.class));
                break;
            case R.id.instruction:

                /**/
                MainActivity_quiz.btnClick(view, getActivity());
                SettingsPreferences.setLan(getContext(), true);
                if (SettingsPreferences.getSoundEnableDisable(getContext())) {
                    StaticUtils.backSoundonclick(getContext());
                }
                if (SettingsPreferences.getVibration(getContext())) {
                    StaticUtils.vibrate(getContext(), StaticUtils.VIBRATION_DURATION);
                }
                Intent playQuiz = new Intent(getActivity(), InstructionActivity.class);
                startActivity(playQuiz);


                break;
            case R.id.setting1:
                MainActivity_quiz.btnClick(view, getActivity());
                if (SettingsPreferences.getSoundEnableDisable(getContext())) {
                    StaticUtils.backSoundonclick(getContext());
                }
                if (SettingsPreferences.getVibration(getContext())) {
                    StaticUtils.vibrate(getContext(), StaticUtils.VIBRATION_DURATION);
                }
                Intent playQuiz1 = new Intent(getActivity(), SettingActivity.class);
                startActivity(playQuiz1);
                break;
            case R.id.leaderbord1:
                startActivity(new Intent(getActivity(), LeaderBoardActivity.class));

                break;
            case R.id.achivments1:
                startActivity(new Intent(getActivity(), ProfileActivity.class));

                break;
            case R.id.imgPrivacy:
                MainActivity_quiz.btnClick(view, getActivity());
                Intent privacyIntent = new Intent(getActivity(), PrivacyPolicy.class);
                startActivity(privacyIntent);
                break;

        }
    }

    public void setShowSignInButton(boolean showSignInButton) {
        mShowSignInButton = showSignInButton;
        updateUI();
    }
}