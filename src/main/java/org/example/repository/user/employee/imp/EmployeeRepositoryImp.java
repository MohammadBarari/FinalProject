package org.example.repository.user.employee.imp;

import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import org.example.domain.*;
import org.example.dto.admin.EmployeeInputHandlersDto;
import org.example.dto.admin.EmployeeOutputDtoReport;
import org.example.repository.user.BaseUserRepositoryImp;
import org.example.repository.user.employee.EmployeeRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
        Subquery<Long> worksCountSubquery = query.subquery(Long.class);
        Root<Orders> orders = worksCountSubquery.from(Orders.class);
        worksCountSubquery.select(cb.count(orders.get("id")));
        worksCountSubquery.where(
                cb.equal(orders.get("employee").get("id"), employee.get("id")),
                cb.equal(orders.get("orderState"), "PAID")
        );

        Subquery<Long> offersCountSubquery = query.subquery(Long.class);
        Root<Offer> offers = offersCountSubquery.from(Offer.class);
        offersCountSubquery.select(cb.count(offers.get("id")));
        offersCountSubquery.where(
                cb.equal(offers.get("employee").get("id"), employee.get("id"))
        );
        query.select(cb.construct(EmployeeOutputDtoReport.class,
                employee.get("id"),
                employee.get("name"),
                employee.get("last_name"),
                employee.get("email"),
                employee.get("phone"),
                employee.get("timeOfRegistration"),
                employee.get("employeeState"),
                worksCountSubquery,
                offersCountSubquery
        ));
        query.where(cb.and(predicates.toArray(new Predicate[0])));
        query.groupBy(
                employee.get("id"),
                employee.get("name"),
                employee.get("last_name"),
                employee.get("email"),
                employee.get("phone"),
                employee.get("timeOfRegistration"),
                employee.get("employeeState")
        );
        List<Predicate> havingOrdersPredicates = new ArrayList<>();
        if (doneWorksStart != null || doneWorksEnd != null) {
            havingOrdersPredicates.add(countingOrders(doneWorksStart, doneWorksEnd, worksCountSubquery, cb, employee));
        }
        if (offerSentStart != null || offerSentEnd != null) {
            havingOrdersPredicates.add(countingOfferPredicate(cb, offerSentStart, offerSentEnd, offersCountSubquery));
        }
        if (havingOrdersPredicates.size() > 0) {
            query.having(havingOrdersPredicates.toArray(new Predicate[0]));
        }
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public Employee findByUser(String user) {
       try {
           Query query = entityManager.createNativeQuery("""
        select employee.* from employee where phone = ?
""",Employee.class);
           query.setParameter(1, user);
           return (Employee) query.getSingleResult();
       }catch (Exception e) {
           return null;
       }
    }


    private Predicate countingOrders(Integer start, Integer end, Subquery<Long> orderCount, CriteriaBuilder cb, Root<Employee> employeeRoot) {
        Predicate predicate = cb.conjunction();
        if (start != null) {
            predicate = cb.and(predicate, cb.greaterThanOrEqualTo(orderCount, start.longValue()));
        }
        if (end != null) {
            predicate = cb.and(predicate, cb.lessThan(orderCount, end.longValue()));
        }
        return predicate;
    }

    private Predicate countingOfferPredicate(CriteriaBuilder cb, Integer start, Integer end, Subquery<Long> offerCount) {
        Predicate predicate = cb.conjunction();
        if (start != null) {
            predicate = cb.and(predicate, cb.greaterThanOrEqualTo(offerCount, start.longValue()));
        }
        if (end != null) {
            predicate = cb.and(predicate, cb.lessThan(offerCount, end.longValue()));
        }
        return predicate;
    }
    public List<Employee> selectEmployeesByOptionalInformation(
            EmployeeInputHandlersDto input) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> employee = query.from(Employee.class);
        List<Predicate> predicates = new ArrayList<>();

        if (input.name() != null && !input.name().isEmpty()) {
            predicates.add(cb.like(employee.get("name"), "%" + input.name() + "%"));
        }
        if (input.lastName() != null && !input.lastName().isEmpty()) {
            predicates.add(cb.like(employee.get("last_name"), "%" + input.lastName() + "%"));
        }
        if (input.email() != null && !input.email().isEmpty()) {
            predicates.add(cb.like(employee.get("email"), "%" + input.email() + "%"));
        }
        if (input.phone() != null && !input.phone().isEmpty()) {
            predicates.add(cb.like(employee.get("phone"), "%" + input.phone() + "%"));
        }
        if (input.minScore() != null ){
            predicates.add(cb.greaterThanOrEqualTo(employee.get("score"), input.minScore()));
        }
        if (input.maxScore() != null){
            predicates.add(cb.lessThanOrEqualTo(employee.get("score"), input.maxScore()));
        }
        List<Predicate> handlerPredicates = new ArrayList<>();
        if (input.handlersName() != null && !input.handlersName().isEmpty()) {
            for (String handlerName : input.handlersName()) {
                Join<Employee, SubHandler> subHandlerJoin = employee.join("subHandlers");
                Join<SubHandler, Handler> handlerJoin = subHandlerJoin.join("handler");
                handlerPredicates.add(cb.like(handlerJoin.get("name"), "%" + handlerName + "%"));
            }
        }

        List<Predicate> subHandlerPredicates = new ArrayList<>();
        if (input.subHandlerName() != null && !input.subHandlerName().isEmpty()) {
            for (String subHandler : input.subHandlerName()) {
                Join<Employee, SubHandler> subHandlerJoin = employee.join("subHandlers");
                subHandlerPredicates.add(cb.like(subHandlerJoin.get("name"), "%" + subHandler + "%"));
            }
        }

        Predicate combineHandler = cb.or(handlerPredicates.toArray(new Predicate[0]));
        Predicate combineSubHandler = cb.or(subHandlerPredicates.toArray(new Predicate[0]));
        if (!combineHandler.getExpressions().isEmpty() || !combineSubHandler.getExpressions().isEmpty()) {
            predicates.add(cb.or(combineHandler, combineSubHandler));
        }

        query.select(employee).where(cb.and(predicates.toArray(new Predicate[0])));
        if (input.ascending()) {
            query.orderBy(cb.desc(employee.get("score")));
        }else {
            query.orderBy(cb.asc(employee.get("score")));
        }
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
