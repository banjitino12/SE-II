package org.fffd.l23o6.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import io.github.lyc8503.spring.starter.incantation.exception.BizException;
import io.github.lyc8503.spring.starter.incantation.exception.CommonErrorType;
import lombok.RequiredArgsConstructor;
import org.fffd.l23o6.dao.UserDao;
import org.fffd.l23o6.exception.BizError;
import org.fffd.l23o6.pojo.entity.UserEntity;
import org.fffd.l23o6.pojo.vo.user.UserVO;
import org.fffd.l23o6.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    @Override
    public void register(String username, String password, String name, String idn, String phone, String type, String role) {
        UserEntity user = userDao.findByUsername(username);

        if (user != null) {
            throw new BizException(BizError.USERNAME_EXISTS);
        }

//        if(role.equals("passenger")) {
            userDao.save(UserEntity.builder().username(username).password(BCrypt.hashpw(password))
                    .name(name).idn(idn).phone(phone).type(type).mileagePoints(0L).isMember(false).role(role).build());
//        }
//        else {
//            userDao.save(UserEntity.builder().username(username).password(BCrypt.hashpw(password))
//                    .name(name).idn(idn).phone(phone).type(type).role(role).build());
//        }
    }

    @Override
    public UserVO findByUserName(String username) {
        UserEntity user=userDao.findByUsername(username);
        return UserVO.builder()
                .username(user.getUsername())
                .name(user.getName())
                .phone(user.getPhone())
                .idn(user.getIdn())
                .type(user.getType())
                .mileagePoints(user.getMileagePoints())
                .isMember(user.getIsMember())
                .role(user.getRole())
                .build();
    }

    @Override
    public void login(String username, String password, String role) {
        UserEntity user = userDao.findByUsername(username);
        if (user == null || !BCrypt.checkpw(password, user.getPassword())) {
            throw new BizException(BizError.INVALID_CREDENTIAL);
        }

        if(!role.equals(user.getRole())){
            if(role.equals("passenger")) throw new BizException(BizError.NOT_A_PASSENGER);
            else if (role.equals("admin")) throw new BizException(BizError.NOT_AN_ADMINISTRATOR);
        }

    }

    @Override
    public void editInfo(String username, String name, String idn, String phone, String type){
        UserEntity user = userDao.findByUsername(username);
        if(user == null){
            throw new BizException(CommonErrorType.ILLEGAL_ARGUMENTS, "用户不存在");
        }
        userDao.save(user.setIdn(idn).setName(name).setPhone(phone).setType(type));
    }


    @Override
    public void getMembership(String username,String vippassword){
        UserEntity user = userDao.findByUsername(username);
        if(user == null){
            throw new BizException(CommonErrorType.ILLEGAL_ARGUMENTS, "用户不存在");
        }
        userDao.save(user.setIsMember(true).setVippassword(vippassword));
    }

}