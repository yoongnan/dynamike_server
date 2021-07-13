package com.dynamike.pos.model.entities.repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.dynamike.pos.model.entities.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>, PagingAndSortingRepository<Client, Long> {
//    List<Inventory> findByAuthid(String authid);
    
    @Query("SELECT v From Client v")
    List<Client> getClients();

//    @Query("SELECT v From VMwareProductProp v WHERE v.user_id = :userid ")
//    List<Inventory> findByUserId(@Param("userid") Long userid);
}
