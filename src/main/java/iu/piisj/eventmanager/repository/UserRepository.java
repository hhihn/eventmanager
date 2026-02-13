package iu.piisj.eventmanager.repository;

import iu.piisj.eventmanager.usermanagement.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;

import java.util.List;

@ApplicationScoped
public class UserRepository {

    private final EntityManager em = Persistence.createEntityManagerFactory("eventmanagerPU")
            .createEntityManager();

    public void save(User user){
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
    }

    public boolean emailExists(String email){
        // SQL zählt wie viele Spalten in der Tabelle User die gleiche Email-Adresse haben
        // wie die die als Argument übergeben wurde
        // :email setzt die Email aus dem Argument des Methodenaufrufs in die Query ein, zB.
        // emailExists("heinke.hihn@iu.org) -> SELECT COUNT(u) FROM User u WHERE u.email = heinke.hihn@org
        Long count = em.createQuery("SELECT COUNT(u) FROM User u WHERE u.email = :email",
                Long.class
        ).setParameter("email", email).getSingleResult();
        return count > 0;
    }

    public User findByUsername(String username){
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.username = :username",
                    User.class)
                    .setParameter("username", username)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }

    public User findById(Long id) { return em.find(User.class, id); }

    public List<User> findAll() {
        return em.createQuery("SELECT u FROM User u ORDER BY u.name, u.firstname", User.class)
                .getResultList();
    }

}
