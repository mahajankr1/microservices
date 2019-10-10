package com.kr.profile.service;

import java.util.List;

import com.kr.models.commons.model.Customer;

public interface CustomerService {


    Customer save(Customer customer);

    Customer fetchById(int profileId);

    List<Customer> fetchAllProfiles();
}
