//package org.example.repository.customerCart.imp;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import jakarta.persistence.Query;
//import org.example.domain.CustomerCart;
//import org.example.repository.customerCart.CustomerCartRepository;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public class CustomerCartRepositoryImp implements CustomerCartRepository {
//    @PersistenceContext
//    private EntityManager em;
//
//    @Override
//    public void saveCustomerCart(CustomerCart customerCart) {
//        em.persist(customerCart);
//    }
//
//    @Override
//    public void updateCustomerCart(CustomerCart customerCart) {
//        em.merge(customerCart);
//    }
//
//    @Override
//    public CustomerCart selectCustomerCart(Integer id) {
//      try {
//          Query query = em.createNativeQuery("""
//        select * from customtestschema.customer_cart where customer_id = ?
//""",CustomerCart.class);
//          query.setParameter(1, id);
//          return (CustomerCart) query.getSingleResult();
//      }catch(Exception e) {
//          return null;
//      }
//    }
//
//}
