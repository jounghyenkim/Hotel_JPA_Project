package com.shop.service;

import com.shop.constant.ReservationStatus;
import com.shop.dto.*;
import com.shop.entity.*;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import com.shop.repository.ReservationRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.thymeleaf.util.StringUtils;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;



    public List<Reservation> getmemberIdList(Principal principal,HttpSession httpSession) {

        String email = memberService.loadMemberEmail(principal,httpSession);
        Member member1 =memberRepository.findByEmail(email);
        System.out.println(member1.getId() + " member1의 아이디");

        return this.reservationRepository.findByMemberId(member1.getId());
    }


    public Reservation getReservationId(Long reservationId){
        return reservationRepository.findByReservationId(reservationId);
    }



    public List<Reservation> getAll()
    {
        return this.reservationRepository.findAll();
    }




    public void deletByeAll() {
        reservationRepository.deleteAll();
    }
    public Reservation reservationOk(Principal principal, HttpSession httpSession,ItemSearchDto itemSearchDto) throws Exception {
        String email = memberService.loadMemberEmail(principal,httpSession);
        Member member1 = memberRepository.findByEmail(email);

        Reservation reservation = Reservation.reservationRoom(member1,principal,httpSession,memberService,itemSearchDto);
        reservationRepository.save(reservation);
        return reservation;
    }



    public Long deleteByReservationId(@Param("reservationId")Long reservationId) {

        reservationRepository.deleteByReservationId(reservationId);

        return reservationId;
    }

    @Transactional(readOnly = true)
    public Page<Reservation> getAdminReservationPage(ReservationSearchDto reservationSearchDto, Pageable pageable){
        return reservationRepository.getAdminReservationPage(reservationSearchDto,pageable);
    }





}
