package iuh.fit.trainingsystembackend;

import iuh.fit.trainingsystembackend.bean.UserBean;
import iuh.fit.trainingsystembackend.enums.SystemRole;
import iuh.fit.trainingsystembackend.exceptions.ValidationException;
import iuh.fit.trainingsystembackend.mail.MailService;
import iuh.fit.trainingsystembackend.model.*;
import iuh.fit.trainingsystembackend.repository.*;
import iuh.fit.trainingsystembackend.utils.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
class TrainingSystemBackendApplicationTests {

    @Autowired
    private MailService mailService;
    @Test
    void contextLoads() {
        mailService.sendEmail("nmthuan0712@gmail.com",
                "Đăng ký thành công tài khoản sinh viên", "Bạn đã đăng ký thành công tài khoản Email cho sinh viên.\n\nThông tin đăng ký như sau:\n" +
                          "- Email: "+ "studentEmail" +"\n" +
                          "- MSSV: "+ "studentCode" +"\n" +
                          "- Mật khẩu (cho lần đăng nhập đầu tiên): 1111\n" +
                          "- Số ĐT và Email lần đầu đăng ký để khôi phục mật khẩu: " + "0817808173" + " - " + "nmthuan0712@gmail.com" );
    }
}
