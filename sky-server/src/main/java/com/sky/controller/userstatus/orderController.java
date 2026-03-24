package com.sky.controller.userstatus;

import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.Result;
import com.sky.service.userOrderService;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/order")
public class orderController {
    @Autowired
    private userOrderService userOrderService;
   @PostMapping("/submit")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
       OrderSubmitVO submit = userOrderService.submit(ordersSubmitDTO);
       return Result.success(submit);
   }

}
