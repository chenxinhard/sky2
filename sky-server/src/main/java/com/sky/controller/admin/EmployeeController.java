package com.sky.controller.admin;

import ch.qos.logback.core.pattern.util.RegularEscapeUtil;
import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.sky.entity.Employee;
import java.awt.geom.RectangularShape;
import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工登录")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);
        System.out.println("线程名字"+ Thread.currentThread().getId());

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation("登出")
    public Result<String> logout() {
        return Result.success();
    }




    @PostMapping
    @ApiOperation("新增员工")
    public Result save(@RequestBody EmployeeDTO employeeDTO) {
           log.info("新增员工:{}", employeeDTO);
           employeeService.save(employeeDTO);
           return Result.success();
    }


    @GetMapping("/page")
    @ApiOperation("查询员工")
    public Result<PageResult> select( EmployeePageQueryDTO employeePageQueryDTO) {
      log.info("员工分页查询,{}", employeePageQueryDTO);
    PageResult<Employee> pageResult =  employeeService.pageselect(employeePageQueryDTO);
      return Result.success(pageResult);
    }


    @PostMapping("/status/{status}")
    public Result forbid(@PathVariable Integer status,long id) {
     log.info("修改参数:{},{}", status, id);
     employeeService.ChangeParam(status,id);
     return Result.success();
    }

    @GetMapping("/{id}")
    public Result<Employee> selectById(@PathVariable Integer id) {
        log.info("根据id查询{}", id);
    Employee employee =     employeeService.selectById(id);
        return Result.success(employee);
    }

    @PutMapping
    public Result update(@RequestBody  Employee employee) {
        log.info("修改员工{}", employee);
        employeeService.Change(employee);
        return Result.success();
    }

    @PutMapping("/editPassword")
    @ApiOperation("修改密码")
    public Result EditPassword(@RequestBody PasswordDTO passwordDTO) {

        employeeService.Editpass(passwordDTO);
        return Result.success();

    }


}
