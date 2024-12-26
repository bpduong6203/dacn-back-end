package com.example.DoAnCN.Repository;

import com.example.DoAnCN.Entity.Bookings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingsRepository extends JpaRepository<Bookings, Long> {
}
