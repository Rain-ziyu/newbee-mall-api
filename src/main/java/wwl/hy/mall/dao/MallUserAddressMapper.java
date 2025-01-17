
package wwl.hy.mall.dao;

import wwl.hy.mall.entity.MallUserAddress;

import java.util.List;

public interface MallUserAddressMapper {
    int deleteByPrimaryKey(Long addressId);

    int insert(MallUserAddress record);

    int insertSelective(MallUserAddress record);

    MallUserAddress selectByPrimaryKey(Long addressId);

    MallUserAddress getMyDefaultAddress(Long userId);

    List<MallUserAddress> findMyAddressList(Long userId);

    int updateByPrimaryKeySelective(MallUserAddress record);

    int updateByPrimaryKey(MallUserAddress record);
}