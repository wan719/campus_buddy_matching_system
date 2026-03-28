package cn.edu.swu.campus_buddy_matching_system.model.enums;

/**
 * 权限操作类型枚举
 */
public enum PermissionAction {
    CREATE("create"),
    READ("read"),
    UPDATE("update"),
    DELETE("delete"),
    CONFIG("config");

    private final String value;

    PermissionAction(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * 根据字符串值获取对应的枚举
     */
    public static PermissionAction fromValue(String value) {
        for (PermissionAction action : PermissionAction.values()) {
            if (action.value.equalsIgnoreCase(value)) {
                return action;
            }
        }
        return null;
    }
}