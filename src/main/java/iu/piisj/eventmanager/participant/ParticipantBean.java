package iu.piisj.eventmanager.participant;

import iu.piisj.eventmanager.dto.ParticipantDTO;
import iu.piisj.eventmanager.event.Event;
import jakarta.faces.context.FacesContext;
import iu.piisj.eventmanager.repository.EventRepository;
import iu.piisj.eventmanager.repository.ParticipantRepository;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class ParticipantBean implements Serializable {

    @Inject
    private ParticipantRepository participantRepository;

    @Inject
    private EventRepository eventRepository;

    private ParticipantDTO newParticipant = new ParticipantDTO();

    public ParticipantDTO getNewParticipant() {
        return newParticipant;
    }

    public List<Participant> getAllParticipants() {
        return participantRepository.findAll();
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public List<String> getAvailableStates() {
        return List.of("Angemeldet", "Teilgenommen", "Abgemeldet");
    }

    public void saveParticipant() {

        Event event = eventRepository.findById(newParticipant.getEventId());

        Participant participant = new Participant(
                newParticipant.getFirstname(),
                newParticipant.getName(),
                newParticipant.getEmail(),
                newParticipant.getState(),
                event
        );

        participantRepository.save(participant);

        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(
                        FacesMessage.SEVERITY_INFO,
                        "Erfolg",
                        "Der Teilnehmer wurde erfolgreich angelegt."
                )
        );
        newParticipant = new ParticipantDTO();
    }
}