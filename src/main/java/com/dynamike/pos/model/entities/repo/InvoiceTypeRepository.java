package com.dynamike.pos.model.entities.repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dynamike.pos.model.entities.Client;
import com.dynamike.pos.model.entities.InvoiceType;

@Repository
public interface InvoiceTypeRepository extends JpaRepository<InvoiceType, Long>, PagingAndSortingRepository<InvoiceType, Long> {
    
    @Query("SELECT v From InvoiceType v where v.id in (:types)")
    List<InvoiceType> getInvoiceTypes(@Param("types") List<Integer> types);

    @Query("SELECT v From InvoiceType v where v.id not in (:types)")
    List<InvoiceType> getOtherInvoiceTypes(@Param("types") List<Integer> types);
    
    @Query("SELECT v From InvoiceType v ")
    List<InvoiceType> getAllInvoiceTypes();
}
