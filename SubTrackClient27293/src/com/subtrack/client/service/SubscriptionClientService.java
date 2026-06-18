package com.subtrack.client.service;

import com.subtrack.server.rmi.dto.SubscriptionDto;
import java.util.List;

public interface SubscriptionClientService {
    String create(SubscriptionDto dto);
    String update(SubscriptionDto dto);
    String delete(Integer id);
    List<SubscriptionDto> findByUser(Integer userId);
}
