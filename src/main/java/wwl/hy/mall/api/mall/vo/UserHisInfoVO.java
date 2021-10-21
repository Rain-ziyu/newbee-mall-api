package wwl.hy.mall.api.mall.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import wwl.hy.mall.entity.UserHistory;

import java.io.Serializable;
import java.util.List;

@Data
public class UserHisInfoVO implements Serializable {
    @ApiModelProperty("用户的历史搜索记录")
    private List<UserHistory> userHistories;
}
