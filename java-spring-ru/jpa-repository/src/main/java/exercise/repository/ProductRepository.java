package exercise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import exercise.model.Product;

import org.springframework.data.domain.Sort;

import javax.sound.sampled.Port;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // BEGIN
    public List<Product> findByPriceBetween(int minPrice, int maxPrice, Sort sort);

    public List<Product> findByPriceLessThan(int maxPrice, Sort sort);

    public List<Product> findByPriceGreaterThanEqual(int minPrice, Sort sort);
    // END
}
