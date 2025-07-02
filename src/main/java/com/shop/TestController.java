package com.shop;

import com.shop.dto.*;
import com.shop.entity.Board;
import com.shop.entity.Item;
import com.shop.repository.BoardRepository;
import com.shop.repository.CommentRepository;
import com.shop.repository.MemberRepository;
import com.shop.service.BoardService;
import com.shop.service.CommentService;
import com.shop.service.ItemService;
import com.shop.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;


@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class TestController {
    private final MemberService memberService;
    private final BoardService boardService;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final CommentService commentService;
    private final MemberRepository memberRepository;
    private final HttpSession httpSession;

    @GetMapping("/hello")
    public List<String> getTest(){
        System.out.println("테스트 컨트롤러2");
        List<String> list = new ArrayList<>();
        list.add("item");
        list.add("string");
        return list;
    }


}
