package com.tenco.blog.board;


import com.tenco.blog._core.errors.*;
import com.tenco.blog.user.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller // IoC
@RequiredArgsConstructor // DI(디팬던시 인젝션)
public class BoardController {

    private final BoardService boardService;

    /**
     * H2 DB 주소
     * http://localhost:8080/h2-console
     * <p>
     * 게시글 작성 화면 요청
     *
     * @return 페이지 반환
     * 주소 설계: http://localhost:8080/board/save-form
     */
    //자원의 요청
    @GetMapping("/board/save-form")
    public String saveForm(HttpSession httpSession) {
        // 1. 인증검사는 LoginInterceptor에서 먼저 처리함
        return "board/save-form";
    }

    /**
     * 게시글 작성 기능 요청
     *
     * @return 페이지 반환
     * 주소 설계: http://localhost:8080/board/save
     */
    // 자원의 생성
    @PostMapping("/board/save")
    public String saveProc(BoardRequest.SaveDTO saveDTO, HttpSession session) {

        User sessionUser = (User) session.getAttribute("sessionUser");

        saveDTO.validate();
        boardService.save(saveDTO, sessionUser);
        return "redirect:/";
    }


    /**
     * 게시글 목록 화면 요청
     *
     * @return 페이지 반환
     * 주소 설계: http://localhost:8080/
     */
    @GetMapping({"/", "index"})
    public String list(Model model) {

        List<Board> boardList = boardService.findAll();
        model.addAttribute("boardList", boardList);
        return "board/list";
    }

    //게시글 상세보기 화면 요청
// http://localhost:8080/board/1
    @GetMapping("/board/{id}")
    public String detailPage(@PathVariable(name = "id") Integer id, Model model) {
        //유효성 검사. 인증 검사

        Board board = boardService.findById(id);
        System.out.println(board.getUser().getUsername());
        model.addAttribute("board", board);
        return "board/detail";
    }


    // 삭제 기능 요청
    @PostMapping("/board/{id}/delete")
    public String deleteProc(@PathVariable(name = "id") Integer id, HttpSession session) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        boardService.deleteById(id, sessionUser);
        return "redirect:/";
    }


    // http://localhost:8080/board/1/update-form
    // 게시글 수정 화면 요청
    @GetMapping("/board/{id}/update-form")
    public String updateFormPage(@PathVariable(name = "id") Integer id, Model model, HttpSession session) {

        User sessionUser = (User) session.getAttribute("sessionUser");
        // findById <-- 상세보기 화면 요청이라서 누구나 요청 가능 (즉 인가처리 안되고 있음)
        Board boardEntity = boardService.findByIdAndCheckOwner(id, sessionUser);
        model.addAttribute("board", boardEntity);
        return "board/update-form";
    }

    // /board/{id}/update
    @PostMapping("/board/{id}/update")

    public String update(@PathVariable(name = "id") Integer id, BoardRequest.UpdateDTO updateDTO, HttpSession session) {

        //인증검사
        User sessionUser = (User) session.getAttribute("sessionUser");
        updateDTO.validate();
        boardService.updateById(id, updateDTO, sessionUser);


        return "redirect:/board/" + id;
    }

}

