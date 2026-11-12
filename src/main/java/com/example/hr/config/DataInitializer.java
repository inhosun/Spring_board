package com.example.hr.config;

import com.example.hr.domain.Employee;
import com.example.hr.repository.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

/** 앱 시작 시 샘플 데이터 입력 */
@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(EmployeeRepository repo) {
        return args -> {
            repo.save(Employee.builder().name("김민수").department("개발팀").position("팀장")
                    .email("minsu@example.com").salary(6500000).hireDate(LocalDate.of(2018, 3, 2)).build());
            repo.save(Employee.builder().name("이서연").department("인사팀").position("대리")
                    .email("seoyeon@example.com").salary(4200000).hireDate(LocalDate.of(2021, 7, 15)).build());
            repo.save(Employee.builder().name("박지훈").department("영업팀").position("사원")
                    .email("jihun@example.com").salary(3300000).hireDate(LocalDate.of(2023, 1, 9)).build());
        };
    }
}
