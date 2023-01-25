package org.springframework.samples.petclinic.sfg.junit5;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.samples.petclinic.sfg.HearingInterpreter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.*;
@SpringJUnitConfig(classes = {HearingInterpreterComponentScanTest.TestConfig.class})
@ActiveProfiles("component-scan")
class HearingInterpreterComponentScanTest {
    @Autowired
    HearingInterpreter hearingInterpreter;
    @Test
    void whatIHeard() {
        String word = hearingInterpreter.whatIHeard();
        assertEquals("Laurel", word);
    }

    @Configuration
    @ComponentScan(basePackages = {"org.springframework.samples.petclinic.sfg"})
    //@Profile("component-scan")
    static class TestConfig {

    }
}
