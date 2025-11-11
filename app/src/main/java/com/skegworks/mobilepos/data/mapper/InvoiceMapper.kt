package com.skegworks.mobilepos.data.mapper

import com.skegworks.mobilepos.data.domain.Invoice
import com.skegworks.mobilepos.data.local.InvoiceEntity
import com.skegworks.mobilepos.data.remote.firestore.InvoiceFireStoreDto

fun InvoiceFireStoreDto.toEntity(): InvoiceEntity {
    return InvoiceEntity(
        id = id,
        invoiceNumber = invoiceNumber,
        invoiceDate = invoiceDate,
        totalPrice = totalPrice,
        totalDiscount = totalDiscount,
        finalPrice = finalPrice,
        customerId = customerId,
        customerName = customerName,
        customerPhone = customerPhone,
        businessId = businessId,
        isSynced = isSynced,
        cashDiscount = cashDiscount,
        couponId = couponId
    )
}

fun Invoice.toFirestoreDto(): InvoiceFireStoreDto {
    return InvoiceFireStoreDto(
        id = id,
        invoiceNumber = invoiceNumber,
        invoiceDate = invoiceDate,
        totalPrice = totalPrice,
        totalDiscount = totalDiscount,
        finalPrice = finalPrice,
        customerId = customer.id,
        customerName = customer.name,
        customerPhone = customer.phone,
        businessId = business.id,
        isSynced = isSynced,
        cashDiscount = cashDiscount,
        couponId = coupon?.id ?: ""
    )
}

fun Invoice.toEntity(): InvoiceEntity {
    return InvoiceEntity(
        id = id,
        invoiceNumber = invoiceNumber,
        invoiceDate = invoiceDate,
        totalPrice = totalPrice,
        totalDiscount = totalDiscount,
        finalPrice = finalPrice,
        customerId = customer.id,
        customerName = customer.name,
        customerPhone = customer.phone,
        businessId = business.id,
        isSynced = isSynced,
        cashDiscount = cashDiscount,
        couponId = coupon?.id ?: ""
    )
}