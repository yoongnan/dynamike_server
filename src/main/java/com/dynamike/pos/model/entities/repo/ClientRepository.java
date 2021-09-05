package com.dynamike.pos.model.entities.repo;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dynamike.pos.model.entities.Client;
import com.dynamike.pos.model.entities.Payment;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>, PagingAndSortingRepository<Client, Long> {
//    List<Inventory> findByAuthid(String authid);
    
    @Query("SELECT v From Client v")
    List<Client> getClients();
    
    @Query("SELECT v From Client v")
    Page<Client> getClients(Pageable pageable);

}
