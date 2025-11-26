-- 创建订单表
CREATE TABLE `t_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_no` varchar(32) NOT NULL COMMENT '订单号',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `total_amount` decimal(10,2) NOT NULL COMMENT '订单总金额',
  `status` varchar(32) NOT NULL COMMENT '订单状态',
  `payment_method` varchar(32) DEFAULT NULL COMMENT '支付方式',
  `payment_time` datetime DEFAULT NULL COMMENT '支付时间',
  `shipping_time` datetime DEFAULT NULL COMMENT '发货时间',
  `finish_time` datetime DEFAULT NULL COMMENT '完成时间',
  `cancel_time` datetime DEFAULT NULL COMMENT '取消时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 创建订单商品项表
CREATE TABLE `t_order_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `product_id` bigint(20) NOT NULL COMMENT '商品ID',
  `product_name` varchar(255) NOT NULL COMMENT '商品名称',
  `product_sku` varchar(64) NOT NULL COMMENT '商品SKU',
  `quantity` int(11) NOT NULL COMMENT '商品数量',
  `unit_price` decimal(10,2) NOT NULL COMMENT '商品单价',
  `total_price` decimal(10,2) NOT NULL COMMENT '商品总价',
  `warehouse_id` bigint(20) DEFAULT NULL COMMENT '仓库ID',
  `supplier_id` bigint(20) DEFAULT NULL COMMENT '供应商ID',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_product_sku` (`product_sku`),
  KEY `idx_warehouse_id` (`warehouse_id`),
  CONSTRAINT `fk_order_item_order_id` FOREIGN KEY (`order_id`) REFERENCES `t_order` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单商品项表';

-- 创建订单日志表
CREATE TABLE `t_order_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `old_status` varchar(32) NOT NULL COMMENT '旧状态',
  `new_status` varchar(32) NOT NULL COMMENT '新状态',
  `operator` varchar(64) NOT NULL COMMENT '操作人',
  `operate_time` datetime NOT NULL COMMENT '操作时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  CONSTRAINT `fk_order_log_order_id` FOREIGN KEY (`order_id`) REFERENCES `t_order` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单日志表';

-- 创建库存表
CREATE TABLE `t_inventory` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `product_sku` varchar(64) NOT NULL COMMENT '商品SKU',
  `warehouse_id` bigint(20) NOT NULL COMMENT '仓库ID',
  `total_stock` int(11) NOT NULL COMMENT '总库存',
  `available_stock` int(11) NOT NULL COMMENT '可用库存',
  `reserved_stock` int(11) NOT NULL COMMENT '预占库存',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_product_sku_warehouse_id` (`product_sku`,`warehouse_id`),
  KEY `idx_product_sku` (`product_sku`),
  KEY `idx_warehouse_id` (`warehouse_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存表';

-- 创建物流信息表
CREATE TABLE `t_logistics_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `logistics_company` varchar(64) NOT NULL COMMENT '物流公司',
  `tracking_number` varchar(64) NOT NULL COMMENT '运单号',
  `waybill_number` varchar(64) DEFAULT NULL COMMENT '电子面单号',
  `status` varchar(32) NOT NULL COMMENT '物流状态',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tracking_number` (`tracking_number`),
  KEY `idx_order_id` (`order_id`),
  CONSTRAINT `fk_logistics_info_order_id` FOREIGN KEY (`order_id`) REFERENCES `t_order` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物流信息表';

-- 插入初始数据
-- 插入库存数据
INSERT INTO `t_inventory` (`product_sku`, `warehouse_id`, `total_stock`, `available_stock`, `reserved_stock`, `create_time`, `update_time`) VALUES
('SKU001', 1, 100, 100, 0, NOW(), NOW()),
('SKU002', 1, 200, 200, 0, NOW(), NOW()),
('SKU003', 2, 150, 150, 0, NOW(), NOW());

-- 插入订单状态字典数据
INSERT INTO `t_sys_dict_type` (`dict_type`, `dict_name`, `status`, `create_time`, `update_time`) VALUES
('order_status', '订单状态', '0', NOW(), NOW());

INSERT INTO `t_sys_dict_data` (`dict_type`, `dict_label`, `dict_value`, `status`, `create_time`, `update_time`) VALUES
('order_status', '待支付', 'PENDING_PAYMENT', '0', NOW(), NOW()),
('order_status', '已支付', 'PAID', '0', NOW(), NOW()),
('order_status', '已发货', 'SHIPPED', '0', NOW(), NOW()),
('order_status', '已完成', 'COMPLETED', '0', NOW(), NOW()),
('order_status', '已取消', 'CANCELLED', '0', NOW(), NOW());

-- 插入物流公司字典数据
INSERT INTO `t_sys_dict_type` (`dict_type`, `dict_name`, `status`, `create_time`, `update_time`) VALUES
('logistics_company', '物流公司', '0', NOW(), NOW());

INSERT INTO `t_sys_dict_data` (`dict_type`, `dict_label`, `dict_value`, `status`, `create_time`, `update_time`) VALUES
('logistics_company', '顺丰速运', 'SF_EXPRESS', '0', NOW(), NOW()),
('logistics_company', '中通快递', 'ZTO_EXPRESS', '0', NOW(), NOW()),
('logistics_company', '圆通速递', 'YTO_EXPRESS', '0', NOW(), NOW()),
('logistics_company', '申通快递', 'STO_EXPRESS', '0', NOW(), NOW()),
('logistics_company', '韵达快递', 'YUNDA_EXPRESS', '0', NOW(), NOW());
