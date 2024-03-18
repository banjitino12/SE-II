package org.fffd.l23o6.service;

import org.fffd.l23o6.pojo.entity.UserEntity;
import org.fffd.l23o6.pojo.vo.user.UserVO;

public interface UserService {
    void login(String username, String password, String role);
    void register(String username, String password, String name, String idn, String phone, String type,String role);

    UserVO findByUserName(String username);
    void editInfo(String username, String name, String idn, String phone, String type);

    void getMembership(String username,String vippassword);
}