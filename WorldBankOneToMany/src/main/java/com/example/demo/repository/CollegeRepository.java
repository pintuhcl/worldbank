package com.example.demo.repository;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.demo.entity.College;
@Repository
public interface CollegeRepository extends JpaRepository<College, Integer>{
	
	
	@Query("select c from College c where c.cid=?1")
	College findByCollegeId(Integer collegeId);
	
	@Query("select c from College c where c.location=?1")
	List<College> findByCollegeLocation(String collegeLocation);
				
	/*
	 * join Query in jpa to get the result from two table
	 */
	@Query("SELECT u.unid,u.name,u.region,c.cid,c.name,c.location FROM University u inner join u.colleges c")
	List<Object[]> findByUniversityIdWithCollegeIdEqual();
	
	
	Page<College> findByLocation(String location,Pageable pagable);
	
	
	@Query("delete from College c where c.cid=?1")
	public void deleteCollegeByCollegeId(Integer collegeId);
}
