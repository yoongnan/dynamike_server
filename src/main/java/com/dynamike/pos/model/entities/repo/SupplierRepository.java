package com.dynamike.pos.model.entities.repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.dynamike.pos.model.entities.Supplier;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long>, PagingAndSortingRepository<Supplier, Long> {
//    List<Inventory> findByAuthid(String authid);
    
    @Query("SELECT v From Supplier v")
    List<Supplier> getSuppliers();
}
