package com.example.project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.project.ui.Menu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton, registerButton;
    private ImageButton backButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Khởi tạo các thành phần giao diện
        usernameEditText = findViewById(R.id.user);
        passwordEditText = findViewById(R.id.pass);
        loginButton = findViewById(R.id.button);
        registerButton = findViewById(R.id.btnNoUser);
        backButton = findViewById(R.id.imgbtnBack);

        // Đặt listener cho nút đăng nhập
        loginButton.setOnClickListener(v -> loginUser());

        // Listener cho nút đăng ký
        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, Sign_up.class);
            startActivity(intent);
        });

        // Listener cho nút quay lại
        backButton.setOnClickListener(v -> {
            finish();
        });

        // Áp dụng Window Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loginUser() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Kiểm tra thông tin nhập
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(Login.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Kiểm tra tài khoản người dùng
        usersRef.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String storedPassword = userSnapshot.child("password").getValue(String.class);
                        String userId = userSnapshot.getKey();

                        // Kiểm tra mật khẩu
                        if (storedPassword != null && storedPassword.equals(password)) {
                            Toast.makeText(Login.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                            // Lưu userId vào SharedPreferences
                            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("user_id", userId); // Lưu userId
                            editor.apply();

                            // Chuyển sang Menu Activity
                            Intent intent = new Intent(Login.this, Menu.class);
                            intent.putExtra("user_id", userId); // Truyền userId vào Intent
                            startActivity(intent);
                            finish();
                            return;
                        }
                    }
                    Toast.makeText(Login.this, "Mật khẩu không đúng.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Login.this, "Tài khoản không tồn tại.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Login.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
