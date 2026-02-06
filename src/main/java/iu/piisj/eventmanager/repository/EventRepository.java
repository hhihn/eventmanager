package iu.piisj.eventmanager.repository;

import iu.piisj.eventmanager.event.Event;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

import java.util.List;

@ApplicationScoped
public class EventRepository{

    private final EntityManager em =
            Persistence.createEntityManagerFactory("eventmanagerPU")
                    .createEntityManager();

    public List<Event> findAll(){
        return this.em.createQuery("SELECT e FROM Event e", Event.class).getResultList();
    }

    public Event findById(Long id) {
        return em.find(Event.class, id);
    }

    public Event update(Event event) {
        em.getTransaction().begin();
        Event merged = em.merge(event);
        em.getTransaction().commit();
        return merged;
    }

    public void save(Event event){
        this.em.getTransaction().begin();
        this.em.persist(event);
        this.em.getTransaction().commit();
    }
}
