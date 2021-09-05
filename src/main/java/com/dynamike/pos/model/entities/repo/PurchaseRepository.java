package com.dynamike.pos.model.entities.repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dynamike.pos.model.entities.Purchase;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long>, PagingAndSortingRepository<Purchase, Long> {
    
	@Query("SELECT v.year From Purchase v group by v.year order by v.year desc")
    List<Integer> getAccountYears();
	
	@Query("SELECT v From Purchase v  where v.id =:id")
    Purchase getPurchasesById(@Param("id") String id);
	
	@Query("SELECT v From Purchase v  where v.type.id in (:types) and v.year =:years and (:month is null or v.month =:month) order by v.date desc")
    List<Purchase> getPurchasesByYearMonth(@Param("years") Integer year,@Param("month") Integer value, @Param("types") List<Integer> types);
	
	@Query("SELECT v From Purchase v  where v.type.id not in (1,2,3,4,5,6,16,17,18,19,20,21) and v.year =:years and (:month is null or v.month =:month) order by v.date desc")
    List<Purchase> getAccountByYearMonth(@Param("years") Integer year,@Param("month") Integer value);
	
	@Query("SELECT v From Purchase v  where v.type.id in (17,18,19,20,21) order by v.date desc")
    List<Purchase> getCashFlow();
	
	@Query("SELECT v From Purchase v  where v.type.id in (1,2,3,4,5,6,16) and v.year =:years and v.month =:month  order by v.date desc")
    List<Purchase> getPurchasesByYearMonth(@Param("years") Integer year,@Param("month") Integer value);
	
	
	
	@Query("SELECT v From Purchase v  where v.type.id in (1,2,3,4,5,6,16)  and v.year =:years  order by v.date desc")
    List<Purchase> getPurchasesByYear(@Param("years") Integer years);
	
	@Query("SELECT v From Purchase v  where v.type.id in (1,2,3,4,5,6,16) and v.month =:month")
    List<Purchase> getPurchasesByMonth(@Param("month") Integer value);
	
	@Query("SELECT v From Purchase v  where v.type.id in (:types)")
    List<Purchase> getPurchasesByType(@Param("types") List<Integer> types);
	
    @Query("SELECT v From Purchase v  where v.type.id in (1,2,3,4,5,6,16)")
    List<Purchase> getPurchases();
    
    @Query("SELECT v From Purchase v  where v.type.id not in (1,2,3,4,5,6,16)")
    List<Purchase> getAccount();

    @Query("select COALESCE(sum(v.totalAmount), 0) from Purchase as v where v.year =:years and v.month =:month and v.type.id in (1,2,3,4,5,6,16)")
    Float getReportbyMonth(@Param("years") Integer years, @Param("month") Integer value);
    
    @Query("select v.month, v.year, COALESCE(sum(v.totalAmount), 0) from Purchase as v where v.year in (:years) group by v.month,v.year")
    List<Object []> getMonthlyReport(@Param("years") List<Integer> years);
    
    @Query("select COALESCE(sum(v.totalAmount), 0) from Purchase as v where v.year in (:years) and v.type.id=1")
    Float getYearlyStockCost(@Param("years") List<Integer> years);
    
    @Query("select COALESCE(sum(v.totalAmount), 0) from Purchase as v where v.year in (:years) and v.type.id=2")
    Float getYearlyPackageValue(@Param("years") List<Integer> years);
    
    @Query("select COALESCE(sum(v.totalAmount), 0) from Purchase as v where v.year in (:years) and v.type.id=3")
    Float getYearlyAdvertiseValue(@Param("years") List<Integer> years);
    
    @Query("select COALESCE(sum(v.totalAmount), 0) from Purchase as v where v.year in (:years) and v.type.id=4")
    Float getYearlyEquipmentValue(@Param("years") List<Integer> years);
    
    @Query("select COALESCE(sum(v.totalAmount), 0) from Purchase as v where v.year in (:years) and v.type.id=5")
    Float getYearlyRefundValue(@Param("years") List<Integer> years);
    
    @Query("select COALESCE(sum(v.totalAmount), 0) from Purchase as v where v.year in (:years) and v.type.id=6")
    Float getYearlyLostValue(@Param("years") List<Integer> years);
    
    @Query("select COALESCE(sum(v.totalAmount), 0) from Purchase as v where v.year in (:years) and v.type.id=7")
    Float getYearlySubsidyValue(@Param("years") List<Integer> years);

    @Query("select COALESCE(sum(v.totalAmount), 0) from Purchase as v where v.type.id=8")
    Float getOpenBankAccountValue(@Param("years") List<Integer> years);    

    @Query("select COALESCE(sum(v.totalAmount), 0) from Purchase as v where v.year in (:years) and v.type.id=9")
    Float getYearlyInvestmentValue(@Param("years") List<Integer> years);
    
    @Query("select COALESCE(sum(v.totalAmount), 0) from Purchase as v where v.year =:years and v.month =:month and v.type.id=9")
    Float getInvestmentValueByMonth(@Param("years") Integer years , @Param("month") Integer value);
    
    @Query("select COALESCE(sum(v.totalAmount), 0) from Purchase as v where v.year in (:years) and v.type.id=10")
    Float getYearlyDividendPaidValue(@Param("years") List<Integer> years);    

    @Query("select COALESCE(sum(v.totalAmount), 0) from Purchase as v where v.year in (:years) and v.type.id=11")
    Float getYearlyRedrawValue(@Param("years") List<Integer> years);
    
    @Query("select COALESCE(sum(v.totalAmount), 0) from Purchase as v where v.year in (:years) and v.type.id=12")
    Float getYearlyEarnedValue(@Param("years") List<Integer> years);

    @Query("select COALESCE(sum(v.totalAmount), 0) from Purchase as v where v.year in (:years) and v.type.id=13")
    Float getInventoryValue(@Param("years") List<Integer> years);
    
    @Query("select COALESCE(sum(v.totalAmount), 0) from Purchase as v where v.year in (:years) and v.month =:month and v.type.id=13")
    Float getInventoryValue(@Param("years") Integer years,@Param("month")  Integer month);
    
    @Query("select COALESCE(sum(v.totalAmount), 0) from Purchase as v where v.year in (:years) and v.month =:month and v.type.id=14")
    Float getInventoryCashValue(@Param("years") Integer years,@Param("month")  Integer month);
    
    @Query("select COALESCE(sum(v.totalAmount), 0) from Purchase as v where v.year in (:years) and v.type.id=14")
    Float getInventoryCashValue(@Param("years") List<Integer> years);

    @Query("select COALESCE(sum(v.totalAmount), 0) from Purchase as v where v.year in (:years) and v.type.id=15")
    Float getCashValue(@Param("years") List<Integer> years);
    
    @Query("select COALESCE(sum(v.totalAmount), 0) from Purchase as v where v.year in (:years) and v.type.id in (1,2,3,4,5,6,16)")
    Float getExpenditureByYear(@Param("years") List<Integer> years);
    
    @Query("select COALESCE(v.totalAmount, 0) from Purchase as v where v.type.id in (17) order by date desc ")
    Float getBankMoney();
    @Query("select COALESCE(v.totalAmount, 0) from Purchase as v where v.type.id in (18) order by date desc ")
    Float getShopCash();
    @Query("select COALESCE(v.totalAmount, 0) from Purchase as v where v.type.id in (19) order by date desc ")
    Float getShopeeWallet();
    @Query("select COALESCE(v.totalAmount, 0) from Purchase as v where v.type.id in (20) order by date desc ")
    Float getLazaWallet();
    @Query("select COALESCE(v.totalAmount, 0) from Purchase as v where v.type.id in (21) order by date desc ")
    Float getWallet();
    
    
    @Query(value = "select type,sum(total_amount) from pos.purchases where type in (1,2,3,5,6,7) and month = :month and year = :years group by type", 
  		  nativeQuery = true)
    List<Object[]> getExpenditure(@Param("years") Integer years,@Param("month") Integer month);
    
    @Query("select COALESCE(sum(v.totalAmount), 0) from Purchase as v where v.year in (:years) and v.type.id in (2,3,5,6,7) and v.month in (:month)")
    Float getTotalExpenditure(@Param("years") Integer years,@Param("month") Integer month);
    
}
