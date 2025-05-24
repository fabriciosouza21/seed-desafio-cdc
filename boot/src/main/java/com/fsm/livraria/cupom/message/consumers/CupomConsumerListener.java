package com.fsm.livraria.cupom.message.consumers;

import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Requires(notEnv = Environment.TEST)
@KafkaListener
public class CupomConsumerListener {

    private final Logger logger = LoggerFactory.getLogger(CupomConsumerListener.class);

    @Topic("livraria-cupom")
    public void receiveCupom(String cupom) {
        logger.info("Cupom received: {}", cupom);

    }
}
