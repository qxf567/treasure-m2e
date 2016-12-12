package com.github.treasure.m2e.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lashou.treasure.es.entity.ESOrdersDetailEntity;
import com.lashou.treasure.es.entity.ESOrdersEntity;
import com.lashou.treasure.es.entity.ProductEntity;
import com.lashou.treasure.es.entity.TreasureTermEntity;
import com.lashou.treasure.es.service.ESOrderDetailService;
import com.lashou.treasure.es.service.ESOrderService;
import com.lashou.treasure.es.service.ESProductService;
import com.lashou.treasure.es.service.ESTreasureTermService;

@Service
public class EsService {

    @Autowired
    private ESProductService productService;

    @Autowired
    private ESTreasureTermService termService;

    @Autowired
    private ESOrderService orderService;
    @Autowired
    private ESOrderDetailService detailService;
    @Autowired
    private CacheService cacheService;

    public void serviceSave(String type, Object object) {
	System.out.println("esService:"+type+"     "+object);
	if ("product".equals(type)) {
	    productService.save((ProductEntity) object);
	} else if ("treasure_term".equals(type)) {
	    termService.save((TreasureTermEntity) object);
	} else if ("orders".equals(type)) {
	    orderService.save((ESOrdersEntity)object);
	}else if ("orders_detail".equals(type)) {
	    detailService.save((ESOrdersDetailEntity)object);
	}
    }
    
    public void serviceDelete(String type, Long id) {
	if ("product".equals(type)) {
	    productService.delete(id);
	} else if ("treasure_term".equals(type)) {
	    termService.delete(id);
//	} else if ("orders".equals(type)) {
//	    orderService.deleteByOrderId(id);
//	}else if ("orders_detail".equals(type)) {
//	    detailService.deleteByOrderId(id);
	}
    }
    
    /**
     * 批量操作 ,商品
     */
    public void bulkProduct(ArrayList<ProductEntity> object) {
	productService.save(object);
    }
    
}