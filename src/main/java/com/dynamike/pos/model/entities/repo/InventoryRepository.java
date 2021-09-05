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
public interface InventoryRepository extends JpaRepository<Inventory, Long>, PagingAndSortingRepository<Inventory, Long> {
    
		
	@Query("SELECT v From ProductCheck v")
    List<ProductCheck> getStock();
	
    @Query("SELECT v From Product v")
    List<Product> getProducts();
    
    @Query("SELECT v From Product v WHERE (v.supplier.id = :id) ")
    List<Product> getListProductBySupplier(@Param("id") Integer id);
    
    @Query("SELECT v From Inventory v WHERE (v.code = :code or v.barcode = :code) order by stock desc ")
    List<Inventory> getListProductByCode(@Param("code") String code);
    
    @Query("SELECT v From Inventory v WHERE v.id = :code order by stock desc ")
    List<Inventory> getListProductById(@Param("code") String code);
    
    @Query("SELECT v From Inventory v WHERE v.code = :code ")
    Inventory getProductByCode(@Param("code") String code);
    
    @Query("SELECT v From Inventory v WHERE v.code = :code and v.supplier.id = :id")
    List<Inventory> getProductByCodeSupplierId(@Param("code") String code, @Param("id") Integer id);
    
    @Query("SELECT v From Inventory v WHERE v.id like :code and v.stock > 0")
    List<Inventory> getAllProductByCode(@Param("code") String code);
    
    @Query("SELECT COALESCE(sum(v.total_stock), 0) From Inventory v")
    Float getInventoryValue();
    
    @Query("SELECT v From Inventory v where v.type is null")
    Page<Inventory> getProductByOtherType(Pageable pageable);
    
    @Query("SELECT v From Inventory v where v.type =:type")
    Page<Inventory> getProductByType(@Param("type") String type,  Pageable pageable);
    

}
