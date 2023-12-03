package com.lcl.service;

import com.lcl.dto.EmployeeLoginDTO;
import com.lcl.entity.Employee;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

}
