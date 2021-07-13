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

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>, PagingAndSortingRepository<Item, Long> {
//    List<Inventory> findByAuthid(String authid);
    
    @Query("SELECT v From ProductItem v")
    List<ProductItem> getItems();
    
    @Query("SELECT count(v) From Item v where v.product.type =:type")
    Integer getItemCountByType(@Param("type") String type);
//    
//    @Query("SELECT v From Item v where v.product.type =:type  order by v.product.Id")
//    List<Item> getItemsByType(@Param("type") String type);
    
    @Query("SELECT v From Item v where v.product.type is null")
    Page<Item> getItemsByOtherType(Pageable pageable);
    
    @Query("SELECT v From Item v where v.product.type =:type")
    Page<Item> getItemsByType(@Param("type") String type,  Pageable pageable);
//    @Query(value="SELECT * From pos.Items as v join pos.inventory as m on v.product_id = m.id where m.product_type =:type  order by v.product_Id limit :size offset :page ", nativeQuery = true)
//    List<Item> getItemsByType(@Param("type") String type, @Param("page") Integer page, @Param("size") Integer size);

    @Query("SELECT v From Item v where v.product.code =:id")
    List<Item> getItemProductsById(@Param("id") String id);
    
}