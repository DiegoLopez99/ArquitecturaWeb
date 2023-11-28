package com.tpe.viaje.repository;

import com.tpe.viaje.Entity.Pausa;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PausaRepositoryMongodb extends MongoRepository<Pausa, String>, PausaRepositoryCustom {
}
