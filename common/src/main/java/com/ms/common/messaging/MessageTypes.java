package com.ms.common.messaging;

public final class MessageTypes {

    public static final String PROCESS_PAYMENT_COMMAND= "ProcessPaymentCommand";
    public static final String PAYMENT_COMPLETED_EVENT= "PaymentCompletedEvent";
    public static final String PAYMENT_FAILED_EVENT = "PaymentFailedEvent";

    public static final String REFUND_PAYMENT_COMMAND= "RefundPaymentCommand";
    public static final String PAYMENT_REFUNDED_EVENT= "PaymentRefundedEvent";

    public static final String RESERVE_STOCK_COMMAND= "ReserveStockCommand";
    public static final String STOCK_RESERVED_EVENT= "StockReservedEvent";
    public static final String STOCK_RESERVATION_FAILED_EVENT="StockReservationFailedEvent";

    public static final String ORDER_CONFIRMED_EVENT = "OrderConfirmedEvent";

    private MessageTypes() {
    }
}
