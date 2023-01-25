package org.springframework.samples.petclinic.sfg;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("base-test")
public class YannyConfig {
    @Bean
    YannyWordProducer yannyWordProducerLol() {
        return new YannyWordProducer();
    }
}
