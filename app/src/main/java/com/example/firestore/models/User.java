package com.example.firestore.models;


import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.Date;
import java.util.List;
import java.util.Map;

@IgnoreExtraProperties
public class User {
	public String email;
	public Map<String, Object> fullName;
	public boolean isAdmin;
	public double weight;
	public long born;
	public Date dateTime;
	public List<String> tags;

	public User() {}

	public User(Map<String, Object> fullName, double weight, long born, String email, Date dateTime, List<String> tags, boolean isAdmin) {
		this.fullName = fullName;
		this.weight = weight;
		this.born = born;
		this.email = email;
		this.dateTime = dateTime;
		this.tags = tags;
		this.isAdmin = isAdmin;
	}
}