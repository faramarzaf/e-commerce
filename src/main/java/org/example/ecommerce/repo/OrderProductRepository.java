package org.example.ecommerce.repo;

import org.example.ecommerce.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {


    List<OrderProduct> findByOrder_Id(Long orderId);
}
