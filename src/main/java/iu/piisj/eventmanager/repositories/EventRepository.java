package iu.piisj.eventmanager.repositories;

import iu.piisj.eventmanager.event.Event;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

import java.util.List;

@ApplicationScoped
public class EventRepository {

    private final EntityManager em =
            Persistence.createEntityManagerFactory("eventmanagerPU")
                    .createEntityManager();

    public List<Event> findAll() {
        return em.createQuery(
                "SELECT e FROM Event e", Event.class
        ).getResultList();
    }

    public void save(Event event) {
        em.getTransaction().begin();
        em.persist(event);
        em.getTransaction().commit();
    }
}