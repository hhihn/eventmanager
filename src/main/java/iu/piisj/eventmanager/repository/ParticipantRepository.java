package iu.piisj.eventmanager.repository;

import iu.piisj.eventmanager.participant.Participant;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

import java.util.List;

@ApplicationScoped
public class ParticipantRepository {

    private final EntityManager em =
            Persistence.createEntityManagerFactory("eventmanagerPU")
                    .createEntityManager();

    public List<Participant> findByEvent(Long eventId) {
        return em.createQuery(
                        "SELECT p FROM Participant p WHERE p.event.id = :id",
                        Participant.class
                ).setParameter("id", eventId)
                .getResultList();
    }

    public void save(Participant participant) {
        em.getTransaction().begin();
        em.persist(participant);
        em.getTransaction().commit();
    }
}