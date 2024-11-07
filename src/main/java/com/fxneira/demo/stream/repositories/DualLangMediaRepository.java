package com.fxneira.demo.stream.repositories;

import com.fxneira.demo.stream.entities.DualLangMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DualLangMediaRepository extends JpaRepository<DualLangMedia, UUID> {
}