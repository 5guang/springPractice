package spring2.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import spring2.error.BusinessException;
import spring2.error.EmBusinessError;
import spring2.request.usermodel.OptInfo;
import spring2.request.usermodel.LoginInfo;
import spring2.response.CommonReturnType;
import spring2.request.usermodel.RegisterInfo;
import spring2.service.UserService;
import spring2.service.model.UserModel;
import spring2.controller.viewobject.UserVO;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * @author 张文光
 * @version 1.0
 * @Date 2018/12/16 0016 11:16
 */
@RestController
@RequestMapping("/api/user")
@CrossOrigin(allowCredentials="true", allowedHeaders="*")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    /**
     * 用户登录接口
     * @return
     * @throws BusinessException
     */
//    @RequestMapping(value = "/login", method = RequestMethod.POST,consumes = {CONTENT_TYPE_FORMED})
//    @ResponseBody
//    public CommonReturnType login(@RequestParam(name="telphone") String telphone,
//                                  @RequestParam(name="password") String password
//    ) throws BusinessException, NoSuchAlgorithmException {
//
//        /**
//         * 入参校验
//         */
//        if (StringUtils.isEmpty(telphone) || StringUtils.isEmpty(password)) {
//            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
//        }
//        /**
//         * 用户登录服务，用来教研用户登录是否合法
//         */
//        UserModel userModel = userService.validateLogin(telphone, this.EncodeByMd5(password));
//
//        /**
//         * 将登录凭证加入到用户登录成功的session内
//         */
//        this.httpServletRequest.getSession().setAttribute("IS_LOGIN", true);
//        this.httpServletRequest.getSession().setAttribute("LOGIN_USER", userModel);
//
//        return CommonReturnType.create(null);
//    }
    @PostMapping(value = "/login",consumes = {CONTENT_TYPE_JSON})
    public CommonReturnType login(@RequestBody LoginInfo loginInfo) throws BusinessException, NoSuchAlgorithmException {

        /**
         * 入参校验
         */
        if (StringUtils.isEmpty(loginInfo.getTelphone()) || StringUtils.isEmpty(loginInfo.getPassword())) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        /**
         * 用户登录服务，用来教研用户登录是否合法
         */
        UserModel userModel = userService.validateLogin(loginInfo.getTelphone(), this.EncodeByMd5(loginInfo.getPassword()));

        /**
         * 将登录凭证加入到用户登录成功的session内
         */
        this.httpServletRequest.getSession().setAttribute("IS_LOGIN", true);
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER", userModel);

        return CommonReturnType.create(null);
    }

    /**
     * 用户注册接口
     * @return
     */
    @PostMapping(value = "/register", consumes = {CONTENT_TYPE_JSON})
    public CommonReturnType register(@RequestBody RegisterInfo registerInfo) throws BusinessException, NoSuchAlgorithmException {
        /**
         * 验证手机号与optcode是否相等
         */
        String inSessionOptCode = (String) this.httpServletRequest.getSession().getAttribute(registerInfo.getTelphone());
        if (!com.alibaba.druid.util.StringUtils.equals(registerInfo.getOptCode(), inSessionOptCode)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        UserModel userModel = new UserModel();
        userModel.setName(registerInfo.getName());
        userModel.setGender(new Byte(String.valueOf(registerInfo.getGender().intValue())));
        userModel.setAge(registerInfo.getAge());
        userModel.setTelphone(registerInfo.getTelphone());
        userModel.setRegisterMode("byphone");
        userModel.setEncrptPassword(this.EncodeByMd5(registerInfo.getPassword()));

        userService.register(userModel);
        return CommonReturnType.create(null);
    }
    public String EncodeByMd5(String str) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();

        String newstr = null;
        try {
            newstr = base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return newstr;
    }
    /**
     * 用户获取otp短信接口
     * @return
     */
    @PostMapping(value = "/getotp", consumes = {CONTENT_TYPE_JSON})
    public CommonReturnType getOtp(@RequestBody OptInfo optInfo) {
        // 需要按照一定的规则生成otp验证码
        Random random = new Random();
        int randomInt = random.nextInt(99999);
        randomInt += 10000;
        String otpCode = String.valueOf(randomInt);
        // 将otp验证码同对应用户的手机号关联,使用httpsession
        httpServletRequest.getSession().setAttribute(optInfo.getTelphone(),otpCode);
        // 将otp验证码
        System.out.println("telphone=" + optInfo.getTelphone() + "&otpCode="+otpCode);
        return CommonReturnType.create(null);
    }



    @RequestMapping("/getUser")
    public CommonReturnType getUser(@RequestParam(name="id") Integer id) throws BusinessException {
        //调用service服务获取对应id的用户对象并返回给前端
        UserModel userModel = userService.getUserById(id);

        if (userModel == null) {
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        // 将核心领域模型用户对象转化为可供UI使用的viewobject
        UserVO userVO = convertFromModel(userModel);
        return CommonReturnType.create(userVO);
    }

    private UserVO convertFromModel(UserModel userModel) {
        if (userModel == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel, userVO);
        return userVO;
    }

}
