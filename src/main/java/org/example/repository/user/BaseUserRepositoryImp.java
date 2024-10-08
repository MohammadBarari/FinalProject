package org.example.repository.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.example.domain.PassAndUser;
import org.example.domain.Users;
import org.springframework.stereotype.Repository;

@Repository
public class BaseUserRepositoryImp<T extends Users> implements BaseUserRepository<T>{

       @PersistenceContext
       private EntityManager entityManager;
       @Transactional
        public void save(T user,PassAndUser passAndUser){
            try {
                entityManager.persist(user);
            }catch (Exception e) {
                entityManager.getTransaction().rollback();
                throw e;
            }
        }
        @Transactional
        public void saveUserAndPass(PassAndUser passAndUser) {
            try {
                entityManager.persist(passAndUser);
            }catch (Exception e) {
                entityManager.getTransaction().rollback();
                throw e;
            }
        }
        public PassAndUser findPass(PassAndUser passAndUser){
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
    @Transactional
    public void updatePass(PassAndUser passAndUser) {
        try {
            entityManager.merge(passAndUser);
        }catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }

    @Transactional
    public void update(T t) {
            try {

                entityManager.merge(t);

            }catch (Exception e){
                entityManager.getTransaction().rollback();
                throw e;
            }
        }
        //todo: must check this out
    @Transactional
        public void delete(T t)throws Exception{
            entityManager.remove(t);
        }
        //todo: must check this all

    @Override

    public T find(String userName , Class<T> userType) {
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
        try {
            return entityManager.find(tClass, id);
        }catch (Exception e) {
            return null;
        }
    }
}
