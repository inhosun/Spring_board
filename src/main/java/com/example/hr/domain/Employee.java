package com.example.hr.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    private String department;

    @Column(nullable = false, length = 50)
    private String position;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private long salary;

    @Column(nullable = false)
    private LocalDate hireDate;

    /** 서버에 저장된 사진 파일명 (없으면 null) */
    @Column(length = 100)
    private String photoFileName;

    @Builder
    public Employee(String name, String department, String position,
                    String email, long salary, LocalDate hireDate, String photoFileName) {
        this.name = name;
        this.department = department;
        this.position = position;
        this.email = email;
        this.salary = salary;
        this.hireDate = hireDate;
        this.photoFileName = photoFileName;
    }

    /** 정보 수정 (setter 대신 의미 있는 메서드로 변경) */
    public void update(String name, String department, String position,
                       String email, long salary, LocalDate hireDate) {
        this.name = name;
        this.department = department;
        this.position = position;
        this.email = email;
        this.salary = salary;
        this.hireDate = hireDate;
    }

    public void changePhoto(String photoFileName) {
        this.photoFileName = photoFileName;
    }
}
