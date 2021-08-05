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
    
    
    @Query("SELECT v From StockCheck as v  where v.year =:year and v.month =:month group by substring(v.date,1,10)")
    List<StockCheck> getStockCheckDate(@Param("year") Integer year,@Param("month") Integer month);
    
    @Query(value = " select *  from pos.stock_check  where date like :date  order by product_id", 
	  nativeQuery = true)
    List<StockCheck> getStockCheckByDate(@Param("date") String date);

}
