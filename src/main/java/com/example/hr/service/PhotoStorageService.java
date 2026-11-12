package com.example.hr.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

/** 사진 파일을 로컬 디스크에 저장/삭제하는 서비스 */
@Service
public class PhotoStorageService {

    private static final long MAX_SIZE = 5L * 1024 * 1024; // 5MB
    // 허용하는 이미지 타입 -> 저장할 확장자
    private static final Map<String, String> ALLOWED = Map.of(
            "image/jpeg", ".jpg",
            "image/png", ".png",
            "image/gif", ".gif",
            "image/webp", ".webp");

    private final Path root;

    public PhotoStorageService(@Value("${app.upload-dir}") String uploadDir) throws IOException {
        this.root = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(root);
    }

    public Path getRoot() {
        return root;
    }

    /** 문제가 있으면 오류 메시지, 정상(또는 파일 없음)이면 null */
    public String validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        if (file.getSize() > MAX_SIZE) {
            return "사진 크기는 5MB 이하여야 합니다.";
        }
        if (!ALLOWED.containsKey(file.getContentType())) {
            return "JPG, PNG, GIF, WEBP 이미지만 업로드할 수 있습니다.";
        }
        return null;
    }

    /** 파일 저장 후 저장된 파일명 반환 (파일이 없으면 null) */
    public String store(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        String ext = ALLOWED.get(file.getContentType());
        if (ext == null) {
            throw new IllegalArgumentException("허용되지 않는 파일 형식입니다.");
        }
        // 사용자가 보낸 원본 파일명은 쓰지 않고 UUID로 새로 만든다 (경로 조작/중복 방지)
        String savedName = UUID.randomUUID() + ext;
        try {
            file.transferTo(root.resolve(savedName));
        } catch (IOException e) {
            throw new UncheckedIOException("사진 저장에 실패했습니다.", e);
        }
        return savedName;
    }

    public void delete(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            return;
        }
        Path target = root.resolve(fileName).normalize();
        if (!target.startsWith(root)) {
            return; // 업로드 폴더 밖은 건드리지 않음
        }
        try {
            Files.deleteIfExists(target);
        } catch (IOException e) {
            // 파일 삭제 실패가 업무 처리를 막지 않도록 무시 (실무에서는 로그 남기기)
        }
    }
}
