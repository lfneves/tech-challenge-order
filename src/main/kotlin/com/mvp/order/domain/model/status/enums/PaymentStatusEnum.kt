package com.mvp.order.domain.model.status.enums

enum class PaymentStatusEnum(val value: String) {
    PENDING("Pendente"),
    PAYMENT_REQUIRED("Aguardando Pagamento"),
    PREPARING("Preparando"),
    PAID("Pago"),
    FINISHED("Finalizado"),
}