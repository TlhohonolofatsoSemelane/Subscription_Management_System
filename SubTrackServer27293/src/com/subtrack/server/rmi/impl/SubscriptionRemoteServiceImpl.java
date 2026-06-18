package com.subtrack.server.rmi.impl;

import com.subtrack.server.dao.SubscriptionDao;
import com.subtrack.server.dao.SubscriptionDaoImpl;
import com.subtrack.server.dao.UserDao;
import com.subtrack.server.dao.UserDaoImpl;
import com.subtrack.server.model.Subscription;
import com.subtrack.server.model.User;
import com.subtrack.server.rmi.dto.SubscriptionDto;
import com.subtrack.server.rmi.remote.SubscriptionRemoteService;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SubscriptionRemoteServiceImpl extends UnicastRemoteObject implements SubscriptionRemoteService {

    private final SubscriptionDao subscriptionDao = new SubscriptionDaoImpl();
    private final UserDao userDao = new UserDaoImpl();

    public SubscriptionRemoteServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public String create(SubscriptionDto dto) throws RemoteException {
        try {
            if (dto == null) return "SUB_CREATE_FAIL:Request is null";
            if (dto.getUserId() == null) return "SUB_CREATE_FAIL:UserId is required";
            if (dto.getName() == null || dto.getName().trim().isEmpty()) return "SUB_CREATE_FAIL:Name is required";
            if (dto.getAmount() <= 0) return "SUB_CREATE_FAIL:Amount must be > 0";
            if (dto.getBillingCycle() == null || dto.getBillingCycle().trim().isEmpty()) return "SUB_CREATE_FAIL:Billing cycle is required";
            if (dto.getNextPaymentDate() == null || dto.getNextPaymentDate().trim().isEmpty()) return "SUB_CREATE_FAIL:Next payment date is required";

            User user = userDao.findById(dto.getUserId());
            if (user == null) return "SUB_CREATE_FAIL:User not found";

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date nextDate = sdf.parse(dto.getNextPaymentDate().trim());

            Subscription s = new Subscription();
            s.setUser(user);
            s.setCategory(null);
            s.setPaymentMethod(null);
            s.setServiceName(dto.getName().trim());
            s.setAmount(BigDecimal.valueOf(dto.getAmount()));
            s.setBillingCycle(dto.getBillingCycle().trim().toUpperCase());
            s.setNextBillingDate(nextDate);
            s.setStatus("ACTIVE");

            Integer id = subscriptionDao.save(s);

            System.out.println("SERVER CREATE -> subId=" + id + ", userId=" + user.getId() + ", name=" + s.getServiceName());
            return "SUB_CREATE_OK:" + id;
        } catch (Exception e) {
            e.printStackTrace();
            return "SUB_CREATE_FAIL:" + e.getMessage();
        }
    }

    @Override
    public String update(SubscriptionDto dto) throws RemoteException {
        try {
            if (dto == null) return "SUB_UPDATE_FAIL:Request is null";
            if (dto.getId() == null) return "SUB_UPDATE_FAIL:ID is required";
            if (dto.getName() == null || dto.getName().trim().isEmpty()) return "SUB_UPDATE_FAIL:Name is required";
            if (dto.getAmount() <= 0) return "SUB_UPDATE_FAIL:Amount must be > 0";
            if (dto.getBillingCycle() == null || dto.getBillingCycle().trim().isEmpty()) return "SUB_UPDATE_FAIL:Billing cycle is required";
            if (dto.getNextPaymentDate() == null || dto.getNextPaymentDate().trim().isEmpty()) return "SUB_UPDATE_FAIL:Next payment date is required";

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date nextDate = sdf.parse(dto.getNextPaymentDate().trim());

            Subscription s = new Subscription();
            s.setId(dto.getId());
            s.setServiceName(dto.getName().trim());
            s.setAmount(BigDecimal.valueOf(dto.getAmount()));
            s.setBillingCycle(dto.getBillingCycle().trim().toUpperCase());
            s.setNextBillingDate(nextDate);

            subscriptionDao.update(s);
            System.out.println("SERVER UPDATE -> subId=" + dto.getId());
            return "SUB_UPDATE_OK:" + dto.getId();
        } catch (Exception e) {
            e.printStackTrace();
            return "SUB_UPDATE_FAIL:" + e.getMessage();
        }
    }

    @Override
    public String delete(Integer id) throws RemoteException {
        try {
            if (id == null) return "SUB_DELETE_FAIL:ID is required";

            Subscription s = subscriptionDao.findById(id);
            if (s == null) return "SUB_DELETE_FAIL:Subscription not found";

            subscriptionDao.delete(s);
            System.out.println("SERVER DELETE -> subId=" + id);
            return "SUB_DELETE_OK:" + id;
        } catch (Exception e) {
            e.printStackTrace();
            return "SUB_DELETE_FAIL:" + e.getMessage();
        }
    }

    @Override
    public List<SubscriptionDto> findByUser(Integer userId) throws RemoteException {
        List<SubscriptionDto> out = new ArrayList<SubscriptionDto>();
        try {
            if (userId == null) return out;

            List<Subscription> list = subscriptionDao.findByUserId(userId);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            for (Subscription s : list) {
                out.add(new SubscriptionDto(
                        s.getId(),
                        (s.getUser() != null ? s.getUser().getId() : null),
                        s.getServiceName(),
                        (s.getAmount() != null ? s.getAmount().doubleValue() : 0.0),
                        s.getBillingCycle(),
                        (s.getNextBillingDate() != null ? sdf.format(s.getNextBillingDate()) : null)
                ));
            }

            System.out.println("RMI FIND_BY_USER -> userId=" + userId + ", dtoRows=" + out.size());
            return out;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("FIND_BY_USER failed: " + e.getMessage(), e);
        }
    }
}
