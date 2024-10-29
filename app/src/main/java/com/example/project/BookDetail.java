package com.example.project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Dialog;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.project.R;
import com.example.project.model.Book;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BookDetail extends AppCompatActivity {

    private ImageView bookCoverDetail, btnback;
    private TextView bookTitleDetail, bookGenreDetail, bookRatingDetail, bookPriceDetail, bookAuthorDetail;
    private Button readButton, addLibraryButton;
    private DatabaseReference databaseReference;
    private ImageView iconShare;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        iconShare = findViewById(R.id.IconShare);
        iconShare.setOnClickListener(v -> showShareBottomSheet());
        bookCoverDetail = findViewById(R.id.bookCoverDetail);
        bookTitleDetail = findViewById(R.id.bookTitleDetail);
        bookGenreDetail = findViewById(R.id.bookGenreDetail);
        bookRatingDetail = findViewById(R.id.bookRatingDetail);
        bookPriceDetail = findViewById(R.id.bookPriceDetail);
        bookAuthorDetail = findViewById(R.id.bookAuthorDetail);
        readButton = findViewById(R.id.readButton);
        addLibraryButton = findViewById(R.id.addLibrary);
        btnback = findViewById(R.id.IconCLose);

        Intent intent = getIntent();
        String bookImage = intent.getStringExtra("book_image");

        if (bookImage != null) {
            Glide.with(this)
                    .load(bookImage)
                    .into(bookCoverDetail);

            fetchBookDetailsFromFirebase(bookImage);
        }

        btnback.setOnClickListener(v -> {
            finish();
        });

    }
    private void showShareBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.activity_share_book);

        ImageView closeShareSheet = bottomSheetDialog.findViewById(R.id.closeShareSheet);
        if (closeShareSheet != null) {
            closeShareSheet.setOnClickListener(v -> bottomSheetDialog.dismiss());
        }

        bottomSheetDialog.show();
    }
    private void fetchBookDetailsFromFirebase(String bookImage) { // Thêm
        databaseReference = FirebaseDatabase.getInstance().getReference("books");
        databaseReference.orderByChild("coverUrl").equalTo(bookImage).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Book book = dataSnapshot.getValue(Book.class);
                        if (book != null) {
                            bookTitleDetail.setText(book.getTitle());
                            bookGenreDetail.setText(book.getGenre());
                            bookAuthorDetail.setText(book.getAuthor());
                            bookRatingDetail.setText("5.0 (10 Nhận xét)");
                            bookPriceDetail.setText("Miễn phí - Bạn có thể thêm vào thư viện và đọc trọn vẹn cuốn sách miễn phí");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
