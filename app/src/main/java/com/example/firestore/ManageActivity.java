package com.example.firestore;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.firestore.helpers.MyDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.HashMap;
import java.util.Map;

public class ManageActivity extends BaseActivity implements View.OnClickListener{
	private EditText mEditText;
	private FirebaseFirestore db;
	private CollectionReference colRefUsers;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage);

		FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
				.setHost("10.0.2.2:8080")
				.setSslEnabled(false)
				.setPersistenceEnabled(false)
				.build();

		db = FirebaseFirestore.getInstance();

		//db.setFirestoreSettings(settings);

		colRefUsers = db.collection(COLLECTION_USERS);

		bindWidget();
	}

	@Override
	public void onClick(View view) {
		String docId = mEditText.getText().toString().trim();
		switch (view.getId()) {
			case R.id.btn_add_generated:
				addDocumentWithGeneratedId();
				break;
			case R.id.btn_add_specific:
				if ("".equals(docId)) {
					mEditText.setError(getString(R.string.error_required));
				} else {
					addDocumentWithSpecificId(docId);
				}
				break;
			case R.id.btn_fetch_col:
				fetchCollection();
				break;
			case R.id.btn_fetch_doc:
				if ("".equals(docId)) {
					mEditText.setError(getString(R.string.error_required));
				} else {
					fetchDocument(docId);
				}
				break;
			case R.id.btn_update_single:
				if ("".equals(docId)) {
					mEditText.setError(getString(R.string.error_required));
				} else {
					updateSingleValue(docId);
				}
				break;
			case R.id.btn_update_nested:
				if ("".equals(docId)) {
					mEditText.setError(getString(R.string.error_required));
				} else {
					updateNestedObject(docId);
				}
				break;
			case R.id.btn_batch:
				batchMultipleWrite();
				break;
			case R.id.btn_delete_doc:
				if ("".equals(docId)) {
					mEditText.setError(getString(R.string.error_required));
				} else {
					deleteDoc(docId);
				}
				break;
			case R.id.btn_delete_field:
				if ("".equals(docId)) {
					mEditText.setError(getString(R.string.error_required));
				} else {
					deleteField(docId);
				}
				break;
		}
	}

	private void bindWidget() {
		mEditText = findViewById(R.id.edt_doc_id);
		mTextView = findViewById(R.id.tv);
		findViewById(R.id.btn_add_generated).setOnClickListener(this);
		findViewById(R.id.btn_add_specific).setOnClickListener(this);
		findViewById(R.id.btn_fetch_col).setOnClickListener(this);
		findViewById(R.id.btn_fetch_doc).setOnClickListener(this);
		findViewById(R.id.btn_update_single).setOnClickListener(this);
		findViewById(R.id.btn_update_nested).setOnClickListener(this);
		findViewById(R.id.btn_batch).setOnClickListener(this);
		findViewById(R.id.btn_delete_doc).setOnClickListener(this);
		findViewById(R.id.btn_delete_field).setOnClickListener(this);
	}

	private void addDocumentWithGeneratedId() {
		colRefUsers.add(mFirebaser).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
			@Override
			public void onSuccess(DocumentReference documentReference) {
				fetchDocument(documentReference.getId());
				mEditText.setText(documentReference.getId());
			}
		}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				mTextView.setText(e.getMessage());
			}
		});
	}

	private void addDocumentWithSpecificId(final String docId) {
		colRefUsers.document(docId).set(mFirebaseThailand).addOnSuccessListener(new OnSuccessListener<Void>() {
			@Override
			public void onSuccess(Void aVoid) {
				fetchDocument(docId);
			}
		}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				mTextView.setText(e.getMessage());
			}
		});
	}

	private void fetchCollection() {
		MyDialog.showDialog(this);
		colRefUsers.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
			@Override
			public void onComplete(@NonNull Task<QuerySnapshot> task) {
				MyDialog.dismissDialog();
				if (task.isSuccessful()) {
					mTextView.setText(null);
					for (DocumentSnapshot document : task.getResult()) {
						mTextView.append(document.getId() + "\n" + document.getData() + "\n\n");
					}
				} else {
					mTextView.setText(task.getException().getMessage());
				}
			}
		});
	}

	private void fetchDocument(String docId) {
		MyDialog.showDialog(this);
		colRefUsers.document(docId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
			@Override
			public void onSuccess(DocumentSnapshot documentSnapshot) {
				MyDialog.dismissDialog();
				if (documentSnapshot != null && documentSnapshot.exists()) {
					showData(documentSnapshot);
				} else {
					mTextView.setText(R.string.error_no_data);
				}
			}
		}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				MyDialog.dismissDialog();
				mTextView.setText(e.getMessage());
			}
		});
	}

	private void updateSingleValue(final String docId) {
		colRefUsers.document(docId).update(FIELD_IS_ADMIN, false).addOnSuccessListener(new OnSuccessListener<Void>() {
			@Override
			public void onSuccess(Void aVoid) {
				fetchDocument(docId);
			}
		}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				mTextView.setText(e.getMessage());
			}
		});
	}

	private void updateNestedObject(final String docId) {
		Map<String, Object> nestedData = new HashMap<>();
		nestedData.put(FIELD_FIRST_NAME, "Jirawat");
		nestedData.put(FIELD_LAST_NAME, "Karanwittayakarn");
		mFirebaser.put(FIELD_FULL_NAME, nestedData);
		mFirebaser.put(FIELD_DATE_TIME, FieldValue.serverTimestamp());
		colRefUsers.document(docId).update(mFirebaser).addOnCompleteListener(new OnCompleteListener<Void>() {
			@Override
			public void onComplete(@NonNull Task<Void> task) {
				if (task.isSuccessful()) {
					fetchDocument(docId);
				} else {
					mTextView.setText(R.string.error_no_data);
				}
			}
		});
	}

	private void batchMultipleWrite() {
		WriteBatch batch = db.batch();

		DocumentReference docRef1 = colRefUsers.document(DOCUMENT_FIREBASE_THAILAND);
		mFirebaseThailand.born = 1999;
		batch.set(docRef1, mFirebaseThailand);

		DocumentReference docRef2 = colRefUsers.document("Firebaser");
		batch.update(docRef2, FIELD_DATE_TIME, FieldValue.serverTimestamp());

		DocumentReference docRef3 = colRefUsers.document("Jirawatee");
		batch.delete(docRef3);

		batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
			@Override
			public void onComplete(@NonNull Task<Void> task) {
				if (task.isSuccessful()) {
					fetchCollection();
				} else {
					mTextView.setText(R.string.error_batch);
				}
			}
		});
	}

	private void deleteDoc(final String docId) {
		colRefUsers.document(docId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
			@Override
			public void onSuccess(Void aVoid) {
				fetchDocument(docId);
				mEditText.setText(null);
			}
		}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				mTextView.setText(e.getMessage());
			}
		});
	}

	private void deleteField(final String docId) {
		Map<String,Object> updates = new HashMap<>();
		updates.put(FIELD_TAGS, FieldValue.delete());
		colRefUsers.document(docId).update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
			@Override
			public void onSuccess(Void aVoid) {
				fetchDocument(docId);
			}
		}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				mTextView.setText(e.getMessage());
			}
		});
	}
}