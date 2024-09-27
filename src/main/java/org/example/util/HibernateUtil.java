package org.example.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class HibernateUtil {
    private static EntityManagerFactory emf;
    private static final HibernateUtil hibernateUtil;
    static {
        hibernateUtil = new HibernateUtil();
    }
    public static HibernateUtil getInstance() {
        emf = Persistence.createEntityManagerFactory("jpa");
        return hibernateUtil;
    }
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}
