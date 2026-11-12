# 인사관리 프로그램 (Spring Boot)

Spring Boot 3.3 / Java 17 / Spring Data JPA / H2 / Thymeleaf / Lombok

## 실행
IntelliJ 등에서 프로젝트를 Gradle 프로젝트로 열고 HrApplication을 실행하거나, 터미널에서 `gradle bootRun`

- 화면: http://localhost:8080/employees
- H2 콘솔: http://localhost:8080/h2-console  (JDBC URL: jdbc:h2:mem:hrdb, 사용자: sa, 비밀번호 없음)

## 기능
직원 목록 / 이름·부서 검색 / 등록 / 수정 / 삭제, 입력값 검증, 이메일 중복 검사
프로필 사진 업로드 (JPG/PNG/GIF/WEBP, 5MB 이하) / 교체 / 삭제 / 미리보기

## 사진 저장 위치
프로젝트 실행 폴더 아래 `uploads/` (application.properties의 `app.upload-dir`로 변경 가능)
DB에는 파일명만 저장하고, `/photos/파일명` 주소로 이미지를 제공합니다.
