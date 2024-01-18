package com.lcl.enumeration;

/**
 * @author LovelyPeracid
 */
public enum Role {
    root(1),
    admin(2),
    user(3),
    visitor(4);

    private final int accessLevel;

    Role(int accessLevel) {
        this.accessLevel = accessLevel;
    }

    public int getAccessLevel() {
        return accessLevel;
    }
}
