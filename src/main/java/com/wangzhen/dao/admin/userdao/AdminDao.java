package com.wangzhen.dao.admin.userdao;
import com.wangzhen.models.users.Admin;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * @Author wangzhen
 * @Description 管理员数据库操作
 * @CreateDate 2020/1/12 18:00
 */
@Mapper
public interface AdminDao {
    /**
     * @Description 增加管理员账户
     * @date 2020/1/12 21:56
     * @param uuid
     * @param name
     * @param account
     * @param code
     * @param roles
     * @return boolean
     */
    @Insert("insert into admin(uuid,name,account,code,roles) values (#{uuid},#{name},#{account},#{code},#{roles})")
    public boolean insertAdmin(@Param("uuid") String uuid, @Param("name") String name, @Param("account") String account, @Param("code") String code, @Param("roles") String roles);

//    @Delete("delete from admin where account = #{account}")
//    public boolean deleteAdmin(@Param("account") String account);

    /**
     * @Description 删除账户但实际上不删除，只是把登录权限更改
     * @date 2020/1/12 21:55
     * @param account
     * @return boolean
     */
    @Update("update admin set enabled = '0' where account = #{account}")
    public boolean deleteAdmin(@Param("account") String account);

    /**
     * @Description 支持账户、邮箱双登录
     * @date 2020/1/12 21:54
     * @param account
     * @return com.wangzhen.models.users.Admin
     */
    @Select("select * from admin where (account = #{account} or email = #{account}) and enabled = '1'")
    public Admin selectAdminByAccount(@Param("account") String account);

    /**
     * @Description 查找所有管理员账户，但是不能为失效账户
     * @date 2020/1/12 22:01
     * @param
     * @return java.util.List<com.wangzhen.models.users.Admin>
     */
    @Select("select * from admin where enabled = '1'")
    public List<Admin> selectAllAdmin();

    /**
     * @Description 修改管理员密码
     * @date 2020/1/12 22:09
     * @param account
     * @param code
     * @return boolean
     */
    @Update("update admin set code = #{code} where account = #{account}")
    public boolean updateAdminCode(@Param("account") String account,@Param("code") String code);


    /**
     * @Description 修改管理员账户信息
     * @date 2020/1/12 22:16
     * @param account
     * @param name
     * @param age
     * @param email
     * @param phone
     * @param acccountNonExpired
     * @param accountNonLocked
     * @param credentialsNonExpired
     * @param enabled
     * @return boolean
     */
    @Update("update admin set name = #{name}, age=#{age}, email=#{email}, phone=#{phone}, acccountNonExpired=#{acccountNonExpired}, accountNonLocked=#{accountNonLocked}, credentialsNonExpired=#{credentialsNonExpired}, enabled=#{enabled} where account=#{account}")
    public boolean updateAdminInfo(@Param("account") String account,
                                   @Param("name") String name,
                                   @Param("age") Integer age,
                                   @Param("email") String email,
                                   @Param("phone") String phone,
                                   @Param("acccountNonExpired") boolean acccountNonExpired,
                                   @Param("accountNonLocked") boolean accountNonLocked,
                                   @Param("credentialsNonExpired") boolean credentialsNonExpired,
                                   @Param("enabled") boolean enabled);

    @Update("update admin set account = #{account} where account = #{account}")
    public boolean updateAdminAccount(@Param("account") String account);
}
