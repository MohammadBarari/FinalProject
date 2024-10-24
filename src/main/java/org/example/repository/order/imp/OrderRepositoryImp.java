package org.example.repository.order.imp;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import org.example.domain.*;
import org.example.repository.order.OrderRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Repository
public class OrderRepositoryImp implements OrderRepository {
    @PersistenceContext
    EntityManager entityManager;
    @Override
    public void save(Orders orders) {
            entityManager.persist(orders);
    }

    @Override
    public void update(Orders orders) {
        entityManager.merge(orders);
    }

    @Override
    public void delete(int orderId) {
            Orders orders = entityManager.find(Orders.class, orderId);
            entityManager.remove(orders);
    }

    @Override
    public Orders findById(int orderId) {
        try {
            Orders orders = entityManager.find(Orders.class, orderId);
            return orders;
        }catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Orders> findAll() {
        Query query = entityManager.createQuery("from Orders order", Orders.class);
        List<Orders> orders = query.getResultList();
        return orders;
    }

    @Override
        public List<Orders> selectByEmployeeSubHandler(Integer employeeId) {
        try {
            Query query = entityManager.createNativeQuery("""
        select * from orders
        where employee_id = ?
""", Orders.class);
            query.setParameter(1, employeeId);
            List<Orders> orders = query.getResultList();
            return orders;
        }catch (Exception e) {
            return null;
        }
    }
    @Override
    public List<Orders> selectOrdersByCustomer(Integer customerId) {
        try {
            Query query = entityManager.createNativeQuery("""
        select * from orders where customer_id = ?
""", Orders.class);
        return  query.setParameter(1, customerId).getResultList();
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public List<Orders> selectOrdersBySubHandlerId(Integer subHandlerId) {
        try {
            Query query = entityManager.createNativeQuery("""
        select o.* from orders as o join   sub_handler sh on sh.id = o.sub_handler_id
where sh.id = ? and order_state ='UNDER_CHOOSING_EMPLOYEE' or order_state = 'WAITING_FOR_EMPLOYEE_OFFER'
""",Orders.class);
            query.setParameter(1, subHandlerId);
            return  query.getResultList();
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public List<Orders> selectActiveOrdersForEmployee() {
       try {
           Query query = entityManager.createNativeQuery("""
        select o.* from orders as o join   sub_handler sh on sh.id = o.sub_handler_id
where sh.id = ? and order_state ='UNDER_CHOOSING_EMPLOYEE' or order_state = 'WAITING_FOR_EMPLOYEE_OFFER'
""",Orders.class);
       return  query.getResultList();
       }catch (Exception e){
           return null;
       }
    }

    @Override
    public List<Orders> selectGotOrdersByEmployeeId(Integer employeeId) {
        try {
            Query query = entityManager.createNativeQuery("""
        select orders.* from customtestschema.orders join customtestschema.employee e on e.id = orders.employee_id
        where e.id = ?
""",Orders.class);
            query.setParameter(1, employeeId);
            return  query.getResultList();
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public List<Orders> selectPaidOrdersForEmployee(Integer employeeId) {
        try {
            Query query = entityManager.createNativeQuery("""
    select * from customtestschema.orders join customtestschema.employee e on e.id = orders.employee_id
    where e.id = ? and order_state = 'PAID'
""",Orders.class);
            query.setParameter(1, employeeId);
            return  query.getResultList();
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public List<Orders> selectPaidOrdersForCustomer(Integer employeeId) {
        try {
            Query query = entityManager.createNativeQuery("""
    select * from customtestschema.orders join customtestschema.customer e on e.id = orders.customer_id
    where e.id = ? and order_state = 'PAID'
""",Orders.class);
            query.setParameter(1, employeeId);
            return  query.getResultList();
        }catch (Exception e){
            return null;
        }

    }

    @Override
    public List<Orders> optionalSelectOrders(LocalDate startDate, LocalDate endDate, List<String> handlersName, List<String> subHandlers) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Orders> cq = cb.createQuery(Orders.class);
        Root<Orders> ordersRoot = cq.from(Orders.class);
        List<Predicate> predicates = new ArrayList<>();
        if (startDate != null && endDate != null) {
            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
            predicates.add(cb.between(ordersRoot.get("timeOfWork"), startDateTime, endDateTime));
        }
        if (subHandlers != null && !subHandlers.isEmpty()) {
            List<Predicate> subHandlerPredicates = new ArrayList<>();
            Join<Orders, SubHandler> subHandlerJoin = ordersRoot.join("subHandler");

            for (String subHandlerName : subHandlers) {
                subHandlerPredicates.add(cb.like(cb.lower(subHandlerJoin.get("name")), "%" + subHandlerName.toLowerCase() + "%"));
            }

            if (!subHandlerPredicates.isEmpty()) {
                predicates.add(cb.or(subHandlerPredicates.toArray(new Predicate[0])));
            }
        }
        if (handlersName != null && !handlersName.isEmpty()) {
            List<Predicate> handlerPredicates = new ArrayList<>();
            Join<Orders, SubHandler> subHandlerJoin = ordersRoot.join("subHandler");
            Join<SubHandler, Handler> handlerJoin = subHandlerJoin.join("handler");

            for (String handlerName : handlersName) {
                handlerPredicates.add(cb.like(cb.lower(handlerJoin.get("name")), "%" + handlerName.toLowerCase() + "%"));
            }
            if (!handlerPredicates.isEmpty()) {
                predicates.add(cb.or(handlerPredicates.toArray(new Predicate[0])));
            }
        }
        cq.select(ordersRoot).where(cb.and(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(cq).getResultList();
    }

}
