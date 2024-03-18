package org.fffd.l23o6;

import org.fffd.l23o6.dao.UserDao;
import org.fffd.l23o6.pojo.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserDaoTest {

    @Mock
    private UserDao userDao;


    public UserDaoTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByUsername() {
        // 准备测试数据
        String username = "testuser";
        UserEntity user = new UserEntity();
        // 设置user的属性

        // 模拟userDao的行为
        when(userDao.findByUsername(username)).thenReturn(user);

        // 调用userDao的方法
        UserEntity result = userDao.findByUsername(username);

        // 验证返回结果是否符合预期
        assertEquals(user, result);

        // 验证userDao的方法是否被调用
        verify(userDao, times(1)).findByUsername(username);
    }


}
