package com.dynamike.pos.model.entities.repo;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dynamike.pos.model.entities.Item;
import com.dynamike.pos.model.entities.ProductItem;
import com.dynamike.pos.model.entities.SimpleItem;

@Repository
public interface SimpleItemRepository extends JpaRepository<SimpleItem, Long>, PagingAndSortingRepository<SimpleItem, Long> {
//    List<Inventory> findByAuthid(String authid);
    
    @Query("SELECT v From ProductItem v")
    List<ProductItem> getItems();
    
//    @Query("SELECT count(v) From Item v where v.product.type =:type")
//    Integer getItemCountByType(@Param("type") String type);
//
//    @Query("SELECT v From Item v where v.product.code =:id")
//    List<Item> getItemProductsById(@Param("id") String id);
    
}