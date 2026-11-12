package com.example.hr.dto;

import com.example.hr.domain.Employee;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
public class EmployeeForm {

    @NotBlank(message = "이름을 입력하세요.")
    @Size(max = 50, message = "이름은 50자 이하로 입력하세요.")
    private String name;

    @NotBlank(message = "부서를 입력하세요.")
    private String department;

    @NotBlank(message = "직급을 입력하세요.")
    private String position;

    @NotBlank(message = "이메일을 입력하세요.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @Min(value = 0, message = "급여는 0 이상이어야 합니다.")
    private long salary;

    @NotNull(message = "입사일을 입력하세요.")
    @PastOrPresent(message = "입사일은 오늘 이전이어야 합니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate hireDate;

    /** 업로드된 사진 (선택) */
    private MultipartFile photo;

    /** 수정 시 기존 사진 삭제 여부 */
    private boolean deletePhoto;

    /** 엔티티 -> 폼 (수정 화면용) */
    public static EmployeeForm from(Employee e) {
        EmployeeForm f = new EmployeeForm();
        f.name = e.getName();
        f.department = e.getDepartment();
        f.position = e.getPosition();
        f.email = e.getEmail();
        f.salary = e.getSalary();
        f.hireDate = e.getHireDate();
        return f;
    }
}
