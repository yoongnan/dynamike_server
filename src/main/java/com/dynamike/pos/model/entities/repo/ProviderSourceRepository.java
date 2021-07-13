package com.dynamike.pos.model.entities.repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.dynamike.pos.model.entities.Client;
import com.dynamike.pos.model.entities.ProviderSource;

@Repository
public interface ProviderSourceRepository extends JpaRepository<ProviderSource, Long>, PagingAndSortingRepository<ProviderSource, Long> {

    
    @Query("SELECT v From ProviderSource v")
    List<ProviderSource> getProviderSources();


}
