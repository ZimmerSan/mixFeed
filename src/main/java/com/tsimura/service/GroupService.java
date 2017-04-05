package com.tsimura.service;

import com.tsimura.domain.Group;

import java.util.List;
import java.util.Optional;

public interface GroupService {

    Group save(com.vk.api.sdk.objects.groups.Group group);

    Optional<Group> findById(String id);

    Optional<Group> findByScreenName(String screenName);

    List<Group> findAll();
}
