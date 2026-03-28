package cn.edu.swu.campus_buddy_matching_system.repository;

import cn.edu.swu.campus_buddy_matching_system.model.entity.Role;
import org.apache.ibatis.annotations.*;

import java.util.Set;

@Mapper
public interface RoleMapper {

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
    Role findById(Long id);

    @Select("SELECT * FROM roles WHERE name = #{name}")
    @ResultMap("roleResult")
    Role findByName(String name);

    @Select("SELECT r.* FROM roles r " +
            "JOIN user_role ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = #{userId}")
    @ResultMap("roleResult")
    Set<Role> findRolesByUserId(Long userId);

    @Insert("INSERT INTO roles (name, description) VALUES (#{name}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Role role);

    @Insert("INSERT INTO user_role (user_id, role_id) VALUES (#{userId}, #{roleId})")
    int assignRoleToUser(@Param("userId") Long userId, @Param("roleId") Long roleId);

    @Delete("DELETE FROM user_role WHERE user_id = #{userId} AND role_id = #{roleId}")
    int removeRoleFromUser(@Param("userId") Long userId, @Param("roleId") Long roleId);
}