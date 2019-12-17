package com.dreamtechdesignandstudio.quickquiz1.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dreamtechdesignandstudio.quickquiz1.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class LeaderBoardActivity extends AppCompatActivity {
    int total;
    @BindView(R.id.sndImageLeader)
    CircleImageView sndImageLeader;
    @BindView(R.id.sndNameLeader)
    TextView sndNameLeader;
    @BindView(R.id.sndScoreLeader)
    TextView sndScoreLeader;
    @BindView(R.id.fstImageLeader)
    CircleImageView fstImageLeader;
    @BindView(R.id.fstNameLeader)
    TextView fstNameLeader;
    @BindView(R.id.fstScoreLeader)
    TextView fstScoreLeader;
    @BindView(R.id.trdImageLeader)
    CircleImageView trdImageLeader;
    @BindView(R.id.trdNameLeader)
    TextView trdNameLeader;
    @BindView(R.id.trdScoreLeader)
    TextView trdScoreLeader;
    FirebaseUser user;
    @BindView(R.id.leader_recycler)
    RecyclerView leaderRecycler;
    @BindView(R.id.textView3)
    TextView textView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        ButterKnife.bind(this);
        user = FirebaseAuth.getInstance().getCurrentUser();
        getTop();
     countTotal();
       getNotice();

    }

    private void getNotice() {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("admin/leardboardnotice");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                textView3.setText((dataSnapshot.exists()?dataSnapshot.getValue().toString():"Add text under admin/leaderboardnotice"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void countTotal() {
        Query databaseReference = FirebaseDatabase.getInstance().getReference("user").orderByChild("score").limitToLast(20);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                total = Integer.parseInt(dataSnapshot.getChildrenCount() + "");
                getOthers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getOthers() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        leaderRecycler.setLayoutManager(manager);
        leaderRecycler.setNestedScrollingEnabled(false);

        Query databaseReference = FirebaseDatabase.getInstance().getReference("user").orderByChild("score").limitToLast(20);
        FirebaseRecyclerOptions<Model> options = new FirebaseRecyclerOptions.Builder<Model>().setQuery(databaseReference, snapshot ->
                //new Model()).build();
                new Model(snapshot.child("name").getValue().toString(), snapshot.child("profile").getValue().toString(), snapshot.child("id").getValue().toString(), (snapshot.child("score").exists()?snapshot.child("score").getValue(Integer.class):0))).build();
        FirebaseRecyclerAdapter<Model, ViewHolder> adapter = new FirebaseRecyclerAdapter<Model, ViewHolder>(options) {
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout, parent, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull Model model) {
                viewHolder.setup(model, (total - i));

            }


        };
        leaderRecycler.setAdapter(adapter);
        adapter.startListening();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView serial, name, right;
        ImageView imageView;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            serial = itemView.findViewById(R.id.position);
            name = itemView.findViewById(R.id.name);
            right = itemView.findViewById(R.id.right);
            imageView = itemView.findViewById(R.id.image);
            cardView = itemView.findViewById(R.id.card);

        }

        public void setup(Model model, int position) {
            Log.i("123321", model.getName());
            if (position == 1 || position == 2 || position == 3) {
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
                cardView.setLayoutParams(lp);
            } else {
                serial.setText("" + (position));
                name.setText(model.getName());
                right.setText("" + model.getRight());
                Picasso.get().load(model.getImage()).placeholder(R.drawable.rpf).into(imageView);

            }
        }
    }

    private void getTop() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading");
        progressDialog.show();
        Query databaseReference = FirebaseDatabase.getInstance().getReference("user").orderByChild("score").limitToLast(3);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            int i = 0;

            String name, firstName, profile, score;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                total = Integer.parseInt("" + dataSnapshot.getChildrenCount());
                Log.i("123321 ", " total " + total + " i= " + i);
                if (total == 1) i += 2;
                else if (total == 2) i += 1;
                else if (total == 3) i += 0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    profile = ds.child("profile").exists() ? ds.child("profile").getValue().toString() : "";
                    name = ds.child("name").exists() ? ds.child("name").getValue().toString() : "";
                    profile = ds.child("profile").exists() ? ds.child("profile").getValue().toString() : "";
                    score = (ds.child("score").exists()?ds.child("score").getValue(Integer.class):0)+"";
                    if (name.split("\\w+").length > 1) {
                        firstName = name.substring(0, name.lastIndexOf(' '));
                    } else {
                        firstName = name;
                    }
                    progressDialog.dismiss();
                    switch (i) {
                        case 0:


                            Picasso.get().load(profile).placeholder(R.drawable.rpf).into(trdImageLeader);


                            trdNameLeader.setText(firstName);
                            trdScoreLeader.setText(score);
                            break;
                        case 1:

                            Picasso.get().load(profile).placeholder(R.drawable.rpf).into(sndImageLeader);
                            sndNameLeader.setText(firstName);
                            sndScoreLeader.setText(score);
                            break;
                        case 2:

                            Picasso.get().load(profile).placeholder(R.drawable.rpf).into(fstImageLeader);
                            fstNameLeader.setText(firstName);
                            fstScoreLeader.setText(score);
                            break;
                    }
                    i++;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
