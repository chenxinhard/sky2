package com.sky.service;

import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;
import com.sky.result.Result;

import com.sky.dto.EmployeeDTO;
public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);


    void save(EmployeeDTO employeeDTO);

    PageResult<Employee> pageselect(EmployeePageQueryDTO employeePageQueryDTO);

    void ChangeParam(Integer status, long id);

    Employee selectById(Integer id);

    void Change(Employee employee);

    void Editpass(PasswordDTO passwordDTO);
}
