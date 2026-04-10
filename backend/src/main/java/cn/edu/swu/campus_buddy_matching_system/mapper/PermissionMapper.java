package cn.edu.swu.campus_buddy_matching_system.mapper;

import cn.edu.swu.campus_buddy_matching_system.model.entity.Permission;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Set;

@Mapper
public interface PermissionMapper {

    // ==================== 注解方式实现 ====================

    @Insert("""
        INSERT INTO permissions (name, description, resource, action)
        VALUES (#{name}, #{description}, #{resource}, #{action})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Permission permission);

    @Select("SELECT * FROM permissions WHERE id = #{id}")
    @Results(id = "permissionResult", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "description", column = "description"),
            @Result(property = "resource", column = "resource"),
            @Result(property = "action", column = "action"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "updatedAt", column = "updated_at")
    })
    Permission findById(@Param("id") Long id);

    @Select("SELECT * FROM permissions WHERE name = #{name}")
    @ResultMap("permissionResult")
    Permission findByName(@Param("name") String name);

    @Select("SELECT * FROM permissions ORDER BY resource, action")
    @ResultMap("permissionResult")
    List<Permission> selectAll();

    @Insert("INSERT INTO role_permission (role_id, permission_id) VALUES (#{roleId}, #{permissionId})")
    int assignPermissionToRole(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);

    @Delete("DELETE FROM role_permission WHERE role_id = #{roleId} AND permission_id = #{permissionId}")
    int removePermissionFromRole(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);

    // ==================== XML方式实现 ====================

    Set<Permission> findPermissionsByRoleId(@Param("roleId") Long roleId);

    List<Permission> selectByUser(@Param("userId") Long userId);

    List<Permission> selectByRole(@Param("roleId") Long roleId);
}