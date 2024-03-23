package exercise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import exercise.model.Product;
import exercise.repository.ProductRepository;
import exercise.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductRepository productRepository;

    // BEGIN
    @GetMapping(path="")
    public List<Product> index(@RequestParam(name ="min", required = false) Integer minPrice, @RequestParam(name = "max", required = false) Integer maxPrice){

        if(minPrice == null && maxPrice == null){
            return productRepository.findAll(Sort.by(Sort.Order.by("price")));
        } else if (minPrice !=null && maxPrice == null){
            return productRepository.findByPriceGreaterThanEqual(minPrice, Sort.by(Sort.Order.by("price")));
        } else if (minPrice == null && maxPrice != null){
            return productRepository.findByPriceLessThan(maxPrice, Sort.by(Sort.Order.by("price")));
        } else {
            return productRepository.findByPriceBetween(minPrice, maxPrice, Sort.by(Sort.Order.by("price")));
        }
    }
    // END

    @GetMapping(path = "/{id}")
    public Product show(@PathVariable long id) {

        var product =  productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));

        return product;
    }
}
