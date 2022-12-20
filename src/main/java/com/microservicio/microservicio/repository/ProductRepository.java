package com.microservicio.microservicio.repository;

import com.microservicio.microservicio.entity.Category;
import com.microservicio.microservicio.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository <Product, Long>{

    public List<Product> findByCategory(Category category);
}

