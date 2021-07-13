package com.dynamike.pos.model.entities.repo;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dynamike.pos.model.entities.Inventory;
import com.dynamike.pos.model.entities.Item;
import com.dynamike.pos.model.entities.Product;
import com.dynamike.pos.model.entities.ProductCheck;

@Repository
public interface ProuctCheckRepository extends JpaRepository<ProductCheck, Long>, PagingAndSortingRepository<ProductCheck, Long> {
    
		
	@Query("SELECT v From ProductCheck v")
    List<ProductCheck> getStock();
	
  

}
