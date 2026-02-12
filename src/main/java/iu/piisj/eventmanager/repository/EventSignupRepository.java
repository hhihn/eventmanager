package iu.piisj.eventmanager.repository;

import iu.piisj.eventmanager.eventsignup.EventSignup;
import iu.piisj.eventmanager.eventsignup.SignupStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class EventSignupRepository {

    private final EntityManager em =
            Persistence.createEntityManagerFactory("eventmanagerPU")
                    .createEntityManager();

    public void save(EventSignup signup) {
        em.getTransaction().begin();
        em.persist(signup);
        em.getTransaction().commit();
    }

    public Optional<EventSignup> findByUserAndEvent(Long userId, Long eventId) {
        try {
            EventSignup result = em.createQuery(
                            "SELECT es FROM EventSignup es WHERE es.user.id = :userId AND es.event.id = :eventId",
                            EventSignup.class
                    )
                    .setParameter("userId", userId)
                    .setParameter("eventId", eventId)
                    .getSingleResult();
            return Optional.of(result);
        } catch (jakarta.persistence.NoResultException e) {
            return Optional.empty();
        }
    }

    public List<EventSignup> findByEventId(Long eventId) {
        return em.createQuery(
                        "SELECT es FROM EventSignup es WHERE es.event.id = :eventId AND es.status = :status",
                        EventSignup.class
                )
                .setParameter("eventId", eventId)
                .setParameter("status", SignupStatus.REGISTERED)
                .getResultList();
    }

    public List<EventSignup> findByUserId(Long userId) {
        return em.createQuery(
                        "SELECT es FROM EventSignup es WHERE es.user.id = :userId AND es.status = :status",
                        EventSignup.class
                )
                .setParameter("userId", userId)
                .setParameter("status", SignupStatus.REGISTERED)
                .getResultList();
    }

    public void delete(EventSignup signup) {
        em.getTransaction().begin();
        em.remove(em.merge(signup));
        em.getTransaction().commit();
    }
}