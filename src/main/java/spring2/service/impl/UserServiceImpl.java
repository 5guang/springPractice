package spring2.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import spring2.dao.UserDOMapper;
import spring2.dao.UserPasswordDOMapper;
import spring2.dataobject.UserDO;
import spring2.dataobject.UserPasswordDO;
import spring2.error.BusinessException;
import spring2.error.EmBusinessError;
import spring2.response.CommonReturnType;
import spring2.service.UserService;
import spring2.service.model.UserModel;
import spring2.validator.ValidationResult;
import spring2.validator.ValidatorImpl;

/**
 * @author 张文光
 * @version 1.0
 * @Date 2018/12/16 0016 11:22
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDOMapper userDOMapper;

    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;

    @Autowired
    private ValidatorImpl validator;

    @Transactional
    @Override
    public void register(UserModel userModel) throws BusinessException {
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        ValidationResult result = validator.validate(userModel);
        if (result.isHasErrors()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, result.getErrMsg());
        }

        UserDO userDO = convertFromModel(userModel);

        /**
         * 实现model到dataobject方法
         */
        try {
            userDOMapper.insertSelective(userDO);
        } catch (DuplicateKeyException e) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "手机号已存在");
        }
        userModel.setId(userDO.getId());
        UserPasswordDO userPasswordDO = convertPasswordFromModel(userModel);
        userPasswordDOMapper.insertSelective(userPasswordDO);

    }

    @Override
    public UserModel validateLogin(String telphone, String encrptPassword) throws BusinessException {
        /**
         * 通过用户的手机获取用户信息
         */
        UserDO userDO = userDOMapper.selectByTelphone(telphone);
        if (userDO == null) {
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        UserModel userModel = convertFromDataObject(userDO, userPasswordDO);

        /**
         * 比对用户信息加密的密码是否和传输进来的密码相匹配
         */
        if (!StringUtils.equals(encrptPassword, userPasswordDO.getEncrptPassword())) {
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        return userModel;

    }

    private UserDO convertFromModel(UserModel userModel) throws BusinessException {
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userModel, userDO);
        return userDO;
    }

    private UserPasswordDO convertPasswordFromModel(UserModel userModel) throws BusinessException {
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        UserPasswordDO userPasswordDO = new UserPasswordDO();
        userPasswordDO.setEncrptPassword(userModel.getEncrptPassword());
        userPasswordDO.setUserId(userModel.getId());
        return userPasswordDO;
    }


    @Override
    public UserModel getUserById(Integer id) {
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);

        if (userDO == null) {
            return null;
        }
        // 通过用户id获取对应的用户加密密码信息
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());

        return convertFromDataObject(userDO, userPasswordDO);

    }


    private UserModel convertFromDataObject(UserDO userDO, UserPasswordDO userPasswordDO) {
        if(userDO == null) {
            return null;
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDO,userModel);
        if(userPasswordDO != null) {
            userModel.setEncrptPassword(userPasswordDO.getEncrptPassword());
        }
        return userModel;
    }
}
