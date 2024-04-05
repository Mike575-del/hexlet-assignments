package exercise.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import exercise.dto.ProductParamsDTO;
import exercise.model.Product;

// BEGIN
@Component

public class ProductSpecification{

    public Specification<Product> build(ProductParamsDTO params){
        return withCategoryId(params.getCategoryId())
                .and(withPriceGt(params.getPriceGt()))
                .and(withPriceLt(params.getPriceLt()))
                .and(withRatingGt(params.getRatingGt()))
                .and(withNameMatch(params.getTitleCont()));
    }

    private Specification<Product> withCategoryId(Long categoryId){
        return ((root, query, criteriaBuilder) -> categoryId == null ? criteriaBuilder.conjunction()
                : criteriaBuilder.equal(root.get("category").get("id"), categoryId));
    }

    private Specification<Product> withPriceGt(Integer price){
        return ((root, query, criteriaBuilder) -> price == null ? criteriaBuilder.conjunction()
                : criteriaBuilder.greaterThan(root.get("price"), price));
    }

    private Specification<Product> withPriceLt(Integer price){
        return ((root, query, criteriaBuilder) -> price == null ? criteriaBuilder.conjunction()
                : criteriaBuilder.lessThan(root.get("price"), price));
    }

    private Specification<Product> withRatingGt(Double ratingGt){
        return ((root, query, criteriaBuilder) -> ratingGt == null ? criteriaBuilder.conjunction()
                : criteriaBuilder.greaterThan(root.get("rating"), ratingGt));
    }

    private Specification<Product> withNameMatch(String productNameSubstring){
        return ((root, query, criteriaBuilder) -> productNameSubstring == null ? criteriaBuilder.conjunction()
                : criteriaBuilder.like(root.get("title"), productNameSubstring));
    }
}
// END
