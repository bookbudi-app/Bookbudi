package com.app.bookbudiapp;


import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Account extends Fragment {

    private CircleImageView circleImage;
    private TextView profileName,appVersion;
    private Button logOut,update;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth fAuth;
    private FirebaseUser user;
    private LinearLayout faqCard,supportCard,shareCard,rateCard;
    private CardView updateCard;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbRef;


    public Account() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();

        dbRef = firebaseDatabase.getInstance().getReference();

        checkVersion();


        circleImage = view.findViewById(R.id.circleImage);
        profileName = view.findViewById(R.id.profileName);
        appVersion = view.findViewById(R.id.appVersion);
        logOut = view.findViewById(R.id.logOut);
        update = view.findViewById(R.id.update);
        faqCard = view.findViewById(R.id.faqCard);
        supportCard = view.findViewById(R.id.supportCard);
        shareCard = view.findViewById(R.id.shareCard);
        rateCard = view.findViewById(R.id.rateCard);
        updateCard = view.findViewById(R.id.updateCard);

        appVersion.setText("App version: " +BuildConfig.VERSION_NAME);

        update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.app.bookbudiapp");
                Intent playStore = new Intent(Intent.ACTION_VIEW,uri);
                try{
                    startActivity(playStore);
                }catch(Exception e){
                    TastyToast.makeText(getActivity(),"App is not available",TastyToast.LENGTH_SHORT,TastyToast.ERROR).show();
                }
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getContext(),gso);


        if(user != null){

            String name = user.getDisplayName();
            Uri photoUrl = user.getPhotoUrl();

            profileName.setText(name);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.user);

            Glide.with(getContext()).load(photoUrl).apply(requestOptions).into(circleImage);

        }

        faqCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent faq = new Intent(getActivity(),Faq.class);
                startActivity(faq);
                getActivity().finish();
            }
        });

        supportCard.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

               try {

                   Intent emailIntent = new Intent(Intent.ACTION_SEND);
                   emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@bookbudi.com"});
                   emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Help");
                   emailIntent.setType("message/rfc822");
                   startActivity(Intent.createChooser(emailIntent,"Send email"));

               }catch(ActivityNotFoundException e){

                   TastyToast.makeText(getActivity(),e.getMessage(),TastyToast.LENGTH_SHORT,TastyToast.ERROR).show();
               }
            }
        });

        shareCard.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

             /*   Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Bookbudi app");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Hello there, I found this awesome book sharing app take a look."+" https://play.google.com/store/apps/details?id=com.app.bookbudiapp");

                startActivity(Intent.createChooser(shareIntent,"Share via"));  */

                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Bookbudi app");
                    String shareMessage= "\nHello there, I found this awesome book sharing app take a look\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    TastyToast.makeText(getActivity(),e.getMessage(),TastyToast.LENGTH_SHORT,TastyToast.ERROR).show();
                }
            }
        });

        rateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.app.bookbudiapp");
                Intent appLink = new Intent(Intent.ACTION_VIEW,uri);
                try{
                    startActivity(appLink);
                }catch(Exception e){
                    TastyToast.makeText(getActivity(),"App is not available",TastyToast.LENGTH_SHORT,TastyToast.ERROR).show();
                }
            }
        });


        logOut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final ProgressDialog prg = new ProgressDialog(getActivity());
                prg.setMessage("Logging out...");
                prg.setCancelable(false);
                prg.show();

                if(fAuth != null){

                    mGoogleSignInClient.signOut().addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {

                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            prg.dismiss();
                            fAuth.signOut();
                            Intent exit  = new Intent(getActivity(),Login.class);
                            startActivity(exit);
                            getActivity().finish();

                        }
                    });
                }

            }
        });

        return view;
    }

   private void checkVersion(){

        dbRef.child("AppVersion").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String version = dataSnapshot.getValue(String.class);

                if(version != null){

                    if(!version.equals(BuildConfig.VERSION_NAME)){

                        updateCard.setVisibility(View.VISIBLE);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                TastyToast.makeText(getActivity(),databaseError.getMessage(),TastyToast.LENGTH_LONG,TastyToast.ERROR).show();
            }
        });

   }

}
