package com.microservicio.microservicio.service;

import com.microservicio.microservicio.entity.Category;
import com.microservicio.microservicio.entity.Product;
import com.microservicio.microservicio.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor//Para poder pasar el ProductRepository por constructor
public class ProductServiceImp implements ProductService{

    //@Autowired //@Autowired para hacer la inyección de dependencias.
    private final ProductRepository productRepository; //Inyección de dependencia

    @Override
    public List<Product> listAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public Product getProduct(Long id) {
        return productRepository.findById(id).orElseThrow(null);
    }

    @Override
    public Product createProduct(Product product) {
        product.setStatus("CREATED");
        product.setCreateAt(new Date());
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Product product) {
        //Primero verficamos que le producto existe
        Product productDB = getProduct(product.getId());
        if(null == productDB){
            return null;
        }
        //Ahora actualizamos el prodcto de la Base de Datos
        productDB.setName(product.getName());
        productDB.setDescription(product.getDescription());
        product.setCategory(product.getCategory());
        product.setPrice(product.getPrice());
        return productRepository.save(productDB);
    }

    @Override
    public Product deleteProduct(Long id) {
        //primero verificamos que el producto existe
        Product productDB = getProduct(id);
        if(null == productDB){
            return null;
        }
        productDB.setStatus("DELETED");
        return productRepository.save(productDB);

    }

    @Override
    public List<Product> findByCategory(Category category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public Product updateStock(Long id, Double quantity) {
        //Primero verificamos si existe el producto
        Product productDB = getProduct(id);
        if(productDB==null){
            return null;
        }
        //Cuando ya tenemos el producto, ahora sí actualizamos:
        Double stock = productDB.getStock() + quantity;
        productDB.setStock(stock);
        return productRepository.save(productDB);
    }


}
