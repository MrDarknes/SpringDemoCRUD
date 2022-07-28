package com.example.endpoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.CourseDTO;

@RestController
//@RequestMapping("/courses")
@RequestMapping(value = "/courses", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
public class CourseAPI {

	private List<CourseDTO> topCourses = new ArrayList<CourseDTO>(
			List.of(new CourseDTO("React", 4.6f), new CourseDTO("GraphQL", 5f), new CourseDTO("Node", 4.8f)));

	@GetMapping()
	public ResponseEntity<String> getAllCourses() throws Exception {
		JSONObject responseJson = new JSONObject();
		if (this.topCourses.isEmpty()) {
			responseJson.put("message", "Oops! The list of courses is empty");
			return new ResponseEntity<String>(responseJson.toJSONString(), HttpStatus.NOT_FOUND);
		}

		JSONArray topCoursesJArray = listArraytoJsonArray(topCourses);
		return new ResponseEntity<String>(topCoursesJArray.toJSONString(), HttpStatus.OK);
	}

	// REST endpoint to FETCH particular course from the course array
	@GetMapping(value = "/{course}")
	public ResponseEntity<String> getCourse(@PathVariable("course") String course) throws Exception {

		Optional<CourseDTO> dataOptional = topCourses.stream()
				.filter(c -> c.getCourseName().toLowerCase().equals(course.toLowerCase())).findFirst();
		JSONObject responseJson = new JSONObject();
		if (dataOptional.isPresent()) {
			CourseDTO data = dataOptional.get();
			responseJson.put("message", "Rating of " + data.getCourseName() + "  is " + data.getRating());
			return new ResponseEntity<String>(responseJson.toJSONString(), HttpStatus.OK);
		}
		responseJson.put("message", "Oops! Cannot find " + course);
		return new ResponseEntity<String>(responseJson.toJSONString(), HttpStatus.NOT_FOUND);
	}

	@PostMapping("/add")
	public ResponseEntity<String> addCourse(@RequestBody CourseDTO course) {
		Optional<CourseDTO> optionalData = topCourses.stream()
				.filter(c -> c.getCourseName().toString().equals(course.getCourseName().toLowerCase())).findFirst();
		JSONObject responseJson = new JSONObject();
		if (optionalData.isPresent()) {
			responseJson.put("message", "Course Already Exist");
			return new ResponseEntity<String>(responseJson.toJSONString(), HttpStatus.CONFLICT);
		} else {
			JSONArray responseJArray = new JSONArray();
			if (topCourses.add(course)) {
				topCourses.forEach(c -> {
					JSONObject o = new JSONObject();
					o.put("courseName", c.getCourseName());
					o.put("rating", c.getRating());
					responseJArray.add(o);
				});
				return new ResponseEntity<String>(responseJArray.toJSONString(), HttpStatus.CREATED);
			} else {
				responseJson.put("message", "Insertion Failed");
				return new ResponseEntity<String>(responseJson.toJSONString(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}

	@PutMapping("{course}")
	public ResponseEntity<String> updateCourse(@PathVariable("course") String course,
			@RequestBody CourseDTO courseDTO) {
		JSONObject responseJson = new JSONObject();
		Optional<CourseDTO> dataOptional = topCourses.stream()
				.filter(c -> c.getCourseName().toLowerCase().equals(course.toLowerCase())).findFirst();
		if (dataOptional.isPresent()) {
			CourseDTO data = dataOptional.get();
			topCourses.get(topCourses.indexOf(data)).setRating(courseDTO.getRating());
			responseJson.put("message", "Rating of " + course + "is " + topCourses.get(topCourses.indexOf(data)));
			return new ResponseEntity<String>(responseJson.toJSONString(), HttpStatus.OK);
		}
		responseJson.put("message", "Oops! Cannot find " + course);
		return new ResponseEntity<String>(responseJson.toJSONString(), HttpStatus.NOT_FOUND);

	}

	@DeleteMapping("{course}")
	public ResponseEntity<String> deleteCourse(@PathVariable("course") String course) {
		JSONObject responseJson = new JSONObject();
		Optional<CourseDTO> dataOptional = topCourses.stream()
				.filter(c -> c.getCourseName().toLowerCase().equals(course.toLowerCase())).findFirst();
		if (dataOptional.isPresent()) {
			CourseDTO data = dataOptional.get();
			topCourses.remove(data);
			JSONArray topCoursesJArray = listArraytoJsonArray(topCourses);

			return new ResponseEntity<String>(topCoursesJArray.toJSONString(), HttpStatus.OK);
		}
		responseJson.put("message", "Oops! Cannot find:" + course);
		return new ResponseEntity<String>(responseJson.toJSONString(), HttpStatus.NOT_FOUND);

	}

	@SuppressWarnings("unchecked")
	private JSONArray listArraytoJsonArray(List<CourseDTO> list) {
		JSONArray jsonArray = new JSONArray();
		list.forEach(o -> {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("courseName", o.getCourseName());
			jsonObject.put("rating", o.getRating());
			jsonArray.add(jsonObject);
		});
		return jsonArray;
	}

}
