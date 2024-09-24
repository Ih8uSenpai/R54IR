package com.example.practice5_6_7.jpa;

import com.example.practice5_6_7.entities.Telephone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelephoneRepository extends JpaRepository<Telephone, Long> {}