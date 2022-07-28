package com.example.dto;

public class CourseDTO {
	String courseName;
	Float rating;
	
	
	public CourseDTO(String courseName, Float rating) {
		super();
		this.courseName = courseName;
		this.rating = rating;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public Float getRating() {
		return rating;
	}
	public void setRating(Float rating) {
		this.rating = rating;
	}
	
	
	
}
