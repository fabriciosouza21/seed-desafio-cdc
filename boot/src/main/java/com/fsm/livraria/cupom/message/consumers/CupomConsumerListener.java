package com.fsm.livraria.cupom.message.consumers;

import com.fsm.livraria.cupom.entities.Cupom;
import com.fsm.livraria.cupom.message.produces.CupomProducerCupomClient;
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

    private final CupomProducerCupomClient cupomProducerCupomClient;

    public CupomConsumerListener(CupomProducerCupomClient cupomProducerCupomClient) {
        this.cupomProducerCupomClient = cupomProducerCupomClient;
    }

    @Topic("livraria-cupom")
    public void receiveCupom(Cupom cupom) {
        logger.info("Cupom received: {}", cupom);
        try {
            //Executar o seriviço aqui
            logger.info("Cupom processado com sucesso: {}", cupom);
        } catch (Exception e) {
            logger.error("enviar para a fila morta: {}", cupom);
            cupomProducerCupomClient.sendCupomDeadLetter(cupom).subscribe();
        }


    }
}
