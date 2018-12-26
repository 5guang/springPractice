package spring2.service;

import spring2.error.BusinessException;
import spring2.service.model.UserModel;

/**
 * @author 张文光
 * @version 1.0
 * @Date 2018/12/16 0016 11:21
 */
public interface UserService {
    UserModel getUserById(Integer id);

    void register(UserModel userModel) throws BusinessException;

    /**
     *
     * @param telphone 用户注册手机
     * @param encrptPassword 加密密码
     * @throws BusinessException
     */
    UserModel validateLogin(String telphone, String encrptPassword) throws BusinessException;
}
