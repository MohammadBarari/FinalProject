package org.example.repository.subHandler;

import org.example.domain.SubHandler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubHandlerRepository extends JpaRepository<SubHandler, Integer> {

    SubHandler findSubHandlerById(Integer id);

    @Query(value = """
        select * from sub_handler
        where handler_id= ?1
""",nativeQuery = true)
    List<SubHandler> selectBySameHandler(Integer handlerId);

    @Query(value = """
      select DISTINCT * from sub_handler join  employee_sub_handlers esh on sub_handler.id = esh.sub_handlers_id
       where employee_id = ?1
""",nativeQuery = true)
    List<SubHandler> selectByEmployeeId(Integer employeeId);

    @Query(value = """
            select sub_handler.* from sub_handler where name = ?1\s
""",nativeQuery = true)
    SubHandler selectByName(String name);
}
