package com.tsimura.repository;

import com.tsimura.domain.Group;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface GroupRepository extends PagingAndSortingRepository<Group, String> {

    Group findByScreenName(String screenName);

}
