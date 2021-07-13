package com.dynamike.pos.model.entities.repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dynamike.pos.model.entities.OrderList;
import com.dynamike.pos.model.entities.PurchaseItemList;

@Repository
public interface OrderListRepository extends JpaRepository<OrderList, Long>, PagingAndSortingRepository<OrderList, Long> {
    
    @Query("SELECT v From OrderList v")
    List<OrderList> getOrderLists();

    @Query("SELECT v From OrderList v where v.invoiceId = :invoiceId")
    List<OrderList> getOrderItemListsById(@Param("invoiceId") String invoiceId);
    
    @Query("SELECT v From OrderList v where v.invoiceId = :invoiceId and v.itemId= :itemId")
    List<OrderList> getOrderItemListsBy2Id(@Param("invoiceId") String invoiceId, @Param("itemId") String itemId);
}
