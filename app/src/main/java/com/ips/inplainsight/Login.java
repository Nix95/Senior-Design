package com.ips.inplainsight;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivityOccuring";
    private static final int RC_SIGN_IN = 123;
    private GoogleSignInClient mGoogleSignInClient;
    View v;
    SignInButton signInButton;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        Log.d(TAG, "Made it to onCreate");
        //v = findViewById(R.id.sign_in_button);

        setContentView(R.layout.activity_login);

        //BUTTON LISTENERS
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.disconnect_button).setOnClickListener(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "I made it to onStart");
    // Check for existing Google Sign In account, if the user is already signed in
    // the GoogleSignInAccount will be non-null.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

//        v = findViewById(R.id.sign_in_button);
        signInButton = (SignInButton)findViewById(R.id.sign_in_button);

        Log.d(TAG, "Cameback from updateUI & initalized v");
//        signInButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v)
//            {
//                signIn();
//            }
//        });

    }
    private void signIn() {
        Log.d(TAG, "Made it to SignIn");
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void createSignInIntent() {
        // [START auth_fui_create_intent]
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());


        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
        // [END auth_fui_create_intent]
    }

    // [START auth_fui_result]
    // THIS IS GOOGLE SIGN IN ONACTIVITY RESULT, NOT FIREBASE
    //   @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Log.d(TAG, "Made it to onActvitiyResult");
//        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            // The Task returned from this call is always completed, no need to attach
//            // a listener.
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            handleSignInResult(task);
//        }
//    }
                // THIS IS FIREBASE SIGN IN ONACTIVITYRESULT
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                           // Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }
//    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
//        Log.d(TAG, "Made it to HandleSigninresult");
//        try {
//            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
//
//            // Signed in successfully, show authenticated UI.
//            updateUI(account);
//        } catch (ApiException e) {
//            // The ApiException status code indicates the detailed failure reason.
//            // Please refer to the GoogleSignInStatusCodes class reference for more information.
//            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
//            updateUI(null);
//        }
//    }
    // [END auth_fui_result]


    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    public void delete() {
        // [START auth_fui_delete]
        AuthUI.getInstance()
                .delete(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
        // [END auth_fui_delete]
    }

    @SuppressLint("StringFormatInvalid")
    private void updateUI(FirebaseUser user) {
//        hideProgressDialog();
        TextView mStatusTextView = findViewById(R.id.mStatus);
        TextView mDetailTextView = findViewById(R.id.mDetail);
        Log.d(TAG, "I made it to UpdateUI");
        if (user != null) {


            mStatusTextView.setText(getString(R.string.google_status_fmt, user.getEmail()));
            Log.d(TAG, "User IS Found");
            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            //TODO THIS LINE BELOW IS WHAT MOVES TO LOBBY
//            startActivity(new Intent(Login.this, Lobby.class));
            findViewById(R.id.disconnect_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);

        } else {
            mStatusTextView.setText(null);
            mDetailTextView.setText(null);
            Log.d(TAG, "User NOT Found");
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.disconnect_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_button).setVisibility(View.GONE);
        }
    }

//    public void privacyAndTerms() {
//        List<AuthUI.IdpConfig> providers = Collections.emptyList();
//        // [START auth_fui_pp_tos]
//        startActivityForResult(
//                AuthUI.getInstance()
//                        .createSignInIntentBuilder()
//                        .setAvailableProviders(providers)
//                        .setTosAndPrivacyPolicyUrls(
//                                "https://example.com/terms.html",
//                                "https://example.com/privacy.html")
//                        .build(),
//                RC_SIGN_IN);
//        // [END auth_fui_pp_tos]
//    }
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.sign_in_button) {
            signIn();
        } else if (i == R.id.sign_out_button) {
            signOut();
        } else if (i == R.id.disconnect_button) {
        revokeAccess();
    }
    }
}