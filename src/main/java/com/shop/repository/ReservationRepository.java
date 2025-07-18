package com.shop.repository;

import com.shop.entity.Item;
import com.shop.entity.Reservation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation,Long>, QuerydslPredicateExecutor<Reservation>, ReservationCustom {

    @Query(value = "SELECT * FROM reservation", nativeQuery = true)
    List<Reservation> findAll();



    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "delete FROM reservation where reservation_id=?", nativeQuery = true)
    void deleteByReservationId(@Param("reservation_id") Long id);

    @Query(value = "SELECT * FROM reservation where reservation_id=?",nativeQuery = true)
    Reservation findByReservationId(@Param("reservationId")Long reservationId);


    List<Reservation> findByMemberId(Long memberId);

}
