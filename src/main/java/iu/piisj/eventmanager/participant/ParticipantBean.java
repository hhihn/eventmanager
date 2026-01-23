package iu.piisj.eventmanager.participant;

import iu.piisj.eventmanager.dto.ParticipantDTO;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

import java.util.List;

@Named
@RequestScoped
public class ParticipantBean {

    public List<ParticipantDTO> getParticipants(){
        return List.of(
                new ParticipantDTO("Mustermann", "Max", "maxmustermann@mail.com", "Aktiv"),
                new ParticipantDTO("Mustermann", "Eva", "evamustermann@mail.com", "Inaktiv")
        );
    }

}
