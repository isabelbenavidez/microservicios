package com.microservicio.microservicio.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservicio.microservicio.entity.Category;
import com.microservicio.microservicio.entity.Product;
import com.microservicio.microservicio.service.ProductService;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController//Inidca que vamos a implementar un servicio REST
@RequestMapping(value = "/products")//Mapear el recurso, y se le pasa el uri del recurso(Nombre del recurso en plural)
public class ProductController {

    @Autowired
    private ProductService productService;


    /*

    //Implementar método para que devuelva la lista de productos que está en la Base de datos
    @GetMapping
    public ResponseEntity<List<Product>>listProduct(){
        List<Product> products = productService.listAllProduct();
        if(products.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(products);
    }*/


    //Implementar método para que devuelva la lista de productos que está en la Base de datos, filtrado por categoría
    @GetMapping
    public ResponseEntity<List<Product>> listProduct(@RequestParam(name="categoryId", required = false) Long categoryId){
        //si no nos envían una categoría, entonces que se listen todos los productos:
        List<Product> products = new ArrayList<>();
        if(categoryId == null) {
            products = productService.listAllProduct();
            if(products.isEmpty()){
                return ResponseEntity.noContent().build();
            }
        }else {
            products = productService.findByCategory(Category.builder().id(categoryId).build());
            if(products.isEmpty()){
               return ResponseEntity.notFound().build();
           }
        }
        return ResponseEntity.ok(products);

    }

    //Recuperar un producto en específico

    @GetMapping(value = "/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") Long id){//El /{id} del @getMapping se  captura con el @PathVariable
        Product product = productService.getProduct(id);
        if(product==null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(product);
    }


    //Crear un producto nuevo
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product, BindingResult result){
        if(result.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(result));
        }
        Product productCreated = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productCreated);
    }



    //Actualizar un producto existente
    @PutMapping(value = "/{id}")//Se mapea con el id del producto que debemos atualizar
    public ResponseEntity<Product> updateProduct(@PathVariable("id") Long id, @RequestBody  Product product){//Recibimos el id del producto a actualizar y recibimos el producto a actualizar.
     //Se setea el ID del producto
     product.setId(id);
     Product productDB = productService.updateProduct(product);
     if(productDB==null){
         return ResponseEntity.notFound().build();
     }
     return ResponseEntity.ok(productDB);//Producto Actualizado
    }

    @DeleteMapping(value = "/{id}")
        public ResponseEntity<Product> deleteProduct(@PathVariable("id") Long id){
            Product productDelete = productService.deleteProduct(id);
            if(productDelete == null){
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(productDelete);
    }

    @GetMapping(value = "/{id}/stock")
        public ResponseEntity<Product> updaeStockProduct(@PathVariable Long id, @RequestParam(value = "quantity", required = true) Double quantity){
        Product product = productService.updateStock(id, quantity);
        if(product == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(product);
    }

    private String formatMessage(BindingResult result){
        List<Map<String, String>> errors = result.getFieldErrors().stream()
                .map(err -> {
                    Map<String, String> error = new HashMap<>();
                    error.put(err.getField(), err.getDefaultMessage());
                    return error;
                }).collect(Collectors.toList());

        ErrorMessage errorMessage = ErrorMessage.builder()
                .code("01")
                .messages(errors).build();
        ObjectMapper mapper = new ObjectMapper();
        String jsonString="";
        try {
            jsonString = mapper.writeValueAsString(errorMessage);
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return jsonString;
    }


}



