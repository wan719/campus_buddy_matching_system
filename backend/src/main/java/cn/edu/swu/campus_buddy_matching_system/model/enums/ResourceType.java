package cn.edu.swu.campus_buddy_matching_system.model.enums;

/**
 * 资源类型枚举
 */
public enum ResourceType {
    USER("user"),
    ROLE("role"),
    PERMISSION("permission"),
    ACTIVITY("activity"),
    SYSTEM("system");

    private final String value;

    ResourceType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * 根据字符串值获取对应的枚举
     */
    public static ResourceType fromValue(String value) {
        for (ResourceType type : ResourceType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        return null;
    }
}