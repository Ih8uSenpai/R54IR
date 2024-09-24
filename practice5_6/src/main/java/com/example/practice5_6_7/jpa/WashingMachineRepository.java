package com.example.practice5_6_7.jpa;

import com.example.practice5_6_7.entities.WashingMachine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WashingMachineRepository extends JpaRepository<WashingMachine, Long> {}