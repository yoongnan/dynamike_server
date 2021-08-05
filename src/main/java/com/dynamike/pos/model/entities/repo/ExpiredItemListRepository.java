package com.dynamike.pos.model.entities.repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dynamike.pos.model.entities.ExpiredCheck;
import com.dynamike.pos.model.entities.PurchaseItemList;

@Repository
public interface ExpiredItemListRepository extends JpaRepository<PurchaseItemList, Long>, PagingAndSortingRepository<PurchaseItemList, Long> {
    
    @Query("SELECT v From PurchaseItemList v")
    List<PurchaseItemList> getPurchaseItemLists();

    @Query("SELECT v From PurchaseItemList v where v.purchaseId = :id")
    List<PurchaseItemList> getPurchaseItemListsByPurchaseId(@Param("id") String id);
    
    @Query("SELECT v From ExpiredCheck v where v.purchase.type.id = 1 order by v.expired desc, v.purchase.date desc")
    List<ExpiredCheck> getPurchaseItemListsByExpired();
}
