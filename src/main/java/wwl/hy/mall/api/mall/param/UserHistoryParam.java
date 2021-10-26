package wwl.hy.mall.api.mall.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserHistoryParam {
    @ApiModelProperty("用户搜索历史")
    private String history;
}
