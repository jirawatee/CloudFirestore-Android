package com.example.firestore;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.firestore.models.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryActivity extends BaseActivity implements View.OnClickListener{
	private static final String TAG = QueryActivity.class.getSimpleName();
	private FirebaseFirestore db = FirebaseFirestore.getInstance();
	private CollectionReference colRefUsers = db.collection(COLLECTION_USERS);
	private ListenerRegistration mListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query);
		bindWidget();
	}

	@Override
	protected void onStart() {
		super.onStart();
		addMultiDocsListener();
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (mListener != null) {
			mListener.remove();
		}
	}

	@Override
	public void onClick(View view) {
		if (mListener != null) {
			mListener.remove();
		}
		switch (view.getId()) {
			case R.id.btn_realtime_doc:
				addSingleDocListener();
				break;
			case R.id.btn_realtime_multi_doc:
				addMultiDocsListener();
				break;
			case R.id.btn_add_sample:
				addSampleData();
				break;
		}
	}

	private void addSampleData() {
		List<String> tags1 = Arrays.asList("Authentication", "Realtime Database", "Cloud Storage for Firebase");
		List<String> tags2 = Arrays.asList("Cloud Firestore", "Cloud Functions", "Cloud Messaging");
		List<String> tags3 = Arrays.asList("TestLab", "Crashlytics", "Performance");
		List<String> tags4 = Arrays.asList("Remote Config", "Dynamic Links", "Invites");

		Map<String, Object> nested1 = new HashMap<>();
		nested1.put(FIELD_FIRST_NAME, "Anonymous");
		nested1.put(FIELD_LAST_NAME, "Unknown");

		Map<String, Object> nested2 = new HashMap<>();
		nested2.put(FIELD_FIRST_NAME, "Firebase");
		nested2.put(FIELD_LAST_NAME, "Thailand");

		Map<String, Object> nested3 = new HashMap<>();
		nested3.put(FIELD_FIRST_NAME, "Jirawat");
		nested3.put(FIELD_LAST_NAME, "Karanwittayakarn");

		Map<String, Object> nested4 = new HashMap<>();
		nested4.put(FIELD_FIRST_NAME, "Kittisak");
		nested4.put(FIELD_LAST_NAME, "Phetrungnapa");

		User user1 = new User(nested1, 50.12, 1980, "firebaser@gmail.com", new Date(), tags1, false);
		User user2 = new User(nested2, 55.34, 1985, "firebasethailand@gmail.com", new Date(), tags2, true);
		User user3 = new User(nested3, 60.56, 1990, "jirawatee@gmail.com", new Date(), tags3, true);
		User user4 = new User(nested4, 65.78, 1995, "cs.sealsoul@gmail.com", new Date(), tags4, false);

		colRefUsers.document("Firebaser").set(user1);
		colRefUsers.document("FirebaseThailand").set(user2);
		colRefUsers.document("Jirawatee").set(user3);
		colRefUsers.document("iTopStory").set(user4);
	}

	private void addSingleDocListener() {
		mListener = colRefUsers.document(DOCUMENT_FIREBASE_THAILAND).addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
			@Override
			public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
				if (documentSnapshot != null && documentSnapshot.exists()) {
					//String source = documentSnapshot.getMetadata().hasPendingWrites() ? "Local" : "Server";
					//Log.d(TAG, source);
					showData(documentSnapshot);
				} else if (e != null) {
					mTextView.setText(e.getMessage());
				}
			}
		});
	}

	private void addMultiDocsListener() {
		Query mQuery;
		mQuery = colRefUsers;
		//mQuery = colRefUsers.limit(2);
		//mQuery = colRefUsers.orderBy(FIELD_WEIGHT, Query.Direction.ASCENDING);
		//mQuery = colRefUsers.orderBy(FIELD_WEIGHT, Query.Direction.DESCENDING).limit(3);
		//mQuery = colRefUsers.orderBy(FIELD_IS_ADMIN).orderBy(FIELD_BORN, Query.Direction.ASCENDING);
		//mQuery = colRefUsers.whereEqualTo(FIELD_IS_ADMIN, true);
		//mQuery = colRefUsers.whereLessThan(FIELD_WEIGHT, 55);
		//mQuery = colRefUsers.whereGreaterThan(FIELD_BORN, 1990);
		//mQuery = colRefUsers.whereGreaterThanOrEqualTo(FIELD_EMAIL, "firebaser@gmail.com");
		//mQuery = colRefUsers.whereLessThanOrEqualTo(FIELD_EMAIL, "firebaser@gmail.com");
		//mQuery = colRefUsers.whereEqualTo(FIELD_IS_ADMIN, false).whereEqualTo(FIELD_BORN, 1984);
		//mQuery = colRefUsers.whereEqualTo(FIELD_IS_ADMIN, false).whereLessThan(FIELD_BORN, 1984);
		//mQuery = colRefUsers.whereGreaterThanOrEqualTo(FIELD_WEIGHT, 40).whereLessThanOrEqualTo(FIELD_WEIGHT, 50);
		mListener = mQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
			@Override
			public void onEvent(QuerySnapshot querySnapshots, FirebaseFirestoreException e) {
				if (e != null) {
					Log.e(TAG, e.getMessage());
					mTextView.setText(e.getMessage());
					return;
				} else {
					mTextView.setText(null);
				}

				if (querySnapshots.getDocuments().size() > 0) {
					for (DocumentSnapshot document : querySnapshots) {
						User user = document.toObject(User.class);
						mTextView.append(document.getId());
						mTextView.append("\n");
						mTextView.append("Born: " + user.born);
						mTextView.append("\n");
						mTextView.append("Weight: " + user.weight);
						mTextView.append("\n");
						mTextView.append("Email: " + user.email);
						mTextView.append("\n");
						mTextView.append("isAdmin: " + user.isAdmin);
						mTextView.append("\n\n");
					}
				} else {
					mTextView.setText(R.string.error_no_data);
				}

				for (DocumentChange dc : querySnapshots.getDocumentChanges()) {
					switch (dc.getType()) {
						case ADDED:
							Log.d(TAG, "New: " + dc.getDocument().getId());
							break;
						case MODIFIED:
							Log.d(TAG, "Modified: " + dc.getDocument().getId());
							break;
						case REMOVED:
							Log.d(TAG, "Removed: " + dc.getDocument().getId());
							break;
					}
				}
			}
		});
	}

	private void bindWidget() {
		mTextView = findViewById(R.id.tv);
		findViewById(R.id.btn_realtime_doc).setOnClickListener(this);
		findViewById(R.id.btn_realtime_multi_doc).setOnClickListener(this);
		findViewById(R.id.btn_add_sample).setOnClickListener(this);
	}
}