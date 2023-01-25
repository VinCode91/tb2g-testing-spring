package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Vets;
import org.springframework.samples.petclinic.service.ClinicService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VetControllerTest {
    @Mock
    ClinicService clinicService;
    @InjectMocks
    VetController vetController;

    List<Vet> allVets;

    @BeforeEach
    void setUp() {
        Vet vet1 = new Vet();
        vet1.setFirstName("James");
        vet1.setLastName("Carter");
        Vet vet2 = new Vet();
        vet2.setFirstName("Linda");
        vet2.setLastName("Douglas");
        allVets = new ArrayList<>(Arrays.asList(vet1, vet2));
        given(clinicService.findVets()).willReturn(allVets);
    }

    @Test
    void showVetList() {
        // given
        Map<String, Object> model = new HashMap<>();
        // when
        String page = vetController.showVetList(model);

        // then
        then(clinicService).should().findVets();
        assertEquals("vets/vetList", page);
        assertEquals(2, ((Vets) model.get("vets")).getVetList().size());
    }

    @Test
    void showResourcesVetList() {
        // when
        Vets resource = vetController.showResourcesVetList();

        // then
        assertEquals(2, resource.getVetList().size());
        assertEquals(allVets, resource.getVetList());
        verify(clinicService).findVets();
    }
}
