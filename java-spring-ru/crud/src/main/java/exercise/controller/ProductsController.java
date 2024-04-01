package exercise.controller;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import exercise.dto.ProductCreateDTO;
import exercise.dto.ProductDTO;
import exercise.dto.ProductUpdateDTO;
import exercise.mapper.JsonNullableMapper;
import exercise.mapper.ProductMapper;
import exercise.repository.CategoryRepository;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import exercise.exception.ResourceNotFoundException;
import exercise.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/products")
public class ProductsController {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private JsonNullableMapper jsonNullableMapper;

    // BEGIN
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path="")
    public List<ProductDTO> show(@RequestParam (defaultValue = "10") Long limit){
        return productRepository.findAll().stream().limit(limit).map(p -> productMapper.map(p)).toList();
    }

    @GetMapping(path="/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO index(@PathVariable Long id){
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));
        return productMapper.map(product);
    }

    @PostMapping(path="")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO create(@RequestBody ProductCreateDTO dto){
        var category  = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "entity not found"
        ));
        var newProduct = productMapper.map(dto);

        category.addProduct(newProduct);
        categoryRepository.save(category);

        productRepository.save(newProduct);

        return productMapper.map(newProduct);
    }

    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO update(@PathVariable Long id, @RequestBody ProductUpdateDTO data){
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found: " + id));

        var category  = categoryRepository.findById(jsonNullableMapper.unwrap(data.getCategoryId()))
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + jsonNullableMapper.unwrap(data.getCategoryId()) + " not found"));
        category.removeProduct(product);
        category.addProduct(product);
        categoryRepository.save(category);


        productMapper.update(data, product);

        product.setCategory(category);

        productRepository.save(product);

        var productDTO = productMapper.map(product);

        return productDTO;
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        var deletedProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));

        productRepository.delete(deletedProduct);
    }
    // END
}
