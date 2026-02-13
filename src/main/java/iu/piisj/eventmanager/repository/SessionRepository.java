package iu.piisj.eventmanager.repository;

import iu.piisj.eventmanager.session.Session;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import java.util.List;

@ApplicationScoped
public class SessionRepository {

    private final EntityManager em = Persistence.createEntityManagerFactory("eventmanagerPU")
            .createEntityManager();

    public void save(Session session) {
        em.getTransaction().begin();
        em.persist(session);
        em.getTransaction().commit();
        em.clear();
    }

    public void update(Session session) {
        em.getTransaction().begin();
        em.merge(session);
        em.getTransaction().commit();
        em.clear();
    }

    public Session findById(Long id) {
        Session session = em.find(Session.class, id);
        if (session != null) {
            em.refresh(session);
        }
        return session;
    }

    public List<Session> findByEventId(Long eventId) {
        return em.createQuery(
                        "SELECT s FROM Session s WHERE s.event.id = :eventId ORDER BY s.startTime",
                        Session.class)
                .setParameter("eventId", eventId)
                .getResultList();
    }

    public void delete(Session session) {
        em.getTransaction().begin();
        em.remove(em.merge(session));
        em.getTransaction().commit();
        em.clear();
    }
}