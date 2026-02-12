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
        Event event = em.find(Event.class, id);
        if (event != null) {
            em.refresh(event);  // Always fetch fresh from database
        }
        return event;
    }

    public void save(Event event){
        this.em.getTransaction().begin();
        this.em.persist(event);
        this.em.getTransaction().commit();
        this.em.clear();
    }

    public void update(Event event) {
        em.getTransaction().begin();
        em.merge(event);
        em.getTransaction().commit();
        em.clear();  // Clear cache after write
    }
}
