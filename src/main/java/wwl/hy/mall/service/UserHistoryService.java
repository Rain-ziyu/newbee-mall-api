package wwl.hy.mall.service;

import wwl.hy.mall.entity.MallUser;
import wwl.hy.mall.entity.UserHistory;

import java.util.List;

public interface UserHistoryService {
    List<UserHistory> getUserHistory(MallUser mallUser);
    void insertUserHistory(UserHistory userHistory);
}
