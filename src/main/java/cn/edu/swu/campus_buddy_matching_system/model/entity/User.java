package cn.edu.swu.campus_buddy_matching_system.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户实体类（整合 RBAC 权限模型）
 * 包含用户基本信息以及与角色的多对多关系
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;

    @NotBlank(message = "学号不能为空")
    @Size(min = 8, max = 20, message = "学号长度必须在8-20个字符之间")
    private String studentId;

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度必须在3-50个字符之间")
    private String username;

    @JsonIgnore
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 100, message = "密码长度必须在6-100个字符之间")
    private String password;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    @NotBlank(message = "昵称不能为空")
    @Size(max = 50, message = "昵称长度不能超过50个字符")
    private String nickname;

    private String avatarUrl;
    private String college;
    private Integer grade;
    private String tags; // JSON 字符串，后续可改为 List<String> + TypeHandler

    @Builder.Default
    private Integer creditScore = 100;

    @Builder.Default
    private Boolean enabled = true;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    // 多对多关联角色（RBAC）
    @JsonIgnore
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    // ==================== 业务方法 ====================

    /**
     * 添加角色
     */
    public boolean addRole(Role role) {
        if (this.roles == null) {
            this.roles = new HashSet<>();
        }
        return this.roles.add(role);
    }

    /**
     * 移除角色
     */
    public boolean removeRole(Role role) {
        if (this.roles == null) {
            return false;
        }
        return this.roles.remove(role);
    }

    /**
     * 检查是否拥有指定角色
     */
    public boolean hasRole(String roleName) {
        if (this.roles == null) {
            return false;
        }
        return this.roles.stream()
                .anyMatch(role -> role.getName().equals(roleName));
    }

    /**
     * 获取所有角色名称
     */
    public Set<String> getRoleNames() {
        if (this.roles == null) {
            return Set.of();
        }
        return this.roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }

    /**
     * 获取用户的所有权限（通过角色）
     */
    public Set<String> getPermissions() {
        if (this.roles == null) {
            return Set.of();
        }
        return this.roles.stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(Permission::getName)
                .collect(Collectors.toSet());
    }

    /**
     * 检查是否拥有指定权限
     */
    public boolean hasPermission(String permissionName) {
        return getPermissions().contains(permissionName);
    }
}