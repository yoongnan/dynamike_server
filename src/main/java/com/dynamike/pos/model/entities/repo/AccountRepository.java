package com.dynamike.pos.model.entities.repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dynamike.pos.model.entities.Account;
import com.dynamike.pos.model.entities.Purchase;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>, PagingAndSortingRepository<Account, Long> {
    
    @Query("SELECT v From Account v")
    List<Account> getPurchases();
    
    @Query("select v from Account as v where v.type.id =8 ")
    List<Account> getOpeningAccount(@Param("years") List<Integer> years);
    
    @Query("select v.month, v.year, sum(v.totalAmount) from Account as v where v.year in (:years) group by v.month,v.year")
    List<Object []> getMonthlyReport(@Param("years") List<Integer> years);
}
