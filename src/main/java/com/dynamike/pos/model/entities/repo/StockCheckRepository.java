package com.dynamike.pos.model.entities.repo;


import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dynamike.pos.model.entities.StockCheck;

@Repository
public interface StockCheckRepository extends JpaRepository<StockCheck, Long>, PagingAndSortingRepository<StockCheck, Long> {
    
	@Query("SELECT v From StockCheck v where v.status = :status and v.date = :date order by v.productId")
    List<StockCheck> getConflictStock(@Param("status") String status, @Param("date") String date);
	
    @Query("SELECT v From StockCheck v")
    List<StockCheck> getStockCheck();
    
    @Query("SELECT v From StockCheck v where v.productId = :id order by v.date desc")
    List<StockCheck> getStockCheck(@Param("id") String id);

}
