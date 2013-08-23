/*
 * 香港摩比科技有限公司 版权所有
 *
 * www.mobi-inf.com
 */
package org.herod.event;

/**
 * @author Xiong Zhijun
 * 
 */
public interface EventCodes {

	String REGIST_COMMAND = "regist";

	String TRANSACTION_ORDERED_COMMAND = "transactionOrdered";
	String TRANSACTION_ORDER_STATUS_UPDATE_COMMAND = "transactionOrderStatusUpdate";

	String ORDER_ORDERED_COMMAND = "orderOrdered";

	String ORDER_QUANTITY_UPDATE_COMMAND = "orderQuantityUpdate";
	String ORDER_STATUS_UPDATE_COMMAND = "orderStatusUpdate";
}
