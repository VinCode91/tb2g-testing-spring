package org.springframework.samples.petclinic.sfg;


//@Component
//@Primary
public class AloysWordProducer implements WordProducer{
    @Override
    public String getWord() {
        return "Aloys";
    }
}
