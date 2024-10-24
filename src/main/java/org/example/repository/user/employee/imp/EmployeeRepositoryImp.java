package org.example.repository.user.employee.imp;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import org.example.domain.*;
import org.example.dto.EmployeeOutPutDto;
import org.example.dto.admin.EmployeeOutputDtoReport;
import org.example.enumirations.OrderState;
import org.example.repository.user.BaseUserRepositoryImp;
import org.example.repository.user.employee.EmployeeRepository;
import org.example.service.offer.OfferService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
@Primary
public class  EmployeeRepositoryImp extends BaseUserRepositoryImp<Employee> implements EmployeeRepository {


@Transactional
    @Override
    public Employee login(String username, String password) {
        try {
            Query query = entityManager.createNativeQuery("""
select employee.* from employee join pass_and_user pau on pau.id = employee.pass_and_user_id
where  pau.username= ? and pau.pass = ?
   """,Employee.class);
            query.setParameter(1, username);
            query.setParameter(2, password);
            Employee employee = (Employee) query.getSingleResult();
            return employee;
        }catch (Exception e) {
            return null;
        }
    }

    @Override
    public Boolean employeeExistsById(Integer id) {
        try {
            Query query = entityManager.createNativeQuery("""
             SELECT COUNT(e) FROM Employee e WHERE e.id = ?
""",Integer.class);
            query.setParameter(1, id);
            Integer count = (Integer) query.getSingleResult();
            return count > 0;
        }catch (Exception e) {
            return false;
        }
    }
    @Override
    public Boolean employeeExistsByEmail(String mail) {
        try {
            Query query = entityManager.createNativeQuery("""
             SELECT COUNT(e) FROM Employee e WHERE e.email = ?
""",Integer.class);
            query.setParameter(1, mail);
            Integer count = (Integer) query.getSingleResult();
            return count > 0;
        }catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<EmployeeOutputDtoReport> selectEmployeeByReports(
            LocalDate startDateRegistration,
            LocalDate endDateRegistration,
            Integer doneWorksStart,
            Integer doneWorksEnd,
            Integer offerSentStart,
            Integer offerSentEnd) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<EmployeeOutputDtoReport> query = cb.createQuery(EmployeeOutputDtoReport.class);
        Root<Employee> employee = query.from(Employee.class);
        List<Predicate> predicates = new ArrayList<>();

        if (startDateRegistration != null) {
            LocalDateTime registerTime = startDateRegistration.atStartOfDay();
            predicates.add(cb.greaterThanOrEqualTo(employee.get("timeOfRegistration"), registerTime));
        }
        if (endDateRegistration != null) {
            LocalDateTime endDate = endDateRegistration.atTime(23, 59, 59);
            predicates.add(cb.lessThanOrEqualTo(employee.get("timeOfRegistration"), endDate));
        }
        Predicate orderPredicate = cb.conjunction();
        Predicate offerPredicate = cb.conjunction();

        Expression<Long> worksCount = null;
        Expression<Long> offerCount = null;
        if (doneWorksStart != null || doneWorksEnd != null) {
            Root<Orders> orders = query.from(Orders.class);
            Join<Orders, Employee> ordersJoin = orders.join("employee", JoinType.INNER);
            worksCount = getOrderCount(orders, cb);
            orderPredicate = countingOrders(doneWorksStart, doneWorksEnd, orders, cb);
            predicates.add(cb.equal(ordersJoin.get("id"), employee.get("id")));
        }
        if (offerSentStart != null || offerSentEnd != null) {
            Root<Offer> offer = query.from(Offer.class);
            Join<Offer, Employee> offerJoin = offer.join("employee", JoinType.INNER);
            offerCount = cb.count(offerJoin.get("id"));
            offerPredicate = countingOfferPredicate(offerSentStart, offerSentEnd, offer, cb);
            predicates.add(cb.equal(offerJoin.get("id"), employee.get("id")));
        }
        query.groupBy(
                employee.get("id"),
                employee.get("name"),
                employee.get("last_name"),
                employee.get("email"),
                employee.get("phone"),
                employee.get("timeOfRegistration"),
                employee.get("employeeState")
        );
        query.select(cb.construct(EmployeeOutputDtoReport.class,
                        employee.get("id"),
                        employee.get("name"),
                        employee.get("last_name"),
                        employee.get("email"),
                        employee.get("phone"),
                        employee.get("timeOfRegistration"),
                        employee.get("employeeState"),
                        worksCount == null ? cb.literal(null) : worksCount,
                        offerCount == null ? cb.literal(null) : offerCount
                ))
                .where(cb.and(predicates.toArray(new Predicate[0])));
        List<Predicate> havingPredicates = new ArrayList<>();
        if (orderPredicate != null) {
            havingPredicates.add(orderPredicate);
        }
        if (offerPredicate != null) {
            havingPredicates.add(offerPredicate);
        }
        if (!havingPredicates.isEmpty()) {
            query.having(cb.and(havingPredicates.toArray(new Predicate[0])));
        }
        return entityManager.createQuery(query).getResultList();
    }

