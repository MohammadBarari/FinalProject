//package org.example.repository.handler.imp;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import jakarta.persistence.Query;
//import org.example.domain.Handler;
//import org.example.repository.handler.HandlerRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//@Repository
//public class HandlerRepositoryImp implements HandlerRepository {
//    @PersistenceContext
//    private EntityManager entityManager;
//    @Override
//    public void save(Handler handler) {
//            entityManager.persist(handler);
//    }
//
//    @Override
//    public void update(Handler handler) {
//            entityManager.merge(handler);
//    }
//
//    @Override
//    public void delete(int id) {
//            Handler handler = entityManager.find(Handler.class, id);
//            entityManager.remove(handler);
//    }
//
//    @Override
//    public Handler findById(int id) {
//        try {
//        return entityManager.find(Handler.class, id);
//        }catch (Exception e) {
//            return null;
//        }
//    }
//
//    @Override
//    public List<Handler> findAll() {
//
//        try {
//            Query query = entityManager.createQuery("from Handler",Handler.class);
//            return query.getResultList();
//        }catch (Exception e) {
//            return null;
//        }
//    }
//
//    @Override
//    public Handler findByName(String name) {
//        try {
//
//            Query query = entityManager.createNativeQuery("""
//        select * from handler where name = ?
//""", Handler.class);
//            query.setParameter(1, name);
//            return (Handler) query.getSingleResult();
//        }catch (Exception e) {
//            return null;
//        }
//    }
//}
