package com.shop.controller;

import com.shop.constant.RoomType;
import com.shop.dto.*;
import com.shop.entity.CartItem;
import com.shop.entity.Member;
import com.shop.entity.Reservation;
import com.shop.repository.CartItemRepository;
import com.shop.service.CartItemService;
import com.shop.service.CartService;
import com.shop.service.ItemService;
import com.shop.service.MemberService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final MemberService memberService;
    private final HttpSession httpSession;
    private final ItemService itemService;
    private final CartItemService cartItemService;

    @PostMapping(value = "/cart")
    public @ResponseBody
    ResponseEntity order(@RequestBody @Valid ItemSearchDto itemSearchDto,
                         BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {

            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }
        String email = memberService.loadMemberEmail(principal,httpSession);
        Long cartItemId;
        Long itemId = (Long) httpSession.getAttribute("itemId");

        try {
            cartItemId = cartService.addCart(itemSearchDto, email,itemId);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }

    @GetMapping(value = "/cart")
    public String orderHist(Principal principal, Model model, ItemSearchDto itemSearchDto,HttpSession httpSession){
        Member member = memberService.findMember(httpSession,principal);
        List<CartItem> cartItemList = cartItemService.findByMemberId(member.getId());
        List<CartDetailDto> cartDetailDtoList = cartService.getCartList(principal,httpSession);


        List<MainItemDto> items = itemService.getMainItemPages(itemSearchDto);
        model.addAttribute("cartItem", cartItemList);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("cartItems", cartDetailDtoList);
        model.addAttribute("items", items);
        String name = memberService.loadMemberName(principal, httpSession);
        model.addAttribute("name", name);
        return "/cart/cartList";

    }
    @PatchMapping(value = "/cartItem/{itemId}")
    public @ResponseBody ResponseEntity updateCartItem(@PathVariable("itemId") Long itemId,Principal principal,int count) {
        System.out.println(itemId);
        if (count<= 0) {
            return new ResponseEntity<String>("최소 1개이상 담아주세요.", HttpStatus.BAD_REQUEST);
        } else if (!cartService.validateCartItem(itemId, principal,httpSession)) {
            return new ResponseEntity<String>("수정권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        cartService.updateCartItemCount(itemId, count);
        return new ResponseEntity<Long>(itemId, HttpStatus.OK);
    }
    @DeleteMapping(value = "/cartItem/{itemId}")
    public @ResponseBody ResponseEntity deleteCartItem(@PathVariable("itemId") Long itemId,
                                                       Principal principal){
        if (!cartService.validateCartItem(itemId, principal,httpSession)) {
            return new ResponseEntity<String>("수정권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        cartService.deleteCartItem(itemId);
        return new ResponseEntity<Long>(itemId, HttpStatus.OK);
    }
    @PostMapping(value = "/cart/orders")
    public @ResponseBody ResponseEntity orderCartItem(@RequestBody CartOrderDto cartOrderDto,
                                                      Principal principal){
        System.out.println(cartOrderDto.getCartItemId());
        List<CartOrderDto> cartOrderDtoList = cartOrderDto.getCartOrderDtoList();

        if(cartOrderDtoList == null || cartOrderDtoList.size() == 0){
            return new ResponseEntity<String>("주문할 상품을 선택해주세요.",HttpStatus.FORBIDDEN);
        }
        for(CartOrderDto cartOrder : cartOrderDtoList){
            if(!cartService.validateCartItem(cartOrder.getCartItemId(), principal,httpSession)){
                return new ResponseEntity<String>("주문 권한이 없습니다.",HttpStatus.FORBIDDEN);
            }
        }
        String email = memberService.loadMemberEmail(principal,httpSession);
        Long orderId;
        try {
            orderId = cartService.orderCartItem(cartOrderDtoList, email);
        }
        catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Long>(orderId,HttpStatus.OK);
    }
}
