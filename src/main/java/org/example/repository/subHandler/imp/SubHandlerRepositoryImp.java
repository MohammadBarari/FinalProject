package org.example.repository.subHandler.imp;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.example.domain.Handler;
import org.example.domain.SubHandler;
import org.example.repository.subHandler.SubHandlerRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class SubHandlerRepositoryImp implements SubHandlerRepository {
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public void save(SubHandler subHandler) {
            entityManager.persist(subHandler);
    }

    @Override
    public void delete(Integer id) {
            SubHandler subHandler = entityManager.find(SubHandler.class, id);
            entityManager.remove(subHandler);
    }

    @Override
    public void update(SubHandler subHandler) {
            entityManager.merge(subHandler);
    }

    @Override
    public SubHandler selectById(Integer id) {
        try {
             return entityManager.find(SubHandler.class, id);
        }catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<SubHandler> selectBySameHandler(Integer handlerId) {
        try {
            Query query = entityManager.createNativeQuery("""
        select * from subhandler
        where handler_id= ?
""", SubHandler.class);

            query.setParameter(1, handlerId);
            return query.getResultList();
        }catch (Exception e) {
            return null;
        }
        }

    @Override
    public List<SubHandler> selectByEmployeeId(Integer employeeId) {
        try {
            Query query = entityManager.createNativeQuery("""
       select DISTINCT * from sub_handler join employee_sub_handlers esh on sub_handler.id = esh.sub_handlers_id
       where employee_id = ?
""",SubHandler.class);
            query.setParameter(1, employeeId);
            return query.getResultList();
        }catch (Exception e) {
            return null;
        }
    }
}
