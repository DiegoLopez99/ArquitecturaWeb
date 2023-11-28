package com.tpe.viaje.repository;

import com.tpe.viaje.Entity.Viaje;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
public interface ViajeRepositoryMongodb extends MongoRepository<Viaje, String>, ViajeRepositoryCustom {

    List<Viaje> findAll(Sort sort);

}
