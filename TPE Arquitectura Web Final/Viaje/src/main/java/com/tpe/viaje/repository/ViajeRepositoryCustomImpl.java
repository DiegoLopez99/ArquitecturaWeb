package com.tpe.viaje.repository;

import com.tpe.viaje.DTO.MonopatinPorUso;
import com.tpe.viaje.DTO.TotalFacturadoDTO;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;
import org.springframework.data.mongodb.core.query.Criteria;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ViajeRepositoryCustomImpl implements ViajeRepositoryCustom {
    private final MongoTemplate mongoTemplate;

    public ViajeRepositoryCustomImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }



    @Override
    public List<Long> obtenerIdMonopatinesMasViajesAnio(int x, int anio) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.project("id_monopatin", "inicio"),
                Aggregation.match(Criteria.where("inicio").gte(LocalDate.of(anio, 1, 1).atStartOfDay()).lte(LocalDate.of(anio, 12, 31).atTime(23, 59, 59)))
        );

        AggregationResults<Document> result1 = mongoTemplate.aggregate(aggregation, "viaje", Document.class);

        Aggregation aggregation2 = Aggregation.newAggregation(
                Aggregation.group("id_monopatin").count().as("totalViajes"),
                Aggregation.match(Criteria.where("totalViajes").gte(x))
        );

        AggregationResults<Document> result2 = mongoTemplate.aggregate(aggregation2, "viaje", Document.class);
        List<Long> results2 = result2.getMappedResults().stream()
                .map(document -> document.getLong("_id")) // Obtener el id_monopatin
                .collect(Collectors.toList());

        return results2;
    }

    @Override
    public List<MonopatinPorUso> obtenerUsoMonopatin() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("id_monopatin")
                        .sum(ArithmeticOperators.Subtract.valueOf("$fin").subtract("$inicio")).as("duracionTotal"),
                Aggregation.project()
                        .and("id_monopatin").previousOperation()
                        .and("duracionTotal").divide(3600).as("horas") // convierte segundos a horas
                        .and("duracionTotal").mod(3600).divide(60).as("minutos") // obtiene el residuo en segundos y convierte a minutos
        );

        AggregationResults<MonopatinPorUso> result = mongoTemplate.aggregate(aggregation, "viaje", MonopatinPorUso.class);

        return result.getMappedResults();
    }

    @Override
    public TotalFacturadoDTO calcularTotalFacturadoEnRango(Integer mesInicio, Integer mesFinal, Integer anio) {
        LocalDate startDate = LocalDate.of(anio, mesInicio, 1);
        LocalDate endDate = LocalDate.of(anio, mesFinal, LocalDate.of(anio, mesFinal, 1).lengthOfMonth());

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("inicio").gte(startDate.atStartOfDay()).lte(endDate.atTime(23, 59, 59))),
                Aggregation.group().sum("costo").as("totalFacturado")
        );

        AggregationResults<TotalFacturadoDTO> result = mongoTemplate.aggregate(aggregation, "viaje", TotalFacturadoDTO.class);

        TotalFacturadoDTO totalFacturadoDTO = result.getUniqueMappedResult();

        assert totalFacturadoDTO != null;
        totalFacturadoDTO.setMesInicio(mesInicio);
        totalFacturadoDTO.setMesFinal(mesFinal);
        totalFacturadoDTO.setAnio(anio);

        return totalFacturadoDTO;
    }


}
