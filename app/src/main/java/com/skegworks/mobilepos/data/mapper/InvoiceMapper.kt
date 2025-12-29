package com.skegworks.mobilepos.data.mapper

import com.skegworks.mobilepos.data.domain.Business
import com.skegworks.mobilepos.data.domain.Coupon
import com.skegworks.mobilepos.data.domain.CreditNote
import com.skegworks.mobilepos.data.domain.CreditNoteItem
import com.skegworks.mobilepos.data.domain.Customer
import com.skegworks.mobilepos.data.domain.Invoice
import com.skegworks.mobilepos.data.domain.InvoiceItem
import com.skegworks.mobilepos.data.domain.InvoiceState
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
        couponId = couponId,
        invoiceState = invoiceState
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
        couponId = coupon?.id ?: "",
        invoiceState = invoiceState.name
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
        couponId = coupon?.id ?: "",
        invoiceState = invoiceState.name
    )
}

fun InvoiceEntity.toDomain(customer: Customer, business: Business, coupon: Coupon?,  items: List<InvoiceItem>): Invoice {
    return Invoice(
        id = id,
        invoiceNumber = invoiceNumber,
        invoiceDate = invoiceDate,
        totalPrice = totalPrice,
        totalDiscount = totalDiscount,
        finalPrice = finalPrice,
        customer = customer,
        business = business,
        isSynced = true,
        cashDiscount = cashDiscount,
        invoiceState = InvoiceState.fromState(invoiceState) ?: InvoiceState.PRINT,
        items = items,
        coupon = coupon
    )
}

fun InvoiceFireStoreDto.toDomain(customer: Customer, business: Business, coupon: Coupon?,  items: List<InvoiceItem>): Invoice {
    return Invoice(
        id = id,
        invoiceNumber = invoiceNumber,
        invoiceDate = invoiceDate,
        totalPrice = totalPrice,
        totalDiscount = totalDiscount,
        finalPrice = finalPrice,
        customer = customer,
        business = business,
        isSynced = true,
        cashDiscount = cashDiscount,
        invoiceState = InvoiceState.fromState(invoiceState) ?: InvoiceState.PRINT,
        items = items,
        coupon = coupon
    )
}

fun Invoice.toCreditNote(creditNoteId: String,
                         creditNoteItems: List<CreditNoteItem>?): CreditNote {
    return CreditNote(
        id = creditNoteId,
        invoiceId = id,
        invoiceNumber = invoiceNumber,
        invoiceDate = invoiceDate,
        totalPrice = totalPrice,
        totalDiscount = totalDiscount,
        finalPrice = finalPrice,
        isSynced = isSynced,
        customer = customer,
        business = business,
        cashDiscount = cashDiscount,
        coupon = coupon,
        items = creditNoteItems ?: listOf(),
        description = "",
        creditNoteDate = ""
    )
}