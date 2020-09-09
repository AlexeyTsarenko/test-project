package com.springProject.repositories;

import com.springProject.entities.SimpleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntityRepository extends PagingAndSortingRepository<SimpleEntity, Integer> {
    Page<SimpleEntity> findAllById(Integer userId, Pageable pageable);
    Page<SimpleEntity> findAllByStatus(String status, Pageable pageable);
    Page<SimpleEntity> findAllByIdAndStatus(Integer id,String status, Pageable pageable);
}
