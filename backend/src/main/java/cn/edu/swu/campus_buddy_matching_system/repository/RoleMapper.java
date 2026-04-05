package cn.edu.swu.campus_buddy_matching_system.repository;

import cn.edu.swu.campus_buddy_matching_system.model.entity.Role;
import org.apache.ibatis.annotations.*;

import java.util.Set;

@Mapper
public interface RoleMapper {

    // ==================== 注解方式实现 ====================

    @Insert("INSERT INTO roles (name, description) VALUES (#{name}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Role role);

    @Select("SELECT * FROM roles WHERE id = #{id}")
    @Results(id = "roleResult", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "description", column = "description"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "updatedAt", column = "updated_at"),
            @Result(property = "permissions", column = "id",
                    many = @Many(select = "cn.edu.swu.campus_buddy_matching_system.repository.PermissionMapper.findPermissionsByRoleId"))
    })
    Role findById(@Param("id") Long id);

    @Select("SELECT * FROM roles WHERE name = #{name}")
    @ResultMap("roleResult")
    Role findByName(@Param("name") String name);

    @Insert("INSERT INTO user_role (user_id, role_id) VALUES (#{userId}, #{roleId})")
    int assignRoleToUser(@Param("userId") Long userId, @Param("roleId") Long roleId);

    @Delete("DELETE FROM user_role WHERE user_id = #{userId} AND role_id = #{roleId}")
    int removeRoleFromUser(@Param("userId") Long userId, @Param("roleId") Long roleId);

    // ==================== XML方式实现 ====================

    Set<Role> findRolesByUserId(@Param("userId") Long userId);
}