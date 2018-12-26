package spring2;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring2.dao.UserDOMapper;
import spring2.dataobject.UserDO;

/**
 * Hello world!
 *
 */
@RestController
@SpringBootApplication(scanBasePackages = "spring2")
@MapperScan("spring2.dao")
public class App {
//
//    @Autowired
//    private UserDOMapper userDOMapper;
//
//    @RequestMapping("/")
//    public String hello() {
//        UserDO userDO = userDOMapper.selectByPrimaryKey(1);
//        if (userDO == null) {
//            return "用户对象不存在";
//        } else {
//            return userDO.getName();
//        }
//
//    }

    public static void main( String[] args ) {
        System.out.println( "Hello World!" );
        SpringApplication.run(App.class, args);
    }
}
