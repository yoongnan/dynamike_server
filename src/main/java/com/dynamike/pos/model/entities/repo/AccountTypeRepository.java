package com.dynamike.pos.model.entities.repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.dynamike.pos.model.entities.AccountType;

@Repository
public interface AccountTypeRepository extends JpaRepository<AccountType, Long>, PagingAndSortingRepository<AccountType, Long> {
    
    @Query("SELECT v From AccountType v")
    List<AccountType> getProducts();

}
