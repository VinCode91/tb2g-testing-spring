package org.springframework.samples.petclinic.web;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringJUnitConfig(locations = {"classpath:spring/mvc-test-config.xml", "classpath:spring/mvc-core-config.xml"})
@WebAppConfiguration // annotation necessaire pour le chargement du contexte
@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {
    public static final String REDIRECT_URL_ERROR = "owners/createOrUpdateOwnerForm";
    @Autowired
    OwnerController ownerController;
    @Autowired
    ClinicService clinicService;

    MockMvc mockMvc;
    @Captor
    ArgumentCaptor<Owner> ownerCaptor;

    @BeforeEach
    void setUp() {
        mockMvc = standaloneSetup(ownerController).build();
    }

    @Test
    void testUpdateOwnerPostValid() throws Exception {
        int ownerId = 3;
        mockMvc.perform(post("/owners/{ownerId}/edit", ownerId)
                        .param("firstName", "Aloys")
                        .param("lastName", "SARR")
                        .param("address", "123 rue Lamine Gueye")
                        .param("city", "Thiès")
                        .param("telephone", "0123456789"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/" + ownerId));
        then(clinicService).should().saveOwner(ownerCaptor.capture());
        assertNotNull(ownerCaptor.getValue());
        assertEquals(Integer.valueOf(ownerId), ownerCaptor.getValue().getId());
    }

    @Test
    void testUpdateOwnerPostNotValid() throws Exception {
        mockMvc.perform(post("/owners/{ownerId}/edit", 3)
                        .param("firstName", "Aloys")
                        .param("lastName", "SARR")
                        .param("address", "123 rue Lamine Gueye")
                        .param("city", "Thiès")
                        .param("telephone", "0.123456789"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("owner"))
                .andExpect(model().attributeHasFieldErrors("owner", "telephone"))
                .andExpect(view().name(REDIRECT_URL_ERROR));
    }

    @Test
    void testNewOwnerPostValid() throws Exception {
        mockMvc.perform(post("/owners/new")
                    .param("firstName", "Jo")
                    .param("lastName", "SARR")
                    .param("Address", "123 Rue Senghor") // la majuscule ne pose pas de souci
                    .param("city", "Thiès")
                    .param("telephone", "0123456789"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void testNewOwnerPostNotValid() throws Exception {
        mockMvc.perform(post("/owners/new")
                        .param("firstName", "Jo")
                        .param("lastName", "SARR")
                        .param("city", "Thiès"))
                .andExpect(status().isOk())
                // Le nom de l'attribut lié à l'objet qui encapsule les paramètres du corps de la requête
                // dans model() correspond au nom de la classe de l'objet
                .andExpect(model().attributeExists("owner"))
                .andExpect(model().attributeHasErrors("owner"))
                .andExpect(model().attributeHasFieldErrors("owner", "address"))
                .andExpect(model().attributeHasFieldErrors("owner", "telephone"))
                .andExpect(view().name(REDIRECT_URL_ERROR));
    }

    @Test
    void initCreationForm() throws Exception {
        mockMvc.perform(get("/owners/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("owner"))
                .andExpect(view().name(REDIRECT_URL_ERROR));
    }

    @Test
    void testFindByNameNotFound() throws Exception {
        // le mock clinicService ne renvoie rien après appel de findOwnerByLastName
        mockMvc.perform(get("/owners")
                        .param("lastName", "Don't find me !")
                        .param("city", "Toronto")
                ) //Rajoute un paramètre à la requête GET - Les paramètres
                // sont mappés par un objet Owner dans la méthode d'origine
                // Donc un paramètre ne correspondant pas à un attribut de Owner sera ignoré
                .andExpect(status().isOk())
                .andExpect(view().name("owners/findOwners"));
    }

    @Test
    void testFindWithManyresults() throws Exception {
        // given
        ArgumentCaptor<String> emptyCaptor = ArgumentCaptor.forClass(String.class);
        given(clinicService.findOwnerByLastName(emptyCaptor.capture())).willReturn(Lists.newArrayList(new Owner(), new Owner()));

        // when and then
        mockMvc.perform(get("/owners"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("owner")) // objet encapsulant les paramèters de la requête
                .andExpect(model().attributeExists("selections"))
                .andExpect(model().size(2))
                .andExpect(view().name("owners/ownersList"));
        // Vérification chaîne vide
        then(clinicService).should().findOwnerByLastName(""); // Ce test suffit pour l'exercice
            // Même test que ci-dessus avec ArgumentCaptor
        assertEquals("", emptyCaptor.getValue());
    }

    @Test
    void testFindOneOwner() throws Exception {
        // given
        Owner o = new Owner(); o.setId(1);
        given(clinicService.findOwnerByLastName(anyString())).willReturn(Collections.singletonList(o));

        // when and then
        mockMvc.perform(get("/owners"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/1"));
        then(clinicService).should().findOwnerByLastName(anyString());
    }

    @AfterEach
    void tearDown() {
        reset(clinicService); // effacer les invocations dans les tests effectués avant celui-ci
    }
}
