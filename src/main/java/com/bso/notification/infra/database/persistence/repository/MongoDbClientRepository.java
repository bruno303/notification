package com.bso.notification.infra.database.persistence.repository;

import com.bso.notification.infra.database.persistence.entity.PersistenceClient;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoDbClientRepository extends ReactiveMongoRepository<PersistenceClient, String> {
}
