package org.example.repository.order.imp;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.*;
import org.example.enumirations.OrderState;
import org.example.enumirations.TypeOfUser;
import org.example.exeptions.global.FailedDoingOperation;
import org.example.repository.order.CustomOrderRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Repository
@Slf4j
public class OrderRepositoryImp implements CustomOrderRepository {
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public List<Orders> optionalSelectOrders
            (LocalDate startDate, LocalDate endDate,
             List<String> handlersName, List<String> subHandlers) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Orders> cq = cb.createQuery(Orders.class);
        Root<Orders> ordersRoot = cq.from(Orders.class);
        List<Predicate> predicates = getOrdersPredicate(startDate, endDate, cb, ordersRoot);

        Join<Orders, SubHandler> subHandlerJoin = ordersRoot.join("subHandler", JoinType.LEFT);
        if (subHandlers != null && !subHandlers.isEmpty()) {
            List<Predicate> subHandlerPredicates = createSubHandlerPredicatesJoiningWithOrder(subHandlers, cb, subHandlerJoin);
            if (!subHandlerPredicates.isEmpty()) {
                predicates.add(cb.or(subHandlerPredicates.toArray(new Predicate[0])));
            }
        }
        if (handlersName != null && !handlersName.isEmpty()) {
            List<Predicate> handlerPredicates = createHandlerJoiningWithSubHandlerPredicates(handlersName, subHandlerJoin, cb);
            if (!handlerPredicates.isEmpty()) {
                predicates.add(cb.or(handlerPredicates.toArray(new Predicate[0])));
            }
        }
        cq.select(ordersRoot).where(cb.and(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(cq).getResultList();
    }

    private static List<Predicate> createHandlerJoiningWithSubHandlerPredicates(List<String> handlersName, Join<Orders, SubHandler> subHandlerJoin, CriteriaBuilder cb) {
        List<Predicate> handlerPredicates = new ArrayList<>();
        Join<SubHandler, Handler> handlerJoin = subHandlerJoin.join("handler", JoinType.LEFT);

        for (String handlerName : handlersName) {
            handlerPredicates.add(cb.like(cb.lower(handlerJoin.get("name")), "%" +
                    handlerName.toLowerCase() + "%"));
        }
        return handlerPredicates;
    }

    private static List<Predicate> createSubHandlerPredicatesJoiningWithOrder(List<String> subHandlers, CriteriaBuilder cb, Join<Orders, SubHandler> subHandlerJoin) {
        List<Predicate> subHandlerPredicates = new ArrayList<>();
        for (String subHandlerName : subHandlers) {
            log.debug("subHandlerName: {}", subHandlerName);
            subHandlerPredicates.add(cb.like(cb.lower(subHandlerJoin.get("name")),
                    "%" + subHandlerName.toLowerCase() + "%"));
        }
        return subHandlerPredicates;
    }

    private static List<Predicate> getOrdersPredicate(LocalDate startDate, LocalDate endDate, CriteriaBuilder cb, Root<Orders> ordersRoot) {
        List<Predicate> predicates = new ArrayList<>();
        if (startDate != null && endDate != null) {
            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
            predicates.add(cb.between(ordersRoot.get("timeOfWork"), startDateTime, endDateTime));
        }
        return predicates;
    }

    @Override
    public List<Orders> optionalSelectOrdersForEmployee(Integer employeeId, String orderState) {
        return selectAnyOrdersFromUser(employeeId, OrderState.valueOf(orderState),TypeOfUser.EMPLOYEE);
    }


    @Override
    public List<Orders> optionalSelectOrdersForCustomer(Integer customerId, String orderState) {
        return selectAnyOrdersFromUser(customerId, OrderState.valueOf(orderState),TypeOfUser.CUSTOMER);
    }

    private List<Orders> selectAnyOrdersFromUser(Integer userId, OrderState orderState , TypeOfUser typeOfUser){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Orders> query = cb.createQuery(Orders.class);
        Root<Orders> ordersRoot = query.from(Orders.class);
        List<Predicate> predicates = new ArrayList<>();
        if (orderState != null) {
            predicates.add(cb.equal(ordersRoot.get("orderState"), orderState));
        }
        if (typeOfUser == TypeOfUser.CUSTOMER){
            return selectOrdersFromCustomer(userId, ordersRoot, predicates, cb, query);
        }else
            return selectOrdersFromEmployee(userId, ordersRoot, predicates, cb, query);
    }

    private List<Orders> selectOrdersFromEmployee(Integer employeeId, Root<Orders> ordersRoot, List<Predicate> predicates,
                                                  CriteriaBuilder cb, CriteriaQuery<Orders> query) {
        if (employeeId == null) {
            throw new FailedDoingOperation("employeeId is null");
        }else {
            Join<Orders,Employee> employeeJoin = ordersRoot.join("employee", JoinType.INNER);
            predicates.add(cb.equal(employeeJoin.get("id"), employeeId));
        }
        query.select(ordersRoot).where(cb.and(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(query).getResultList();
    }

    private List<Orders> selectOrdersFromCustomer(Integer customerId, Root<Orders> ordersRoot, List<Predicate> predicates,
                                                  CriteriaBuilder cb, CriteriaQuery<Orders> query) {
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
