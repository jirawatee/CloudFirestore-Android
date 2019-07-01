package com.example.firestore;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import com.example.firestore.models.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseActivity extends AppCompatActivity{
	protected static final String COLLECTION_USERS = "users";
	protected static final String DOCUMENT_FIREBASE_THAILAND = "FirebaseThailand";
	protected static final String FIELD_FULL_NAME = "fullName";
	protected static final String FIELD_FIRST_NAME = "firstName";
	protected static final String FIELD_LAST_NAME = "lastName";
	protected static final String FIELD_BORN = "born";
	protected static final String FIELD_EMAIL = "email";
	protected static final String FIELD_WEIGHT = "weight";
	protected static final String FIELD_TAGS = "tags";
	protected static final String FIELD_DATE_TIME = "dateTime";
	protected static final String FIELD_IS_ADMIN = "isAdmin";

	protected Map<String, Object> mFirebaser;
	protected User mFirebaseThailand;
	protected TextView mTextView;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		List<String> tagsFirebaser = Arrays.asList("iOS", "Android", "Web");
		List<String> tagsFirebaseThailand = Arrays.asList("Cloud Firestore", "Cloud Functions", "Cloud Messaging");

		Map<String, Object> nestedFirebaser = new HashMap<>();
		nestedFirebaser.put(FIELD_FIRST_NAME, "Anonymous");
		nestedFirebaser.put(FIELD_LAST_NAME, "Unknown");

		Map<String, Object> nestedFirebaseThailand = new HashMap<>();
		nestedFirebaseThailand.put(FIELD_FIRST_NAME, "Firebase");
		nestedFirebaseThailand.put(FIELD_LAST_NAME, "Thailand");

		mFirebaser = new HashMap<>();
		mFirebaser.put(FIELD_FULL_NAME, nestedFirebaser);
		mFirebaser.put(FIELD_BORN, 1983);
		mFirebaser.put(FIELD_IS_ADMIN, false);
		mFirebaser.put(FIELD_WEIGHT, 43.21);
		mFirebaser.put(FIELD_TAGS, tagsFirebaser);
		mFirebaser.put(FIELD_EMAIL, "anonymous@gmail.com");
		mFirebaser.put(FIELD_DATE_TIME, FieldValue.serverTimestamp());

		mFirebaseThailand = new User(
				nestedFirebaseThailand,
				54.32,
				1984,
				"firebasethailand@gmail.com",
				new Date(),
				tagsFirebaseThailand,
				true
		);
	}

	protected void showData(DocumentSnapshot documentSnapshot) {
		String id = documentSnapshot.getId();

		/*
		Map<String, Object> fullName = (Map<String, Object>) documentSnapshot.get(FIELD_FULL_NAME);
		double weight = documentSnapshot.getDouble(FIELD_WEIGHT);
		long born = documentSnapshot.getLong(FIELD_BORN);
		String email = documentSnapshot.getString(FIELD_EMAIL);
		Date dateTime = documentSnapshot.getDate(FIELD_DATE_TIME);
		List<String> tags = (List<String>) documentSnapshot.get(FIELD_TAGS);
		boolean isAdmin = documentSnapshot.getBoolean(FIELD_IS_ADMIN);
		*/

		User user = documentSnapshot.toObject(User.class);
		Map<String, Object> fullName = user.fullName;
		double weight = user.weight;
		long born = user.born;
		String email = user.email;
		Date dateTime = user.dateTime;
		boolean isAdmin = user.isAdmin;
		List<String> tags = user.tags;

		mTextView.setText(String.format("ID: %s", id));
		mTextView.append("\n\n");
		mTextView.append("Name: " + fullName.get(FIELD_FIRST_NAME) + " " + fullName.get(FIELD_LAST_NAME));
		mTextView.append("\n\n");
		mTextView.append("Weight: " + weight);
		mTextView.append("\n\n");
		mTextView.append("Email: " + email);
		mTextView.append("\n\n");
		mTextView.append("Born: " + born);
		mTextView.append("\n\n");
		mTextView.append("DateTime: " + dateTime);
		mTextView.append("\n\n");
		mTextView.append("Tags: " + tags);
		mTextView.append("\n\n");
		mTextView.append("IsAdmin: " + isAdmin);
	}
}