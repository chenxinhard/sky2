package com.sky.service.impl;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.BaseException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.orderMapper;
import com.sky.mapper.orderdetailMapper;
import com.sky.mapper.shoppingCardMapper;
import com.sky.service.AddressBookService;
import com.sky.service.shoppingCardService;
import com.sky.service.userOrderService;
import com.sky.vo.OrderSubmitVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class userOrderServiceImpl implements userOrderService {

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private shoppingCardMapper shoppingCardMapper;

    @Autowired
    private orderMapper orderMapper;

    @Autowired
    private orderdetailMapper orderdetailMapper;



    @Transactional(rollbackFor = BaseException.class)
    @Override
    public OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO) {
        //校验购物车或者地址簿为空

        AddressBook addressBook = addressBookService.getById(ordersSubmitDTO.getAddressBookId());
        if(addressBook==null){
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        ShoppingCart shoppingCart = new ShoppingCart();
        Long currentId = BaseContext.getCurrentId();
         shoppingCart.setUserId(currentId);
        List<ShoppingCart> list = shoppingCardMapper.list(shoppingCart);
        if(list==null||list.size()==0){
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }
        //向订单表插入一条数据
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO,orders);
        orders.setUserId(currentId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(orders.UN_PAID);
        orders.setStatus(orders.PENDING_PAYMENT);
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setPhone(addressBook.getPhone());
        //收货人
        orders.setConsignee(addressBook.getConsignee());

        orderMapper.insert(orders);

        //向明细表插入n条数据
        List<OrderDetail> ListOD = new ArrayList<>();
      for (ShoppingCart cart : list) {
          OrderDetail orderDetail = new OrderDetail();
          BeanUtils.copyProperties(cart,orderDetail);
          orderDetail.setOrderId(orders.getId());
          ListOD.add(orderDetail);
      }
        orderdetailMapper.insertD(ListOD);


        //清空购物车数据

        shoppingCardMapper.clean(currentId);

        //封装返回体
        return OrderSubmitVO.builder()
                .id(orders.getId())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .orderTime(orders.getOrderTime())
                .build();


    }
}
