package com.ClothStore.repository;

import org.springframework.data.repository.CrudRepository;

import com.ClothStore.domain.UserPayment;

public interface UserPaymentRepository extends CrudRepository<UserPayment, Long>{

}