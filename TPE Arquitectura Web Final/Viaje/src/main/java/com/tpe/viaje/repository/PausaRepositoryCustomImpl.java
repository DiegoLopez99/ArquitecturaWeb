package com.tpe.viaje.repository;

import com.tpe.viaje.Entity.Pausa;
import com.tpe.viaje.Entity.Viaje;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

public class PausaRepositoryCustomImpl implements PausaRepositoryCustom{

    private final MongoTemplate mongoTemplate;

    public PausaRepositoryCustomImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Pausa> findAllByViaje(Viaje viaje) {
        return mongoTemplate.find(query(where("id_viaje").is(viaje.getId())), Pausa.class);
    }

    @Override
    public List<Pausa> findByMonopatinId(Long id) {
        // Paso 1: Obtener los IDs de Viaje asociados al monopatín
        Aggregation aggregation = newAggregation(
                match(Criteria.where("id_monopatin").is(id)),
                project("id")
        );

        AggregationResults<Document> viajeResults = mongoTemplate.aggregate(aggregation, "viaje", Document.class);
        List<String> viajeIds = viajeResults.getMappedResults().stream()
                .map(document -> document.getString("_id"))
                .collect(Collectors.toList());

        // Paso 2: Obtener las pausas asociadas a los IDs de Viaje obtenidos
        return mongoTemplate.find(Query.query(Criteria.where("id_viaje").in(viajeIds)), Pausa.class);
    }

    @Override
    public List<List<Integer>> findUsoByMonopatinId() {
        // Consulta para obtener id_viaje agrupados por id_monopatin desde la entidad Viaje
        TypedAggregation<Viaje> viajeAggregation = (TypedAggregation<Viaje>) Aggregation.newAggregation(
                group("id_monopatin")
                        .addToSet("id").as("viajes")
        );

        AggregationResults<Document> viajeResult = mongoTemplate.aggregate(viajeAggregation, "viaje", Document.class);
        List<Document> viajeDocuments = viajeResult.getMappedResults();

        // Lista para almacenar los resultados finales
        List<List<Integer>> resultList = new ArrayList<>();

        for (Document viajeDocument : viajeDocuments) {
            // Obtenemos id_monopatin y los id_viaje asociados
            int idMonopatin = viajeDocument.get("_id", Integer.class);
            List<String> viajes = viajeDocument.get("viajes", List.class);

            // Verificar si se están obteniendo correctamente los id_viaje
            System.out.println("id_monopatin: " + idMonopatin + ", viajes: " + viajes);

            // Consulta para obtener el total de tiempo de pausa desde la entidad Pausa
            TypedAggregation<Pausa> pausaAggregation = (TypedAggregation<Pausa>) Aggregation.newAggregation(
                    match(Criteria.where("id_viaje").in(viajes)),
                    group("id_viaje")
                            .sum("$tiempo").as("tiempoTotal"),
                    group()
                            .sum("$tiempoTotal").as("totalTiempoPausa")
            );

            AggregationResults<Document> pausaResult = mongoTemplate.aggregate(pausaAggregation, "pausa", Document.class);
            Document pausaDocument = pausaResult.getUniqueMappedResult();

            // Creamos la lista final con id_monopatin, total de horas y total de minutos
            List<Integer> monopatinUso = new ArrayList<>();
            monopatinUso.add(idMonopatin);

            if (pausaDocument != null) {
                long totalTiempoPausaMillis = pausaDocument.get("totalTiempoPausa", Long.class);
                int totalHoras = (int) (totalTiempoPausaMillis / 3600000); // Convertir milisegundos a horas
                int totalMinutos = (int) ((totalTiempoPausaMillis % 3600000) / 60000); // Obtener minutos restantes

                monopatinUso.add(totalHoras);
                monopatinUso.add(totalMinutos);
            } else {
                // Si no hay pausas para los viajes, agregamos 0 horas y 0 minutos
                monopatinUso.add(0);
                monopatinUso.add(0);
            }

            resultList.add(monopatinUso);
        }

        return resultList;
    }
}
