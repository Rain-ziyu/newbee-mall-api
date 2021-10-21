package wwl.hy.mall;

//注意这里引入的test注解对应的包必须是jupiter的
import org.junit.jupiter.api.Test;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import wwl.hy.mall.entity.MallUser;
import wwl.hy.mall.entity.UserHistory;
import wwl.hy.mall.service.UserHistoryService;

@SpringBootTest
public class serviceTest {
    @Autowired
    private UserHistoryService userHistoryService;
    @Test
    public void getHis(){
        MallUser mallUser = new MallUser();
        Long userid = Long.valueOf(1313132131);
        mallUser.setUserId(userid);
        System.out.println(userHistoryService.getUserHistory(mallUser));
    }
    @Test
    public void insertHis(){
        UserHistory userHistory = new UserHistory();
        userHistory.setUserId(Long.valueOf(8));
        userHistory.setHistory("HuaWei");
        userHistoryService.insertUserHistory(userHistory);
        System.out.println("insert success");
    }
}
