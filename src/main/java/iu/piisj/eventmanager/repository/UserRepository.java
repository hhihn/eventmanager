package iu.piisj.eventmanager.repository;

import iu.piisj.eventmanager.usermanagement.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

@ApplicationScoped
public class UserRepository {

    private final EntityManager em =
            Persistence.createEntityManagerFactory("eventmanagerPU")
                    .createEntityManager();

    public void save(User user) {
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
    }

    public boolean emailExists(String email) {
        Long count = em.createQuery(
                        "SELECT COUNT(u) FROM User u WHERE u.email = :email",
                        Long.class
                ).setParameter("email", email)
                .getSingleResult();
        return count > 0;
    }
}