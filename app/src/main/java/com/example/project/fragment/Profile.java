package com.example.project.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.project.MainActivity;
import com.example.project.ui.History;
import com.example.project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends Fragment {
    private TextView tvLib, tvUser;
    private ImageButton btnLogout;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        tvLib = view.findViewById(R.id.tv_Lib);
        tvUser = view.findViewById(R.id.tv_name);

        btnLogout = view.findViewById(R.id.logout);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("user_id", null);

        if (userId != null) {
            CountBook(userId, tvLib); // Gọi hàm CountBook để đếm sách
            Nameuser(userId, tvUser);
        }

        LinearLayout linearHistory = view.findViewById(R.id.linear_History);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });

        linearHistory.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), History.class);
            startActivity(intent);
        });

        return view;
    }

    public void CountBook(String userId, TextView tvLib) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId).child("library");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = (int) snapshot.getChildrenCount();
                tvLib.setText(count+" cuốn sách");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi có lỗi xảy ra
            }
        });
    }
    public void Nameuser(String userId, TextView tvUser) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId).child("username");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.getValue(String.class);
                tvUser.setText(name);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

        }
    });
    }
}
