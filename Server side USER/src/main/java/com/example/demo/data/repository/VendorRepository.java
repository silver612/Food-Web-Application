package com.example.demo.data.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.data.models.Vendor;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Integer>{
    
    @Query(value = "SELECT * FROM vendor WHERE name = ?1 LIMIT 1", nativeQuery = true)
    public Optional<Vendor> findByName(String name);

    @Query(value = "SELECT * FROM vendor v WHERE v.lat is not null AND v.lng is not null AND ABS(v.lat - ?1) <= 5/110.574 AND ABS(v.lng - ?2) <= 5 / (111.320 * COS(?1 * 0.0174533))", nativeQuery = true)
    public List<Vendor> findByCoord(Float lat, Float lng);

}
