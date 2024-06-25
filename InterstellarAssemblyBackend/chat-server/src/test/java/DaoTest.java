import com.qly.mallchat.common.MallchatCustomApplication;
import com.qly.mallchat.common.common.utils.JwtUtils;
import com.qly.mallchat.common.user.service.LoginService;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.checkerframework.checker.units.qual.A;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Description:
 * Author: <a href="https://github.com/zongzibinbin">abin</a>
 * Date: 2023-08-27
 */

@SpringBootTest(classes = {MallchatCustomApplication.class})
@RunWith(SpringRunner.class)
@Slf4j
public class DaoTest {
    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void redis(){
        RLock lock = redissonClient.getLock("123");
        lock.lock();
    }



}
