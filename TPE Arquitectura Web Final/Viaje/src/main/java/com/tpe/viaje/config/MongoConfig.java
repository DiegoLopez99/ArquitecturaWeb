package com.tpe.viaje.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

public class MongoConfig extends AbstractMongoClientConfiguration {
    @Override
    protected String getDatabaseName() {
        return "microservicioViajeDb";
    }

    @Override
    public MongoClient mongoClient() {
        return MongoClients.create();
    }
}
