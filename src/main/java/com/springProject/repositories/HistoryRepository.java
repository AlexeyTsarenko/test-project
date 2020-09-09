package com.springProject.repositories;

import com.springProject.entities.HistoryEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends PagingAndSortingRepository<HistoryEntity, Integer> {
}
