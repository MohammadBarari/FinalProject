package org.example.repository.subHandler;

import org.example.domain.SubHandler;

import java.util.List;

public interface SubHandlerRepository {
    void save(SubHandler subHandler);
    void delete(Integer id);
    void update(SubHandler subHandler);
    SubHandler selectById(Integer id);
    List<SubHandler> selectBySameHandler(Integer handlerId);
    List<SubHandler> selectByEmployeeId(Integer employeeId);
}
