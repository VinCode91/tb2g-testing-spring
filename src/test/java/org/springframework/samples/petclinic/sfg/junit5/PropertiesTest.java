package org.springframework.samples.petclinic.sfg.junit5;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.samples.petclinic.sfg.HearingInterpreter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;

// @SpringJUnitConfig(classes = PropertiesTest.TestConfig.class)
// @ActiveProfiles("externalized")
// Annotations ci-dessus ne fonctionnent pas avec les @Nested
// Chaque @Nested class doit avoir son propre contexte Spring, on ne peut utiliser
// un même @SpringJUnitConfig pour toutes les classes.
public class PropertiesTest {

    @Configuration
    @ComponentScan("org.springframework.samples.petclinic.sfg")
    static class TestConfig{}

    @Nested
    @TestPropertySource("classpath:aloys.properties")
    @SpringJUnitConfig(classes = PropertiesTest.TestConfig.class)
    @ActiveProfiles("externalized")
    class PropertiesAloysTest {
        @Autowired
        HearingInterpreter hearingInterpreter;
        @Test
        void whatIHeard() {
            String word = hearingInterpreter.whatIHeard();
            assertEquals("AlOyS", word);
        }
    }

    @Nested
    @TestPropertySource("classpath:laurel.properties")
    @SpringJUnitConfig(classes = PropertiesTest.TestConfig.class)
    @ActiveProfiles("extern-laurel") // Le profil externalized aurait suffi pour l'exo
    class PropertiesLaurelTest {
        @Autowired
        HearingInterpreter hearingInterpreter;
        @Test
        void whatIHeard() {
            String word = hearingInterpreter.whatIHeard();
            assertEquals("Laureline", word);
        }
    }
}
