package com.shop.controller;

import com.shop.constant.RoomType;
import com.shop.dto.*;
import com.shop.entity.CartItem;
import com.shop.entity.Item;
import com.shop.entity.Member;
import com.shop.entity.Reservation;
import com.shop.repository.*;
import com.shop.service.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping(value = "/kakao")
@RequiredArgsConstructor

public class KakoPayController {

private final ReservationService reservationService;
private final KakaoPayService kakaoPayService;
private final CartService cartService;

    // 카카오페이결제 요청
    @GetMapping("/pay/{itemId}")
    public @ResponseBody ReadyResponse payReady(@PathVariable("itemId")Long itemId, ItemSearchDto itemSearchDto, Model model, HttpSession httpSession) {
        // 카카오 결제 준비하기	- 결제요청 service 실행
        model.addAttribute("itemSearchDto",itemSearchDto);
        httpSession.setAttribute("itemSearchDto",itemSearchDto);
        httpSession.setAttribute("itemId",itemId);
        ReadyResponse readyResponse = kakaoPayService.payReady(itemId,itemSearchDto);
        return readyResponse; // 클라이언트에 보냄.(tid,next_redirect_pc_url이 담겨있음.)

    }

    // 결제승인요청
    @GetMapping(value = "/pay/completed/{itemId}")
    public String  approveResponse(@PathVariable("itemId")Long itemId,Principal principal, HttpSession httpSession) throws Exception {
        ItemSearchDto itemSearchDto = (ItemSearchDto) httpSession.getAttribute("itemSearchDto");

        reservationService.reservationOk(principal,httpSession,itemSearchDto);

        return "redirect:/";
    }
    // 결제 취소시 실행 url
    @GetMapping("/pay/cancel")
    public String payCancel() {
        System.out.println("취소");
        return "redirect:/";
    }

    // 결제 실패시 실행 url
    @GetMapping("/pay/fail")
    public String payFail() {
        System.out.println("실패");
        return "redirect:/";
    }

}
