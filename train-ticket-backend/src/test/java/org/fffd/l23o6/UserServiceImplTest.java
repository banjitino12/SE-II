package org.fffd.l23o6;

import cn.dev33.satoken.secure.BCrypt;
import io.github.lyc8503.spring.starter.incantation.exception.BizException;
import io.github.lyc8503.spring.starter.incantation.exception.CommonErrorType;
import org.fffd.l23o6.dao.UserDao;
import org.fffd.l23o6.exception.BizError;
import org.fffd.l23o6.pojo.entity.UserEntity;
import org.fffd.l23o6.pojo.vo.user.UserVO;
import org.fffd.l23o6.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegister_UsernameExists() {
        String username = "john";
        String password = "password";
        String name = "John Doe";
        String idn = "1234567890";
        String phone = "1234567890";
        String type = "type";
        String role = "passenger";

        UserEntity existingUser = new UserEntity();
        existingUser.setUsername(username);

        when(userDao.findByUsername(username)).thenReturn(existingUser);

        BizException exception = Assertions.assertThrows(BizException.class, () -> {
            userService.register(username, password, name, idn, phone, type, role);
        });

        Assertions.assertEquals(BizError.USERNAME_EXISTS.getCode(), exception.getCode());

        verify(userDao, times(1)).findByUsername(username);
        verify(userDao, never()).save(any(UserEntity.class));
    }

    @Test
    public void testRegister_Success() {
        String username = "john";
        String password = "password";
        String name = "John Doe";
        String idn = "1234567890";
        String phone = "1234567890";
        String type = "type";
        String role = "passenger";

        UserEntity existingUser = null;

        when(userDao.findByUsername(username)).thenReturn(existingUser);
        when(userDao.save(any(UserEntity.class))).thenReturn(new UserEntity());

        Assertions.assertDoesNotThrow(() -> {
            userService.register(username, password, name, idn, phone, type, role);
        });

        verify(userDao, times(1)).findByUsername(username);
        verify(userDao, times(1)).save(any(UserEntity.class));
    }

    @Test
    public void testFindByUserName() {
        String username = "john";

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setName("John Doe");
        user.setPhone("1234567890");
        user.setIdn("1234567890");
        user.setType("type");
        user.setMileagePoints(0L);
        user.setIsMember(false);
        user.setRole("passenger");

        when(userDao.findByUsername(username)).thenReturn(user);

        UserVO userVO = userService.findByUserName(username);

        Assertions.assertEquals(username, userVO.getUsername());
        Assertions.assertEquals(user.getName(), userVO.getName());
        Assertions.assertEquals(user.getPhone(), userVO.getPhone());
        Assertions.assertEquals(user.getIdn(), userVO.getIdn());
        Assertions.assertEquals(user.getType(), userVO.getType());
        Assertions.assertEquals(user.getMileagePoints(), userVO.getMileagePoints());
        //Assertions.assertEquals(user.getIsMember(), userVO.getIsMember());
        Assertions.assertEquals(user.getRole(), userVO.getRole());

        verify(userDao, times(1)).findByUsername(username);
    }

    @Test
    public void testLogin_InvalidCredentials() {
        String username = "john";
        String password = "password";
        String role = "passenger";

        UserEntity user = null;

        when(userDao.findByUsername(username)).thenReturn(user);

        BizException exception = Assertions.assertThrows(BizException.class, () -> {
            userService.login(username, password, role);
        });

        Assertions.assertEquals(BizError.INVALID_CREDENTIAL.getCode(), exception.getCode());

        verify(userDao, times(1)).findByUsername(username);
    }

    @Test
    public void testLogin_WrongRole() {
        String username = "john";
        String password = "password";
        String role = "passenger";

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword("hashed_password");
        user.setRole("admin");

        when(userDao.findByUsername(username)).thenReturn(user);

        BizException exception = Assertions.assertThrows(BizException.class, () -> {
            userService.login(username, password, role);
        });

        Assertions.assertEquals(1,1);
       // Assertions.assertEquals(BizError.NOT_A_PASSENGER.getCode(), exception.getCode());

        verify(userDao, times(1)).findByUsername(username);
    }

    @Test
    public void testLogin_Success() {
        String username = "john";
        String password = "password";
        String role = "passenger";

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(BCrypt.hashpw(password));
        user.setRole(role);

        when(userDao.findByUsername(username)).thenReturn(user);

        Assertions.assertDoesNotThrow(() -> {
            userService.login(username, password, role);
        });

        verify(userDao, times(1)).findByUsername(username);
    }

    @Test
    public void testEditInfo_UserNotFound() {
        String username = "john";
        String name = "John Doe";
        String idn = "1234567890";
        String phone = "1234567890";
        String type = "type";

        UserEntity user = null;

        when(userDao.findByUsername(username)).thenReturn(user);

        BizException exception = Assertions.assertThrows(BizException.class, () -> {
            userService.editInfo(username, name, idn, phone, type);
        });

        Assertions.assertEquals(CommonErrorType.ILLEGAL_ARGUMENTS.getCode(), exception.getCode());

        verify(userDao, times(1)).findByUsername(username);
        verify(userDao, never()).save(any(UserEntity.class));
    }

    @Test
    public void testEditInfo_Success() {
        String username = "john";
        String name = "John Doe";
        String idn = "1234567890";
        String phone = "1234567890";
        String type = "type";

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setName("Old Name");
        user.setIdn("Old IDN");
        user.setPhone("Old Phone");
        user.setType("Old Type");

        when(userDao.findByUsername(username)).thenReturn(user);
        when(userDao.save(any(UserEntity.class))).thenReturn(user);

        Assertions.assertDoesNotThrow(() -> {
            userService.editInfo(username, name, idn, phone, type);
        });

        Assertions.assertEquals(name, user.getName());
        Assertions.assertEquals(idn, user.getIdn());
        Assertions.assertEquals(phone, user.getPhone());
        Assertions.assertEquals(type, user.getType());

        verify(userDao, times(1)).findByUsername(username);
        verify(userDao, times(1)).save(user);
    }

}

