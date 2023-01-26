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
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
class VetControllerTest {
    @Mock
    ClinicService clinicService;
    @InjectMocks
    VetController vetController;

    List<Vet> allVets;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        Vet vet1 = new Vet();
        vet1.setFirstName("James");
        vet1.setLastName("Carter");
        Vet vet2 = new Vet();
        vet2.setFirstName("Linda");
        vet2.setLastName("Douglas");
        allVets = new ArrayList<>(Arrays.asList(vet1, vet2));
        when(clinicService.findVets()).thenReturn(allVets);

        mockMvc = standaloneSetup(vetController).build();
    }

    /** On peut directement utiliser l'objet model fourni par Spring MVC Test
     * au lieu d'un mock Mockito ou d'instancier carrément
     * un objet model comme dans {@link #showVetList()} */
    @Test
    void testControllerShowVetList() throws Exception {
        mockMvc.perform(get("/vets.html"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("vets"))
                .andExpect(view().name("vets/vetList"));
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
