package cn.edu.swu.campus_buddy_matching_system.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * 权限实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Permission {

    private Long id;

    @NotBlank(message = "权限名称不能为空")
    @Size(max = 100, message = "权限名称长度不能超过100个字符")
    private String name;

    @Size(max = 255, message = "权限描述长度不能超过255个字符")
    private String description;

    @NotNull(message = "资源类型不能为空")
    private String resource;

    @NotNull(message = "操作类型不能为空")
    private String action;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    /**
     * 根据资源与操作构建权限名称
     */
    public static String buildPermissionName(String resource, String action) {
        return resource.toLowerCase() + ":" + action.toLowerCase();
    }

    /**
     * 更新权限名称（当resource或action变化时调用）
     */
    public void updateName() {
        this.name = buildPermissionName(this.resource, this.action);
    }
}