package com.example.hr.repository;

import com.example.hr.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // 이름 또는 부서에 검색어가 포함된 직원 조회
    List<Employee> findByNameContainingOrDepartmentContainingOrderByIdDesc(String name, String department);

    List<Employee> findAllByOrderByIdDesc();

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);
}
