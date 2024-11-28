package org.example.repository.order;

import org.example.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Integer> {

    Orders findOrdersById(int orderId);

    @Query(value = """
        select * from orders
        where employee_id = ?1
""",nativeQuery = true)
    List<Orders> findOrdersByEmployee(Integer employeeId);

    @Query(value = """
        select * from orders
        where customer_id = ?1
""",nativeQuery = true)
    List<Orders> findOrdersByCustomer(Integer customerId);

    @Query(value = """
        select o.* from orders as o join   sub_handler sh on sh.id = o.sub_handler_id
where sh.id = ?1 and order_state ='UNDER_CHOOSING_EMPLOYEE' or order_state = 'WAITING_FOR_EMPLOYEE_OFFER'
""",nativeQuery = true)
    List<Orders> selectOrdersBySubHandlerId(Integer subHandlerId);

    @Query(value = """
            select orders.* from customtestschema.orders join customtestschema.employee e on e.id = orders.employee_id
        where e.id = ?1
""",nativeQuery = true)
    List<Orders> selectGotOrdersByEmployeeId(Integer employeeId);

    @Query(value = """
            select orders.* from orders join employee e on e.id = orders.employee_id
    where e.id = ?1 and order_state = 'PAID'
    """,nativeQuery = true)
    List<Orders> selectPaidOrdersForEmployee(Integer employeeId);

    @Query(value = """
       select orders.* from orders join customer e on e.id = orders.customer_id
    where e.id = ?1 and order_state = 'PAID'
""",nativeQuery = true)
    List<Orders> selectPaidOrdersForCustomer(Integer customerId);

    Orders findOrdersById(Integer orderId);
}
