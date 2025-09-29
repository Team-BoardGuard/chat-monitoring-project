package com.example.authapp.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public String signup(UserForm userForm) {
        User user = new User();
        user.setUsername(userForm.getUsername());
        user.setPassword(userForm.getPassword());
        user.setNickname(userForm.getNickname());
        user.setEmail(userForm.getEmail());
        userService.signup(user);
        return "redirect:/login";
    }

    @GetMapping("/signup")
    public String showSignupPage(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "signup";
    }

    @GetMapping("/")
    public String showRootPage() {
        return "home";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Invalidate session
        return "redirect:/home"; // Redirect to home page
    }

    @GetMapping("/home")
    public String showHomePage() {
        return "home";
    }

    @GetMapping("/gallery")
    public String showGalleryPage() {
        return "gallery";
    }

    @GetMapping("/freeboard")
    public String showBoardPage() {
        return "freeboard";
    }

    @GetMapping("/my-info")
    public String showMyInfoPage(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        return "my-info";
    }

    @GetMapping("/edit-profile")
    public String showEditProfilePage(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        ProfileEditForm profileEditForm = new ProfileEditForm();
        profileEditForm.setNickname(user.getNickname());
        model.addAttribute("profileEditForm", profileEditForm);
        return "edit-profile";
    }

    @PostMapping("/edit-profile")
    public String updateProfile(@ModelAttribute("profileEditForm") ProfileEditForm profileEditForm, Principal principal) {
        userService.updateProfile(principal.getName(), profileEditForm);
        return "redirect:/my-info";
    }

    @GetMapping("/change-password")
    public String showChangePasswordPage(Model model) {
        model.addAttribute("passwordChangeForm", new PasswordChangeForm());
        return "change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@ModelAttribute("passwordChangeForm") PasswordChangeForm form, Principal principal, RedirectAttributes redirectAttributes) {
        boolean success = userService.changePassword(principal.getName(), form);
        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", "비밀번호가 성공적으로 변경되었습니다.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "비밀번호 변경에 실패했습니다. 현재 비밀번호가 다르거나 새 비밀번호가 일치하지 않습니다.");
        }
        return "redirect:/my-info";
    }

    @GetMapping("/confirm-delete")
    public String showConfirmDeletePage() {
        return "confirm-delete";
    }

    @PostMapping("/delete-account")
    public String deleteAccount(Principal principal, HttpSession session) {
        userService.deleteUser(principal.getName());
        session.invalidate();
        return "redirect:/home";
    }
}