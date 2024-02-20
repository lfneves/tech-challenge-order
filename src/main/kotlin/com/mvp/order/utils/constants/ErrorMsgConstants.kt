package com.mvp.order.utils.constants

class ErrorMsgConstants {

    companion object {
        const val ERROR_CREATE_ORDER_CONSTANT   = "Não foi possivel criar o pedido."
        const val ERROR_PRODUCT_NOT_FOUND       = "Produto não encontrado."
        const val ERROR_ORDER_PRODUCT_NOT_FOUND = "Pedido ou Produto não encontrado."
        const val ERROR_USER_NOT_FOUND          = "Usuário não encontrado."
        const val ERROR_ORDER_NOT_FOUND         = "Pedido não encontrado."
        const val ERROR_CATEGORY_NOT_FOUND      = "Categoria do produto não encontrada."
        const val ERROR_ORDER_PAID_CONSTANT     = "Pedido já foi pago, caso queira adicionar novos produtos, cancele e inicie um novo pedido"
        const val ERROR_USER_ALREADY_EXIST      = "Usuário já está cadastrado."
    }
}