package com.example.ver3;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.BreakIterator;

public class EmailPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private static EditText ETemail;

    public static EditText getETemail() {
        return ETemail;
    }

    public void setETemail(EditText ETemail) {
        this.ETemail = ETemail;
    }

    public EditText getETpassword() {
        return ETpassword;
    }

    public void setETpassword(EditText ETpassword) {
        this.ETpassword = ETpassword;
    }

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    //protected EditText ETemail;
    protected EditText ETpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_password);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    String eTemailStr = user.getEmail().toString();
                    Intent intent = new Intent(EmailPasswordActivity.this, Base.class);
                    intent.putExtra("myStringVariableName", ""+eTemailStr);
                    startActivity(intent);
                } else {
                    // User is signed out

                }

            }
        };

        ETemail = (EditText) findViewById(R.id.et_email);
        ETpassword = (EditText) findViewById(R.id.et_password);

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            ETemail.setText(user.getEmail().toString());
        }
        findViewById(R.id.btn_sign_in).setOnClickListener(this);
        findViewById(R.id.btn_registration).setOnClickListener(this);
    }
    //При остановке приложения выход из аккаунта пользователя.
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    //При переходе в другую активность, оставляет заполненным емайл, но очищает пароль.
    @SuppressLint("SetTextI18n")
    @Override
    protected void onPause() {
        super.onPause();
        ETpassword.setText("");
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_sign_in) {
            signin(ETemail.getText().toString(), ETpassword.getText().toString());
        } else if (view.getId() == R.id.btn_registration) {
            registration(ETemail.getText().toString(), ETpassword.getText().toString());
        }
        }
    //Обработка авторизации.
    public void signin(String email, String password) {
        try {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    try {

                        if (task.isSuccessful()) {
                            Toast.makeText(EmailPasswordActivity.this, "Aвторизация успешна", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            String eTemailStr = user.getEmail().toString();
                            Intent intent = new Intent(EmailPasswordActivity.this, Base.class);
                            intent.putExtra("myStringVariableName", ""+eTemailStr);
                            startActivity(intent);
                        } else
                            Toast.makeText(EmailPasswordActivity.this, "Введены неверные данные", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        Toast.makeText(EmailPasswordActivity.this, "Введены некорректные данные", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(EmailPasswordActivity.this, "Введены некорректные данные", Toast.LENGTH_SHORT).show();
        }

    }
    //Обработка регистрации в базе Firebase.
    public void registration(String email, String password) {
        try {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    try {
                        if (task.isSuccessful()) {
                            Toast.makeText(EmailPasswordActivity.this, "Регистрация успешна", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(EmailPasswordActivity.this, "Регистрация провалена", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(EmailPasswordActivity.this, "Введены некорректные данные", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(EmailPasswordActivity.this, "Введены некорректные данные", Toast.LENGTH_SHORT).show();
        }
    }
}

