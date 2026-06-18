package com.subtrack.server.rmi.remote;

import com.subtrack.server.rmi.dto.SubscriptionDto;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface SubscriptionRemoteService extends Remote {
    String create(SubscriptionDto dto) throws RemoteException;
    String update(SubscriptionDto dto) throws RemoteException;
    String delete(Integer id) throws RemoteException;
    List<SubscriptionDto> findByUser(Integer userId) throws RemoteException;
}
