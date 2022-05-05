package com.example.demo.student;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
	
	private final StudentRepository studentRepository;
	
	@Autowired
	public StudentService(StudentRepository studentRepository) {		
		this.studentRepository = studentRepository;
	}

	public List<Student> getStudents() {
		return studentRepository.findAll();
	}
	
	public void addNewStudent(Student student) {
		verifyEmail(student.getEmail());		
		studentRepository.save(student);		
	}

	public void deleteStudent(Long id) {
		boolean exists = studentRepository.existsById(id);
		if(!exists)
			throw new IllegalStateException("student with id " + id + "does not exists");
		studentRepository.deleteById(id);  		
	}

	@Transactional
	public void updateStudent(Long studentId, String name, String email) { 
		Student st = studentRepository.findById(studentId)
				.orElseThrow(() -> new IllegalStateException("student with id: " + studentId + 
						" does not exists"));
		
		if(name != null && name.length() > 0 && !name.equals(st.getName()))
			st.setName(name);
		if(email != null && email.length() > 0 && !email.equals(st.getEmail())) {
			verifyEmail(email);
			st.setEmail(email);
		}					
	}
	
	private void verifyEmail(String email) {
		Optional<Student> studentOptional = studentRepository.findStudentByEmail(email);
		if(studentOptional.isPresent()) {
			throw new IllegalStateException("email taken");
		}
	}
}
