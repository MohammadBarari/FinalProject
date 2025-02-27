package org.example.repository.order.imp;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.example.domain.*;
import org.example.exeptions.global.FailedDoingOperation;
import org.example.repository.order.CustomOrderRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Repository
public class OrderRepositoryImp implements CustomOrderRepository {
    @PersistenceContext
    EntityManager entityManager;
    @Override
    public List<Orders> optionalSelectOrders
            (LocalDate startDate, LocalDate endDate,
             List<String> handlersName, List<String> subHandlers) {
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
            Join<Orders, SubHandler> subHandlerJoin = ordersRoot.join("subHandler", JoinType.LEFT);
            for (String subHandlerName : subHandlers) {
                System.out.println(subHandlerName);
                subHandlerPredicates.add(cb.like(cb.lower(subHandlerJoin.get("name")),
                        "%" + subHandlerName.toLowerCase() + "%"));
            }
            if (!subHandlerPredicates.isEmpty()) {
                predicates.add(cb.or(subHandlerPredicates.toArray(new Predicate[0])));
            }
        }
        if (handlersName != null && !handlersName.isEmpty()) {
            List<Predicate> handlerPredicates = new ArrayList<>();
            Join<Orders, SubHandler> subHandlerJoin = ordersRoot.join("subHandler", JoinType.LEFT);
            Join<SubHandler, Handler> handlerJoin = subHandlerJoin.join("handler", JoinType.LEFT);

            for (String handlerName : handlersName) {
                handlerPredicates.add(cb.like(cb.lower(handlerJoin.get("name")), "%" +
                        handlerName.toLowerCase() + "%"));
            }
            if (!handlerPredicates.isEmpty()) {
                predicates.add(cb.or(handlerPredicates.toArray(new Predicate[0])));
            }
        }
        cq.select(ordersRoot).where(cb.and(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public List<Orders> optionalSelectOrdersForEmployee(Integer employeeId, String orderState) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Orders> query = cb.createQuery(Orders.class);
        Root<Orders> ordersRoot = query.from(Orders.class);
        List<Predicate> predicates = new ArrayList<>();
        if (orderState != null) {
            predicates.add(cb.equal(ordersRoot.get("orderState"), orderState));
        }
        if (employeeId == null) {
            throw new FailedDoingOperation("employeeId is null");
        }else {
            Join<Orders,Employee> employeeJoin = ordersRoot.join("employee", JoinType.INNER);
            predicates.add(cb.equal(employeeJoin.get("id"), employeeId));
        }
        query.select(ordersRoot).where(cb.and(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<Orders> optionalSelectOrdersForCustomer(Integer customerId, String orderState) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Orders> query = cb.createQuery(Orders.class);
        Root<Orders> ordersRoot = query.from(Orders.class);
        List<Predicate> predicates = new ArrayList<>();
        if (orderState != null) {
            predicates.add(cb.equal(ordersRoot.get("orderState"), orderState));
        }
        if (customerId == null) {
            throw new FailedDoingOperation("customerId is null");
        }else {
            Join<Orders,Customer> employeeJoin = ordersRoot.join("customer", JoinType.INNER);
            predicates.add(cb.equal(employeeJoin.get("id"), customerId));
        }
        query.select(ordersRoot).where(cb.and(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(query).getResultList();
    }

}
