package org.springframework.samples.petclinic.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.repository.PetRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClinicServiceImplTest {
    @Mock
    PetRepository petRepository;
    @InjectMocks
    ClinicServiceImpl clinicService;

    List<PetType> petTypes;

    @BeforeEach
    void setUp() {
        PetType canin = new PetType();
        canin.setName("Canin");
        PetType felin = new PetType();
        felin.setName("Félin");
        petTypes = new ArrayList<>(Arrays.asList(canin, felin));
    }

    @Test
    void findPetTypes() {
        // given - Hypothèses de départ
        when(petRepository.findPetTypes()).thenReturn(petTypes);
        // when - Exécution
        Collection<PetType> foundPetTypes = clinicService.findPetTypes();
        // then - Vérification du scénario
        assertEquals(2, foundPetTypes.size());
    }
}
