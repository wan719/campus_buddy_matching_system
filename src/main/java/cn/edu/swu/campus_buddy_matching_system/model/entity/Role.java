package cn.edu.swu.campus_buddy_matching_system.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色实体类
 * 使用了Lombok注解简化代码
 * 包含角色的基本属性和与用户、权限的关联关系
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    /**
     * 角色ID
     * 主键
     */
    private Long id;

    /**
     * 角色名称
     * 不能为空，最大长度为50个字符
     */
    @NotBlank(message = "角色名称不能为空")
    @Size(max = 50, message = "角色名称长度不能超过50个字符")
    private String name;

    /**
     * 角色描述
     * 最大长度为255个字符
     */
    @Size(max = 255, message = "角色描述长度不能超过255个字符")
    private String description;

    /**
     * 创建时间
     * 默认为当前时间
     */
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    // 多对多反向关联
    @JsonIgnore
    @Builder.Default
    private Set<User> users = new HashSet<>();

    @JsonIgnore
    @Builder.Default
    private Set<Permission> permissions = new HashSet<>();

    // ==================== 业务方法 ====================

    /**
     * 添加权限的方法
     * @param permission 要添加的权限对象
     * @return 如果添加成功返回true，如果权限已存在则返回false
     */
    public boolean addPermission(Permission permission) {
        // 如果permissions集合为null，则初始化一个新的HashSet
        if (this.permissions == null) {
            this.permissions = new HashSet<>();
        }
        // 将权限添加到集合中并返回操作结果
        return this.permissions.add(permission);
    }

    /**
     * 移除指定权限
     * @param permission 要移除的权限对象
     * @return 如果成功移除返回true，如果权限集合为null或移除失败返回false
     */
    public boolean removePermission(Permission permission) {
        // 检查权限集合是否为null
        if (this.permissions == null) {
            
            return false;
        }
        // 从权限集合中移除指定权限并返回操作结果
        return this.permissions.remove(permission);
    }

    public boolean hasPermission(String permissionName) {
        if (this.permissions == null) {
            return false;
        }
        return this.permissions.stream()
                .anyMatch(p -> p.getName().equals(permissionName));
    }

    public Set<String> getPermissionNames() {
        if (this.permissions == null) {
            return Set.of();
        }
        return this.permissions.stream()
                .map(Permission::getName)
                .collect(Collectors.toSet());
    }
}