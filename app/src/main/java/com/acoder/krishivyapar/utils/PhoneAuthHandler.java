package com.acoder.krishivyapar.utils;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class PhoneAuthHandler {

    private final FirebaseAuth auth;
    private final Activity activity;
    private String phone;
    private OnCodeSentListener onCodeSentListener;
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;
    private long timeout = 60L;
    private String verificationId;
    private boolean debugMode;

    public PhoneAuthHandler(@NonNull Activity activity) {
        this.activity = activity;
        auth = FirebaseAuth.getInstance();
        this.debugMode = false;
    }

    public PhoneAuthHandler(@NonNull Activity activity, boolean debugMode) {
        this.activity = activity;
        auth = FirebaseAuth.getInstance();
        this.debugMode = debugMode;
    }

    public PhoneAuthHandler(@NonNull Activity activity, long timeoutSeconds) {
        this.activity = activity;
        this.timeout = timeoutSeconds;
        auth = FirebaseAuth.getInstance();
        this.debugMode = false;
    }

    public PhoneAuthHandler(@NonNull Activity activity, long timeoutSeconds, boolean debugMode) {
        this.activity = activity;
        this.timeout = timeoutSeconds;
        auth = FirebaseAuth.getInstance();
        this.debugMode = debugMode;
    }

    public interface OnCompleteListener {
        void onSuccess(String uid);

        void onError(String message);
    }

    public interface OnCodeSentListener {
        void onSent();

        void onCodeAutoDetected(@NonNull String code);

        void onError(@NonNull String message);
    }

    public void resendOTP(@NonNull OnCodeSentListener onCodeSentListener) {
        this.onCodeSentListener = onCodeSentListener;
        if (forceResendingToken == null || phone == null)
            onCodeSentListener.onError("Resend not allowed, Please send OTP first!");
        _codeSender();
    }

    public void sendOTP(@NonNull String phone, @NonNull OnCodeSentListener onCodeSentListener) {
        this.phone = phone;
        this.onCodeSentListener = onCodeSentListener;
        forceResendingToken = null;
        _codeSender();
    }

    public void signIn(@NonNull String code, @NonNull OnCompleteListener onCompleteListener) {
        _signIn(code, onCompleteListener, false);
    }

    public void verifyCode(@NonNull String code, @NonNull OnCompleteListener onCompleteListener) {
        _signIn(code, onCompleteListener, true);
    }

    private void _signIn(@NonNull String code, @NonNull OnCompleteListener onCompleteListener, boolean isSignOut) {
        if (activity.isDestroyed()) return;
        if (debugMode) {
            onCompleteListener.onSuccess("test");
            return;
        }
        if (verificationId == null) {
            onCompleteListener.onError("Invalid Code");
            return;
        }
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        auth.signInWithCredential(credential).addOnCompleteListener(activity, task -> {
            if (activity.isDestroyed()) return;
            if (task.isSuccessful()) {
                if (isSignOut)
                    auth.signOut();
                onCompleteListener.onSuccess(auth.getUid());
            } else {
                String message = task.getException().getMessage();
                onCompleteListener.onError(message);
            }
        });
    }

    private void _codeSender() {
        if (debugMode) {
            onCodeSentListener.onSent();
            return;
        }
        PhoneAuthOptions.Builder builder = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phone)
                .setTimeout(timeout, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        if (!activity.isDestroyed())
                            onCodeSentListener.onCodeAutoDetected(Objects.requireNonNull(phoneAuthCredential.getSmsCode()));
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        if (!activity.isDestroyed())
                            onCodeSentListener.onError(e.getMessage());
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        if (activity.isDestroyed()) return;
                        PhoneAuthHandler.this.verificationId = s;
                        PhoneAuthHandler.this.forceResendingToken = forceResendingToken;
                        onCodeSentListener.onSent();
                    }
                });
        if (forceResendingToken != null)
            builder.setForceResendingToken(forceResendingToken);
        PhoneAuthProvider.verifyPhoneNumber(builder.build());
    }

    public void reset() {
        phone = null;
        verificationId = null;
        forceResendingToken = null;
    }
}