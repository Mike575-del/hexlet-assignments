package exercise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

import exercise.repository.ProductRepository;
import exercise.dto.ProductDTO;
import exercise.dto.ProductCreateDTO;
import exercise.dto.ProductUpdateDTO;
import exercise.exception.ResourceNotFoundException;
import exercise.mapper.ProductMapper;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    // BEGIN
    @GetMapping(path="")
    public List<ProductDTO> show(@RequestParam (defaultValue = "10") long limit){

        return productRepository.findAll().stream().limit(limit).map(i  -> productMapper.map(i)).toList();

    }

    @GetMapping(path="/{id}")
    public ProductDTO index(@PathVariable long id){
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));
        return productMapper.map(product);
    }

    @PostMapping(path="")
    public ResponseEntity<ProductDTO> create(@RequestBody ProductCreateDTO productCreateDTO){
        var product = productMapper.map(productCreateDTO);
        productRepository.save(product);

        return ResponseEntity.status(HttpStatus.CREATED).body(productMapper.map(product));
    }

    @PutMapping(path="/{id}")
    public ProductDTO update(@PathVariable long id, @RequestBody ProductUpdateDTO data){
        var updatedProduct = productRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));

        productMapper.update(data, updatedProduct);

        productRepository.save(updatedProduct);

        return productMapper.map(updatedProduct);
    }
    // END
}
