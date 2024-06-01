package com.lcl.utils;

import com.lcl.dto.user.UserDTO;
import lombok.Data;

/**
 * @author LovelyPeracid
 */
@Data
public class UserHolder {
    private static final ThreadLocal<UserDTO> tl = new ThreadLocal<>();
    public static void saveUser(UserDTO user){
        tl.set(user);
    }

    public static UserDTO getUser(){
        return tl.get();
    }

    public static void removeUser(){
        tl.remove();
    }
}
