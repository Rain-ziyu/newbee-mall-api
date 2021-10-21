package wwl.hy.mall.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
@Data
@NoArgsConstructor
@ToString
public class UserHistory {
    private Long userId;
    private String history;
}
