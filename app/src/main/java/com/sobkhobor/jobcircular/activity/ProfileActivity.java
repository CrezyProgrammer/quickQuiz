package com.sobkhobor.jobcircular.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.sobkhobor.jobcircular.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.name)
    TextInputLayout name;
    @BindView(R.id.phone)
    TextInputLayout phone;
FirebaseUser user;
DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        user= FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference("user").child(user.getUid());
        setData();
    }

    private void setData() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name.getEditText().setText(dataSnapshot.child("name").exists()?dataSnapshot.child("name").getValue().toString():"");
                phone.getEditText().setText(dataSnapshot.child("phone").exists()?dataSnapshot.child("phone").getValue().toString():"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @OnClick(R.id.button2)
    public void onViewClicked() {
        databaseReference.child("name").setValue(name.getEditText().getText().toString());
        databaseReference.child("phone").setValue(phone.getEditText().getText().toString());
        Toast.makeText(this, "Successfully Saved", Toast.LENGTH_SHORT).show();
        
    }
}
