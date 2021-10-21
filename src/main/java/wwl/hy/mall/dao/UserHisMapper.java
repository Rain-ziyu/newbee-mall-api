package wwl.hy.mall.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import wwl.hy.mall.entity.UserHistory;

import java.util.List;
@Mapper
public interface UserHisMapper extends BaseMapper {
        List<UserHistory> selectHis(Long userid);
        boolean insertUserHis(UserHistory userHistory);
}
