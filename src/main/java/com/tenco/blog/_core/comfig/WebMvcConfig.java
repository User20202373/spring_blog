package com.tenco.blog._core.comfig;

import com.tenco.blog._core.intercepter.LoginInterceptor;
import com.tenco.blog._core.intercepter.SessionInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 자바 코드로 스프링 부트 설정 파일을 다룰 수 있다
// @Component 얘는 하나만 수행하면 가버림
@Configuration // 설정파일IoC 대상 - 하나 이상의 IoC 처리를 하고 싶을 때 사용

public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired // DI 처리
    private LoginInterceptor loginInterceptor;
    @Autowired // DI 처리
    private SessionInterceptor sessionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 화면에 SessionUser 정보를 내려줄때 사용됨

        registry.addInterceptor(sessionInterceptor)
                .addPathPatterns("/**"); //모든 URL요청에서 동작함


        //여기에 LoginInterceptor 등록할 예정
        //인증 처리 인터셉터 동작함
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/board/**", "/user/**")
                .excludePathPatterns(
                        //로그인 관련(인증이 필요없는 페이지)
                        "/login-form",    // 로그인 화면 요청 시
                        "/join-form",    // 회원가입 화면 요청 시
                        "/logout",      // 로그아웃

                        // 게시글 조회 관련(인증 없이도 볼 수 있는 페이지)
                        "/board/list",      // 게시글 목록 화면 요청
                        "/",                // 메인페이지
                        "/index",           // 메인페이지
                        "/board/{id:\\d+}", // 게시글 상세보기(숫자 ID만 허용)

                        // 정적 리소스 (CSS, JS, 이미지 등)
                        "/css/**",         // css 파일 제외
                        "/js/**",          // JS 파일 제외
                        "/images/**",      // 이미지 파일 제외
                        "/favicon.ico",    // 파비콘 제외

                        // H2 데이터베이스 콘솔(개발 환경용)
                        "/h2-console/**"   //H2 콘솔 접근
                );

    }
}
