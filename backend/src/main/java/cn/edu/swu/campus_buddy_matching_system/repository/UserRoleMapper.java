package cn.edu.swu.campus_buddy_matching_system.repository;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserRoleMapper {

    @Insert("INSERT INTO user_role (user_id, role_id) VALUES (#{userId}, #{roleId})")
    int insert(@Param("userId") Long userId, @Param("roleId") Long roleId);

    @Delete("DELETE FROM user_role WHERE user_id = #{userId}")
    int deleteByUserId(@Param("userId") Long userId);

    @Delete("DELETE FROM user_role WHERE role_id = #{roleId}")
    int deleteByRoleId(@Param("roleId") Long roleId);

    @Insert("<script>" +
            "INSERT INTO user_role (user_id, role_id) VALUES " +
            "<foreach collection='roleIds' item='roleId' separator=','>" +
            "(#{userId}, #{roleId})" +
            "</foreach>" +
            "</script>")
    int batchInsert(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);

    @Delete("DELETE FROM user_role WHERE user_id = #{userId} AND role_id = #{roleId}")
    int deleteByUserAndRole(@Param("userId") Long userId, @Param("roleId") Long roleId);
}