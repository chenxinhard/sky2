package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.handler.GlobalExceptionHandler;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        //
    password =  DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    @Override
    public void save(EmployeeDTO employeeDTO) {
        System.out.println("线程名字"+ Thread.currentThread().getId());
         Employee employee = new  Employee();
         //对象属性拷贝
        BeanUtils.copyProperties(employeeDTO,employee);
        employee.setStatus(StatusConstant.ENABLE);
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        //解析Token
         employee.setCreateUser(BaseContext.getCurrentId());
         employee.setUpdateUser(BaseContext.getCurrentId());
         employeeMapper.saveEmp(employee);
    }


    @Override
    public PageResult<Employee> pageselect(EmployeePageQueryDTO employeePageQueryDTO) {
        PageHelper.startPage(employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());

        List<Employee> pagelist  = employeeMapper.pageselct(employeePageQueryDTO);
        Page<Employee>  p =  (Page<Employee>)   pagelist;
        return  new PageResult<Employee>(p.getTotal(),p.getResult());
    }

    @Override
    public void ChangeParam(Integer status, long id) {
          //传统写法
//        Employee employee = new Employee();
//        employee.setStatus(status);
//        employee.setId(id);
         //链式编程
//        Employee employee = Employee.builder().status(status).id(id).build();

        employeeMapper.update(status,id);
    }

    @Override
    public Employee selectById(Integer id) {
        return employeeMapper.selectById(id);
    }

    @Override
    public void Change(Employee employee) {
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(BaseContext.getCurrentId());
        employeeMapper.Change(employee);
    }

    @Override
    public void Editpass(PasswordDTO passwordDTO) {
        Integer  empId = passwordDTO.getEmpId();
       Employee employee =  employeeMapper.selectById(empId);
        log.info("查出的员工:{}",employee);
        String password = employee.getPassword();
        String  passwordE  = DigestUtils.md5DigestAsHex(passwordDTO.getOldPassword().getBytes());


       try{
           if (employee != null && password.equals(passwordE)) {
               String passwordN = DigestUtils.md5DigestAsHex(passwordDTO.getNewPassword().getBytes());
               passwordDTO.setNewPassword(passwordN);
                   employeeMapper.editPass(passwordDTO);
               }

       }catch (Exception e){
           e.printStackTrace();//打印错误信息
           throw new RuntimeException("错误"+e.getMessage());
       }

       }
    }

