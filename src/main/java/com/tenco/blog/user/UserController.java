package com.tenco.blog.user;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller //IoC
@RequiredArgsConstructor // DI 처리
public class UserController {
    private final UserService userService;

    //프로필 수정 화면 요청
    // /user/update-form
    @PostMapping("/user/update")
    public String updateProc(UserRequest.UpdateDTO updateDTO, HttpSession session) {
        // 인증 검사 처리 LoginInterceptor에서 처리
        //유효성 검사
        updateDTO.validate();
        // 데이터베이스 업데이트처리
        User sessionUser = (User) session.getAttribute("sessionUser");
        User userEntity = userService.updateById(sessionUser.getId(), updateDTO);

        //세션 동기화 처리
        session.setAttribute("sessionUser", userEntity);


        return "redirect:/";
    }


    //프로필 화면 요청
    @GetMapping("/user/update-form")
    public String updateFormPage(HttpSession session, Model model) {
        //인증검사
        User sessionUser = (User) session.getAttribute("sessionUser");
        User userEntity = userService.findById(sessionUser.getId());

        //가방에 넣어서 내려주기
        model.addAttribute("user", userEntity);

        return "user/update-form";
    }


    //로그인 화면 요청
    //http://localhost:8080/login-form
    @GetMapping("/login-form")
    public String loginFormPage() {
        //인증검사 x
        // 넘겨 받는 값 없으니 유효성 검사x
        return "user/login-form";
    }

    //로그인 기능 요청
    @PostMapping("/login")
    public String loginProc(UserRequest.LoginDTO loginDTO, HttpSession session) {
        //인증검사 x ,유효성 검사 o
        loginDTO.validate();

        User userEntity = userService.login(loginDTO);
        session.setAttribute("sessionUser",userEntity);
        return "redirect:/";

    }

    //로그아웃 기능 요청
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        //세션 메모리에 내 정보를 없애 버림
        session.invalidate();
        return "redirect:/";
    }


    //회원 가입 화면 요청
    // 주소 설계 : http://localhost:8080/join-form
    @GetMapping("/join-form")
    public String joinFormPage() {
        return "user/join-form";
    }

    // 회원가입 기능 요청
    @PostMapping("/join")
    public String joinProc(UserRequest.JoinDTO joinDTO) {
        // 인증검사 x (회원가입 할 때 로그인 안되어있음)
        //유효성 검사하기 o
        joinDTO.validate(); // 유효성 검사 --> 오류 --> 예외 처리넘어감
        userService.join(joinDTO);
        return "redirect:/login-form";
    }
}
