package com.kr.profile.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.kr.models.commons.model.Customer;

public interface CustomerRepository  extends JpaRepository<Customer,Integer> {
}
