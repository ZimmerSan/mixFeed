package com.tsimura.repository;

import com.tsimura.domain.Photo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PhotoRepository extends PagingAndSortingRepository<Photo, Integer> {

    List<Photo> findByUserId(int userId);

    Long countByUserId(int userId);

    @Query("select p.id from Photo p where p.userId = :userId")
    List<Integer> findIdByUserId(@Param("userId") int userId);

}
