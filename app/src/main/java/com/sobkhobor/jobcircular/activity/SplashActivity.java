package com.sobkhobor.jobcircular.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.sobkhobor.jobcircular.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashActivity extends AppCompatActivity {
int i =1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        try
        {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures)
            {
                MessageDigest md = MessageDigest.getInstance("SHA");
                Log.e("123321", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        }
        catch (PackageManager.NameNotFoundException e)
        {

        }
        catch (NoSuchAlgorithmException e)
        {

        }
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            //Do something after 100ms
            if (FirebaseAuth.getInstance().getCurrentUser()==null) {
                startActivity(new Intent(this, SignActivity.class));
                
            }
            else
                startActivity(new Intent(this, MainActivity_quiz.class));
         //  autoUpdate();
            
        }, 3000);

        }

    private void autoUpdate() {
if(i<=90)
    setList();
        //copyRecord(FirebaseDatabase.getInstance().getReference("Vocabulary Practice 1"),FirebaseDatabase.getInstance().getReference("Vocabulary Practice "+(i)));
    }

    private void setList() {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("list").child(""+i);
        databaseReference.child("name").setValue("Vocabulary Practice "+i);
        i++;
        autoUpdate();

    }

    public void copyRecord(DatabaseReference fromPath, final DatabaseReference toPath) {
        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toPath.setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            System.out.println("Copy failed");
                            Log.i("123321", "copy fail");
                        } else {
                            System.out.println("Success");
                            Log.i("123321", " success");
                            i+=1;
                            autoUpdate();
                        }
                    }
                }) ;
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {}
        });
    }

}
