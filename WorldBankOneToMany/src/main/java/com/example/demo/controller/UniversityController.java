package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.example.demo.repository.CollegeRepository;
import com.example.demo.repository.UniversityRepository;

@RestController
public class UniversityController {
	@Autowired
	private UniversityRepository urepository;
	@Autowired
	private CollegeRepository crepository;
	
	@PostMapping("/university/save")
	public ResponseEntity<?> saveUniversity( @RequestBody University university)
	{
		University univ=urepository.save(university);
		return ResponseEntity.ok("University Object is saved ----:"+univ);
	}
	
	@GetMapping("/university/get")
	public ResponseEntity<?> getAllUniversityData(){
		List<University> listuniversity=urepository.findAll();
		return ResponseEntity.ok("list of university ------:"+listuniversity);
	}
	
	@PutMapping("/university/update/{uId}")
	public ResponseEntity<?> updateUniversity(@PathVariable Integer uId, @RequestBody University university)
	{
		University univ=urepository.getOne(uId);
		univ.setName(university.getName());
		univ.setRegion(university.getRegion());
		urepository.save(univ);
		 return ResponseEntity.ok("we can update the University with uId ---:"+univ);
		
	}
	
	@DeleteMapping("/university/delete/{uId}")
	public ResponseEntity<?> deleteUniversity(@PathVariable Integer uId)
	{
		University uni=urepository.getOne(uId);
		System.out.println("university object deleted with id---:"+uni);
		urepository.delete(uni);
		
		return ResponseEntity.ok("university Object deleted with ID----:"+uId);
		
	}
	
	
	
	
}
