package com.example.hr.service;

import com.example.hr.domain.Employee;
import com.example.hr.dto.EmployeeForm;
import com.example.hr.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PhotoStorageService photoStorage;

    public List<Employee> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return employeeRepository.findAllByOrderByIdDesc();
        }
        String k = keyword.trim();
        return employeeRepository.findByNameContainingOrDepartmentContainingOrderByIdDesc(k, k);
    }

    public Employee get(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 직원입니다. id=" + id));
    }

    public boolean isEmailTaken(String email, Long excludeId) {
        return excludeId == null
                ? employeeRepository.existsByEmail(email)
                : employeeRepository.existsByEmailAndIdNot(email, excludeId);
    }

    public String validatePhoto(MultipartFile photo) {
        return photoStorage.validate(photo);
    }

    @Transactional
    public Long create(EmployeeForm f) {
        String photoFileName = photoStorage.store(f.getPhoto());
        Employee e = Employee.builder()
                .name(f.getName())
                .department(f.getDepartment())
                .position(f.getPosition())
                .email(f.getEmail())
                .salary(f.getSalary())
                .hireDate(f.getHireDate())
                .photoFileName(photoFileName)
                .build();
        return employeeRepository.save(e).getId();
    }

    @Transactional
    public void update(Long id, EmployeeForm f) {
        Employee e = get(id);
        // 변경 감지(Dirty Checking)로 UPDATE 쿼리가 자동 실행됨
        e.update(f.getName(), f.getDepartment(), f.getPosition(),
                f.getEmail(), f.getSalary(), f.getHireDate());

        String oldPhoto = e.getPhotoFileName();
        if (f.getPhoto() != null && !f.getPhoto().isEmpty()) {
            // 새 사진을 올린 경우: 새 파일 저장 후 기존 파일 삭제
            e.changePhoto(photoStorage.store(f.getPhoto()));
            photoStorage.delete(oldPhoto);
        } else if (f.isDeletePhoto()) {
            // 사진 삭제만 체크한 경우
            e.changePhoto(null);
            photoStorage.delete(oldPhoto);
        }
    }

    @Transactional
    public void delete(Long id) {
        Employee e = get(id);
        photoStorage.delete(e.getPhotoFileName());
        employeeRepository.delete(e);
    }
}
