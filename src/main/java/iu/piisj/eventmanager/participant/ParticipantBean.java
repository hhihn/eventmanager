package iu.piisj.eventmanager.participant;

import iu.piisj.eventmanager.dto.Participant;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

import java.util.List;

@Named
@RequestScoped
public class ParticipantBean {

    public List<Participant> getParticipants(){
        return List.of(
                new Participant("Mustermann", "Max", "maxmustermann@mail.com", "Aktiv"),
                new Participant("Mustermann", "Eva", "evamustermann@mail.com", "Inaktiv")
        );
    }

}
