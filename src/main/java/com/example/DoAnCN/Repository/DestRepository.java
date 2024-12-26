package com.example.DoAnCN.Repository;

import com.example.DoAnCN.Entity.Destinations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DestRepository extends JpaRepository<Destinations, Long> {
    List<Destinations> findByCity_Province_Country(String country);
    List<Destinations> findByCity_Province_CountryNot(String country);
}
