package com.tsimura.service.impl;

import com.tsimura.domain.Group;
import com.tsimura.repository.GroupRepository;
import com.tsimura.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;

    @Autowired
    public GroupServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public Group save(com.vk.api.sdk.objects.groups.Group vkGroup) {
        Group group = new Group(
                vkGroup.getId(),
                vkGroup.getName(),
                vkGroup.getScreenName(),
                vkGroup.getIsClosed().getValue(),
                vkGroup.getPhoto200()
        );
        return groupRepository.save(group);
    }

    @Override
    public Optional<Group> findById(String id) {
        return Optional.ofNullable(groupRepository.findOne(id));
    }

    @Override
    public Optional<Group> findByScreenName(String screenName) {
        return Optional.ofNullable(groupRepository.findByScreenName(screenName));
    }

    @Override
    public List<Group> findAll() {
        return (List<Group>) groupRepository.findAll();
    }
}
