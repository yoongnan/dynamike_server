package com.dynamike.pos.model.entities.repo;


import java.util.List;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.http.server.PathContainer.PathSegment;
import org.springframework.stereotype.Repository;

import com.dynamike.pos.model.entities.Payment;
import com.dynamike.pos.model.entities.Purchase;;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>, PagingAndSortingRepository<Payment, Long> {
    
    @Query("SELECT v From Payment v")
    List<Payment> getPayments();
    
    @Query("SELECT v.year From Payment v group by v.year order by v.year desc")
    List<Integer> getTransactionYears();
    
    @Query("SELECT v From Payment v  where v.orderId=:invoiceNo  order by v.date")
    Payment getPaymentsByInvoiceNo(@Param("invoiceNo") String id);
    
    
    @Query("SELECT v From Payment v  where (:types is null or v.provider.id in (:types)) and v.year =:years and (:month is null or v.month =:month) order by v.date desc")
    List<Payment> getPaymentsByYearMonth(@Param("years") Integer year,@Param("month") Integer value, @Param("types") List<Integer> types);
	
	@Query("SELECT v From Payment v  where v.year =:years and v.month =:month  order by v.date desc")
    List<Payment> getPaymentsByYearMonth(@Param("years") Integer year,@Param("month") Integer value);
	
	@Query("SELECT v From Payment v  where v.year =:years  order by v.date desc")
    List<Payment> getPaymentsByYear(@Param("years") Integer years);
	
	
    @Query("SELECT v.month, v.year, COALESCE(sum(v.paymentDue), 0) From Payment as v  where v.year in (:years) group by v.month, v.year")
    List<Object[]> getMonthlyReport(@Param("years") List<Integer> years);
    
    @Query("SELECT COALESCE(sum(v.paymentCredit), 0) From Payment as v  where v.year =:years and v.month =:month and v.status!='0'")
    Float getReportByMonth(@Param("years") Integer years, @Param("month") Integer value);
    
    @Query("SELECT COALESCE(sum(v.paymentDue), 0) From Payment as v  where v.year =:years and v.month =:month and v.status!='0'")
    Float getReportDueByMonth(@Param("years") Integer years, @Param("month") Integer value);

    @Query("SELECT v.month,v.year,v.provider, count(v)  From Payment as v  where v.year in (:years) and (:provider is null or v.provider.id = :provider) group by v.month, v.year, v.provider")
    List<Object[]> getMonthlyOrderReport(@Param("years") List<Integer> years, @Param("provider")Integer provider);

    @Query("SELECT count(v.id)  From Payment as v  where v.year =:years and v.month =:month  and (:provider is null or v.provider.id = :provider)")
    Integer getOrderReportbyMonth(@Param("years") Integer years, @Param("month") Integer value, @Param("provider")Integer provider);
    
    @Query("SELECT COALESCE(sum(v.paymentDue), 0)  From Payment as v  where v.year in (:years)")
    Float getLatestSalesbyYear(@Param("years") List<Integer> years);
    
    @Query("SELECT substring(v.date,1,10), provider.id, count(v.id), COALESCE(sum(v.paymentDue), 0)  From Payment as v  where v.year =:years and v.month =:month  and (:provider is null or v.provider.id = :provider) group by substring(v.date,1,10), provider.id")
    List<Object[]> getOrderSummaryDaily(@Param("years") Integer years, @Param("month") Integer value, @Param("provider")Integer provider);
    
    @Query(value = "SELECT date,CONCAT(year,'/',month, '/', WEEK(date)) AS week_name, year, WEEK(date), COUNT(id), COALESCE(sum(payment_due), 0), provider FROM pos.payments where year =:years  and (:provider is null or provider = :provider) GROUP BY week_name ORDER BY year ASC, WEEK(date) ASC", 
    		  nativeQuery = true)
    List<Object[]> getOrderSummaryByWeek(@Param("years") Integer years, @Param("provider")Integer provider);
    
    @Query(value = " select sum(total_price) as cost from pos.order_items  as oi join pos.inventory as i on oi.item_id = i.code where invoice_id in( select order_id from pos.payments where  year = :year and month = :month and (:provider is null or provider = :provider))  ", 
  		  nativeQuery = true)
    Float getCOGS(@Param("year") Integer years, @Param("month") Integer month, @Param("provider")Integer provider);

    @Query(value = " select sum(payment_credit) as credit, sum(payment_fees) as fees, sum(commission_fees) as commission, sum(shipping_fees) as shipping, sum(other_fees) as other, sum(payment_due) as due  from pos.payments  where year = :year and month = :month and (:provider is null or provider = :provider)  ", 
    		  nativeQuery = true)
    List<Object[]> getPaymentBuckets(@Param("year") Integer years, @Param("month") Integer month, @Param("provider")Integer provider);
}
