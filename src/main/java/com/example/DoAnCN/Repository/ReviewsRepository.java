package com.example.DoAnCN.Repository;

import com.example.DoAnCN.Entity.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewsRepository extends JpaRepository<Reviews, Long> {
    List<Reviews> findByDestinationId(Long destinationId);
}
