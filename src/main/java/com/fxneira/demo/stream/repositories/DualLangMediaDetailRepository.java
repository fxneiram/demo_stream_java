package com.fxneira.demo.stream.repositories;

import com.fxneira.demo.stream.entities.DualLangMediaDetail;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DualLangMediaDetailRepository extends MongoRepository<DualLangMediaDetail, UUID> {
}
