package org.example.repository.offer;
import org.example.domain.Employee;
import org.example.domain.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OfferRepository extends JpaRepository<Offer, Integer> {

    Offer findOfferById(int id);

    @Query("SELECT o FROM Offer o WHERE o.orders.id = ?1 ")
    List<Offer> findAllForOrder(int orderId);

    @Query(value = """
        SELECT * 
        FROM offer o
        JOIN orders ord ON ord.id = o.order_id
        WHERE ord.id = ?1 AND o.accepted = true
        """, nativeQuery = true)
    Offer selectAcceptedOfferInOrder( Integer orderId);

    @Query(value = """
   select employee.* from customtestschema.employee join customtestschema.offer o on employee.id = o.employee_id
where o.id = ?1
""",nativeQuery = true)
    Employee findEmployeeByOfferId(Integer id);
}
