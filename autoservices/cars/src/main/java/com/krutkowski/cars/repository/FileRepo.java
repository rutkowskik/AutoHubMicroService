package com.krutkowski.cars.repository;

import com.krutkowski.cars.model.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FileRepo extends JpaRepository<File, Long> {

}
