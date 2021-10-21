package wwl.hy.mall.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wwl.hy.mall.dao.UserHisMapper;
import wwl.hy.mall.entity.MallUser;
import wwl.hy.mall.entity.UserHistory;
import wwl.hy.mall.service.UserHistoryService;

import java.util.List;
@Service
public class UserHistoryServiceImpl implements UserHistoryService {
    @Autowired
    private UserHisMapper userHisMapper;
    @Override
    public List<UserHistory> getUserHistory(MallUser mallUser) {
        List<UserHistory> userHistories = userHisMapper.selectHis(mallUser.getUserId());
        return userHistories;
    }

    @Override
    public void insertUserHistory(UserHistory userHistory) {
        userHisMapper.insertUserHis(userHistory);
    }
}
