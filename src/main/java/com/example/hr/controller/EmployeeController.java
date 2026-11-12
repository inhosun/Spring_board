package com.example.hr.controller;

import com.example.hr.dto.EmployeeForm;
import com.example.hr.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public String list(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("employees", employeeService.search(keyword));
        model.addAttribute("keyword", keyword);
        return "employees/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("employeeForm", new EmployeeForm());
        model.addAttribute("employeeId", null);
        model.addAttribute("currentPhoto", null);
        return "employees/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute EmployeeForm employeeForm,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes ra) {
        if (employeeService.isEmailTaken(employeeForm.getEmail(), null)) {
            bindingResult.rejectValue("email", "duplicate", "이미 사용 중인 이메일입니다.");
        }
        String photoError = employeeService.validatePhoto(employeeForm.getPhoto());
        if (photoError != null) {
            bindingResult.rejectValue("photo", "invalid", photoError);
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("employeeId", null);
            model.addAttribute("currentPhoto", null);
            return "employees/form";
        }
        employeeService.create(employeeForm);
        ra.addFlashAttribute("message", "직원이 등록되었습니다.");
        return "redirect:/employees";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        var employee = employeeService.get(id);
        model.addAttribute("employeeForm", EmployeeForm.from(employee));
        model.addAttribute("employeeId", id);
        model.addAttribute("currentPhoto", employee.getPhotoFileName());
        return "employees/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute EmployeeForm employeeForm,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes ra) {
        if (employeeService.isEmailTaken(employeeForm.getEmail(), id)) {
            bindingResult.rejectValue("email", "duplicate", "이미 사용 중인 이메일입니다.");
        }
        String photoError = employeeService.validatePhoto(employeeForm.getPhoto());
        if (photoError != null) {
            bindingResult.rejectValue("photo", "invalid", photoError);
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("employeeId", id);
            model.addAttribute("currentPhoto", employeeService.get(id).getPhotoFileName());
            return "employees/form";
        }
        employeeService.update(id, employeeForm);
        ra.addFlashAttribute("message", "직원 정보가 수정되었습니다.");
        return "redirect:/employees";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        employeeService.delete(id);
        ra.addFlashAttribute("message", "직원이 삭제되었습니다.");
        return "redirect:/employees";
    }
}
