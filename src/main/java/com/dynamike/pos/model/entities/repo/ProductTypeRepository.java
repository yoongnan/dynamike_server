package com.dynamike.pos.model.entities.repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.dynamike.pos.model.entities.ProductType;

@Repository
public interface ProductTypeRepository extends JpaRepository<ProductType, Long>, PagingAndSortingRepository<ProductType, Long> {
    
    @Query("SELECT v From ProductType v")
    List<ProductType> getProductType();

}
