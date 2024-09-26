package org.example.repository.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.example.domain.PassAndUser;
import org.example.domain.User;
import org.example.util.HibernateUtil;

public class BaseUserRepositoryImp<T extends User> implements BaseUserRepository<T>{
        public void save(T user){
                EntityManager entityManager = HibernateUtil.getInstance().getEntityManager();
            try {
                entityManager.getTransaction().begin();
                entityManager.persist(user);
                entityManager.getTransaction().commit();
            }catch (Exception e) {
                entityManager.getTransaction().rollback();
                throw e;
            }
        }
        public void saveUserAndPass(PassAndUser passAndUser) {
            EntityManager entityManager = HibernateUtil.getInstance().getEntityManager();
            try {
                entityManager.getTransaction().begin();
                entityManager.persist(passAndUser);
                entityManager.getTransaction().commit();
            }catch (Exception e) {
                entityManager.getTransaction().rollback();
                throw e;
            }
        }
        public PassAndUser findPass(PassAndUser passAndUser){
            EntityManager entityManager = HibernateUtil.getInstance().getEntityManager();
            try {
                Query query = entityManager.createNativeQuery("""
            select * from passanduser
            where username = ? and pass = ? and typeofuser = ?
""",PassAndUser.class);
                return (PassAndUser) query.getSingleResult();
            }catch (Exception e) {
                return null;
            }
        }

    @Override
    public void updatePass(PassAndUser passAndUser) {
        EntityManager entityManager = HibernateUtil.getInstance().getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(passAndUser);
            entityManager.getTransaction().commit();
        }catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }

    public void update(T t) {
            EntityManager entityManager = HibernateUtil.getInstance().getEntityManager();
            try {
                entityManager.getTransaction().begin();
                entityManager.merge(t);
                entityManager.getTransaction().commit();
            }catch (Exception e){
                entityManager.getTransaction().rollback();
                throw e;
            }
        }
        //todo: must check this out
        public void delete(T t)throws Exception{
            EntityManager entityManager = HibernateUtil.getInstance().getEntityManager();
            entityManager.getTransaction().begin();
            entityManager.remove(t);
            entityManager.getTransaction().commit();
        }
        //todo: must check this all

    @Override
    public T find(String userName , Class<T> userType) {
        EntityManager entityManager = HibernateUtil.getInstance().getEntityManager();
        try {
            Query query = entityManager.createNativeQuery("""
        select * from passanduser
        where username = ?
""", userType);
            query.setParameter(1, userName);
            T t = (T) query.getSingleResult();
            return t;
        }catch (Exception e) {
            return null;
        }
    }

    @Override
    public T findById(int id, Class<T> tClass) {
        EntityManager entityManager = HibernateUtil.getInstance().getEntityManager();
        try {
            return entityManager.find(tClass, id);
        }catch (Exception e) {
            return null;
        }
    }
}