    private Predicate countingOrders(Integer start, Integer end, Root<Orders> ordersJoin, CriteriaBuilder cb) {
        Predicate predicate = cb.conjunction();
        Expression<Long> orderCount = getOrderCount(ordersJoin, cb);

        if (start != null) {
            predicate = cb.and(predicate, cb.greaterThanOrEqualTo(orderCount, start.longValue()));
        }
        if (end != null) {
            predicate = cb.and(predicate, cb.lessThan(orderCount, end.longValue()));
        }
        return predicate;
    }

    private Expression<Long> getOrderCount(Root<Orders> ordersJoin, CriteriaBuilder cb) {
        return cb.count(cb.selectCase()
                .when(cb.equal(ordersJoin.get("orderState"), "PAID"), 1)
                .otherwise(0));
    }

    private Predicate countingOfferPredicate(Integer start, Integer end, Root<Offer> offerJoin, CriteriaBuilder cb) {
        Predicate predicate = cb.conjunction();
        Expression<Long> offerCount = cb.count(offerJoin.get("id")); // Ensure you're counting the right attribute

        if (start != null) {
            predicate = cb.and(predicate, cb.greaterThanOrEqualTo(offerCount, start.longValue()));
        }
        if (end != null) {
            predicate = cb.and(predicate, cb.lessThan(offerCount, end.longValue()));
        }
        return predicate;
    }

    public List<Employee> selectEmployeesByOptionalInformation(String name, String lastName, String email, String phone, String handlerName) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> employee = query.from(Employee.class);
        List<Predicate> predicates = new ArrayList<>();
        if (name != null && !name.isEmpty()) {
            predicates.add(cb.like(employee.get("name"), "%" + name + "%"));
        }
        if (lastName != null && !lastName.isEmpty()) {
            predicates.add(cb.like(employee.get("last_name"), "%" + lastName + "%"));
        }
        if (email != null && !email.isEmpty()) {
            predicates.add(cb.like(employee.get("email"), "%" + email + "%"));
        }
        if (phone != null && !phone.isEmpty()) {
            predicates.add(cb.like(employee.get("phone"), "%" + phone + "%"));
        }
//        if (handlerName != null && !handlerName.isEmpty()) {
//            predicates.add(cb.like(employee.join("subHandlers").join("handler").get("name"), "%" + handlerName + "%"));
//        }
        if (handlerName != null && !handlerName.isEmpty()) {
                Join<Employee, SubHandler> subHandlerJoin = employee.join("subHandlers");
                Join<SubHandler, Handler> handlerJoin = subHandlerJoin.join("handler");
                predicates.add(cb.like(handlerJoin.get("name"), "%" + handlerName + "%"));
        }

        query.select(employee).where(cb.and(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public void SetUnderReviewState(String email) {
        try {
            Query query = entityManager.createNativeQuery("""
        update employee set employee_state = 'UNDER_REVIEW' where email = ?;
""");
            query.setParameter(1, email);
            query.executeUpdate();
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("failed to set under review state");
        }
    }
}
