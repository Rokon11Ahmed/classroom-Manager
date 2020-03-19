package com.HemontoSoftware.classroommanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    EditText name, email, password, confirmPassword;
    Button register;
    RadioButton radioButtonStudent, radioButtonTeacher;
    String registerAs ="";

    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().setTitle("Sign Up");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = findViewById(R.id.nameId);
        email = findViewById(R.id.emailId);
        password = findViewById(R.id.passwordId);
        confirmPassword = findViewById(R.id.confirmPasswordId);
        register = findViewById(R.id.registerbtnId);
        radioButtonTeacher = findViewById(R.id.radioButtonTeacherId);
        radioButtonStudent = findViewById(R.id.radioButtonStudentId);

        firebaseAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String cName = name.getText().toString().trim();
                final String cEmail = email.getText().toString().trim();
                String cPassword = password.getText().toString().trim();
                String cConfirmPassword = confirmPassword.getText().toString().trim();
                if (radioButtonTeacher.isChecked()){
                    registerAs ="Teacher";
                }else if (radioButtonStudent.isChecked()){
                    registerAs ="Student";
                }

                if (cName.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Enter Name", Toast.LENGTH_SHORT).show();
                }else if (cEmail.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Enter Email", Toast.LENGTH_SHORT).show();
                }else if (cPassword.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_SHORT).show();
                }else if (cConfirmPassword.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Enter Confirm Password", Toast.LENGTH_SHORT).show();
                }else {
                    firebaseAuth.createUserWithEmailAndPassword(cEmail, cPassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        User user = new User(cName, cEmail, registerAs);
                                        FirebaseDatabase.getInstance().getReference("Users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(getApplicationContext(), "Register Successful", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                            }
                                        });
                                    }else {
                                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }
        });
    }


}
