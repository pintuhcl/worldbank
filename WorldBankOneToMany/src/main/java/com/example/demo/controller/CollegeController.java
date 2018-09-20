package com.example.demo.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.entity.College;
import com.example.demo.entity.University;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.CollegeRepository;
import com.example.demo.repository.UniversityRepository;

@RestController
public class CollegeController {
	@Autowired
	private CollegeRepository crepository;

	@Autowired
	private UniversityRepository urepository;

	@GetMapping("/college")
	public ResponseEntity<?> getCollegeList() {
		List<College> clist = crepository.findAll();
		return ResponseEntity.ok("List of College:::" + clist);
	}

	@GetMapping("/college/name/{collegeId}")
	public ResponseEntity<?> getCollegeByCollegeId(@PathVariable(value = "collegeId") Integer collegeId) {

		College college = crepository.findByCollegeId(collegeId);
		return ResponseEntity.ok("find the college details Based on collegeId-----:" + college);
	}

	@GetMapping("/college/loc/{collegeLocation}")
	public ResponseEntity<?> getCollegeByLocation(@PathVariable(value = "collegeLocation") String collegeLocation) {
		List<College> listCollegeName = crepository.findByCollegeLocation(collegeLocation);
		return ResponseEntity.ok("the list of college Name----:" + listCollegeName);
	}

	@DeleteMapping("/college/delete/{collegeId}")
	public ResponseEntity<?> deleteCollegeByCollegeId(@PathVariable(value = "collegeId") Integer collegeId) {
		crepository.deleteCollegeByCollegeId(collegeId);
		return ResponseEntity.ok("the delete object of college ----" + collegeId);
	}

	@PostMapping("/college/university/{uId}")
	public ResponseEntity<?> createPost(@PathVariable(value = "uId") Integer uId, @Valid @RequestBody College college) {
		University uniId = urepository.getOne(uId);

		System.out.println("university data with id that need to be saved in College as foreign key " + uniId);
		if (college == null) {

			return ResponseEntity.ok("College Data Not Available");
		} else {
			college.setUniversity(uniId);
			System.out.println("after saved university id with college data" + college);
			College cid = crepository.save(college);

			return ResponseEntity.ok("College Data saved with CID :" + cid);
		}
	}

	@PutMapping("/college/{cid}")
	public ResponseEntity<?> updateCollege(@PathVariable(name = "cid") Integer cid, @RequestBody College college) {
		College col = crepository.getOne(cid);

		System.out.println("collection object -:" + col);
		col.setName(college.getName());
		col.setLocation(college.getLocation());
		crepository.save(col);

		System.out.println("collection after saving the object" + col);
		return ResponseEntity.ok("we can update the College bY college id" + col);

		/*
		 * return crepository.findById(cid).map(col-> { col.setName(col.getName());
		 * col.setLocation(col.getLocation()); return crepository.save(col);
		 * }).orElseThrow(()->new
		 * ResourceNotFoundException("collegeId"+cid+"not found"));
		 * 
		 * }
		 */
	}

	@DeleteMapping("/college/{cid}")
	public ResponseEntity<?> deleteCollege(@PathVariable(name = "cid") Integer cid) {
		return crepository.findById(cid).map(college -> {

			/*
			 * here we want to delete the child so we need to set the parent as null
			 * otherwise while deleting child parent also deleted as well as no of child of
			 * parent also deleted
			 */
			college.setUniversity(null);
			crepository.delete(college);
			return ResponseEntity.ok("college is deleted with " + cid);

		}).orElseThrow(() -> new ResourceNotFoundException("collegeId" + cid + "not found"));

	}

	@GetMapping("/university/college")
	public ResponseEntity<?> getUniversityCollegeById() {
		List<Object[]> listObj = crepository.findByUniversityIdWithCollegeIdEqual();
		listObj.forEach(System.out::println);
		return ResponseEntity.ok("After inner joining the Query  ----:" + listObj);
	}

	/*
	 * Sort sort = new Sort(new Sort.Order(Direction.ASC, "lastName"));
	 * List<Employee> employees = repo.findBySalaryGreaterThan(new Long(10000),
	 * sort);
	 * 
	 * 
	 * Sort sort = new Sort(new Sort.Order(Direction.ASC, "lastName")); Pageable
	 * pageable = new PageRequest(0, 5, sort); List<Employee> employees =
	 * repo.findBySalaryGreaterThan(new Long(10000), pageable);
	 * 
	 * 
	 * 
	 */

	@GetMapping("/college/sort/{location}")
	public ResponseEntity<?> getCollegePageBy3Record(@PathVariable(name = "location") String location) {
		Page<College> list = crepository.findByLocation(location, new PageRequest(0, 5, Direction.ASC, "cid"));
		System.out.println(list.getNumberOfElements());
		list.forEach(System.out::println);
	

		return ResponseEntity
				.ok("page is sorted by cid and display in ascending order of location---:" + list);

	}

}
