package com.app.bookbudiapp;


import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sdsmdg.tastytoast.TastyToast;

import de.hdodenhof.circleimageview.CircleImageView;


public class Account extends Fragment {

    CircleImageView circleImage;
    TextView profileName;
    Button logOut;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth fAuth;
    FirebaseUser user;
    LinearLayout settingsCard,supportCard,shareCard;

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

        circleImage = view.findViewById(R.id.circleImage);
        profileName = view.findViewById(R.id.profileName);
        logOut = view.findViewById(R.id.logOut);
        settingsCard = view.findViewById(R.id.settingsCard);
        supportCard = view.findViewById(R.id.supportCard);
        shareCard = view.findViewById(R.id.shareCard);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getContext(),gso);


        if(user != null){

            String name = user.getDisplayName();
            Uri photoUrl = user.getPhotoUrl();

            profileName.setText(name);

            Glide.with(getContext()).load(photoUrl).into(circleImage);

        }

        settingsCard.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

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

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Bookbudi app");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Hello there, I found this awesome book sharing app take a look.");
                startActivity(Intent.createChooser(shareIntent,"Share via"));
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

}
