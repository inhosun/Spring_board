package com.example.hr.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    /** application.properties의 최대 업로드 크기(5MB)를 넘었을 때 */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSize(RedirectAttributes ra) {
        ra.addFlashAttribute("error", "업로드 파일 크기는 5MB 이하여야 합니다.");
        return "redirect:/employees";
    }
}
