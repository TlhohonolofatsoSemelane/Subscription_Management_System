package com.subtrack.client.service.impl;

import com.subtrack.client.service.SubscriptionClientService;
import com.subtrack.server.rmi.dto.SubscriptionDto;
import com.subtrack.server.rmi.remote.SubscriptionRemoteService;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionClientServiceImpl implements SubscriptionClientService {

    private final SubscriptionRemoteService subscriptionRemoteService;

    public SubscriptionClientServiceImpl(SubscriptionRemoteService subscriptionRemoteService) {
        this.subscriptionRemoteService = subscriptionRemoteService;
    }

    @Override
    public String create(SubscriptionDto dto) {
        try {
            if (dto == null) return "SUB_CREATE_FAIL:Request is null";
            return subscriptionRemoteService.create(dto);
        } catch (Exception e) {
            return "SUB_CREATE_FAIL:" + e.getMessage();
        }
    }

    @Override
    public String update(SubscriptionDto dto) {
        try {
            if (dto == null) return "SUB_UPDATE_FAIL:Request is null";
            return subscriptionRemoteService.update(dto);
        } catch (Exception e) {
            return "SUB_UPDATE_FAIL:" + e.getMessage();
        }
    }

    @Override
    public String delete(Integer id) {
        try {
            if (id == null) return "SUB_DELETE_FAIL:ID is null";
            return subscriptionRemoteService.delete(id);
        } catch (Exception e) {
            return "SUB_DELETE_FAIL:" + e.getMessage();
        }
    }

    @Override
    public List<SubscriptionDto> findByUser(Integer userId) {
        try {
            if (userId == null) return new ArrayList<>();
            return subscriptionRemoteService.findByUser(userId);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
