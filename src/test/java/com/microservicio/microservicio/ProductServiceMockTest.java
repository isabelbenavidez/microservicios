package com.microservicio.microservicio;

import com.microservicio.microservicio.entity.Category;
import com.microservicio.microservicio.entity.Product;
import com.microservicio.microservicio.repository.ProductRepository;
import com.microservicio.microservicio.service.ProductService;
import com.microservicio.microservicio.service.ProductServiceImp;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;

@SpringBootTest
public class ProductServiceMockTest {

    //Para este caso vamos a trabajar con datos MOCKEADOS con Mockito

    @Mock
    private ProductRepository productRepository;

    private ProductService productService;

    @BeforeEach//Para realizar antes de que se ejecute el test.
    public void setup(){

        MockitoAnnotations.openMocks(this);
        productService = new ProductServiceImp(productRepository);
        Product computer = Product.builder()
                .id(1L)
                .name("computer")
                .category(Category.builder().id(1L).build())
                .price(Double.parseDouble("12.5"))
                .stock(Double.parseDouble("5"))
                .build();
        Mockito.when(productRepository.findById(1L))
                .thenReturn(Optional.of(computer));

        Mockito.when(productRepository.save(computer)).thenReturn(computer);

    }

    @Test
    public void whenValidGetID_ThenReturnProduct(){
        Product found = productService.getProduct(1L);
          Assertions.assertThat(found.getName()).isEqualTo("computer");
    }


    @Test
    public void whenValidUpdateStock_ThenReturnNewStock(){
        Product newStock = productService.updateStock(1L, Double.parseDouble("8"));
        Assertions.assertThat(newStock.getStock()).isEqualTo(13);

    }




}
