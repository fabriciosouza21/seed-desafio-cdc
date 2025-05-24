package com.fsm.livraria.cupom.message.produces;

import com.fsm.livraria.cupom.entities.Cupom;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.Topic;
import reactor.core.publisher.Mono;

@KafkaClient
public interface CupomProducerCupomClient {
    @Topic("livraria-cupom")
    Mono<Cupom> sendCupom(Cupom cupom);

    @Topic("livraria-cupom-deadletter")
    Mono<Cupom> sendCupomDeadLetter(Cupom cupom);
}
