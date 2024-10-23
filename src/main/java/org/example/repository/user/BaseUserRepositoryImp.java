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
       public EntityManager entityManager;

        public void save(T user){
                entityManager.persist(user);
        }
        public void saveUserAndPass(PassAndUser passAndUser) {
                entityManager.persist(passAndUser);
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
    public void updatePass(PassAndUser passAndUser) {
            entityManager.merge(passAndUser);
    }

    public void update(T t) {

                entityManager.merge(t);

        }
        //todo: must check this out
        public void delete(T t)throws Exception{
            entityManager.remove(t);
        }
        //todo: must check this all

    @Override

    public Object find(String userName , Class<T> userType) {
        try {
            Query query = entityManager.createNativeQuery("""
        select * from pass_and_user
        where username = ?
""");
            query.setParameter(1, userName);
            return query.getSingleResult();

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
