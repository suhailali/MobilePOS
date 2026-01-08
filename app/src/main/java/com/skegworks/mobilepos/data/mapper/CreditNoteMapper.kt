package com.skegworks.mobilepos.data.mapper

import com.skegworks.mobilepos.data.domain.Business
import com.skegworks.mobilepos.data.domain.Coupon
import com.skegworks.mobilepos.data.domain.CreditNote
import com.skegworks.mobilepos.data.domain.CreditNoteItem
import com.skegworks.mobilepos.data.domain.Customer
import com.skegworks.mobilepos.data.local.CreditNoteEntity
import com.skegworks.mobilepos.data.remote.firestore.CreditNoteFireStoreDto

fun CreditNoteFireStoreDto.toEntity(): CreditNoteEntity {
    return CreditNoteEntity(
        id = id,
        invoiceId = invoiceId,
        invoiceNumber = invoiceNumber,
        invoiceDate = invoiceDate,
        totalPrice = totalPrice,
        totalDiscount = totalDiscount,
        finalPrice = finalPrice,
        customerId = customerId,
        customerName = customerName,
        customerPhone = customerPhone,
        businessId = businessId,
        isSynced = synced,
        cashDiscount = cashDiscount,
        couponId = couponId,
        creditNoteDate = creditNoteDate,
        description = description,
        updatedAt = updatedAt
    )
}

fun CreditNote.toFirestoreDto(): CreditNoteFireStoreDto {
    return CreditNoteFireStoreDto(
        id = id,
        invoiceId = invoiceId,
        invoiceNumber = invoiceNumber,
        invoiceDate = invoiceDate,
        totalPrice = totalPrice,
        totalDiscount = totalDiscount,
        finalPrice = finalPrice,
        customerId = customer.id,
        customerName = customer.name,
        customerPhone = customer.phone,
        businessId = business.id,
        synced = isSynced,
        cashDiscount = cashDiscount,
        couponId = coupon?.id ?: "",
        creditNoteDate = creditNoteDate,
        description = description
    )
}

fun CreditNote.toEntity(): CreditNoteEntity {
    return CreditNoteEntity(
        id = id,
        invoiceId = invoiceId,
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
        creditNoteDate = creditNoteDate,
        description = description,
        updatedAt = updatedAt
    )
}

fun CreditNoteEntity.toDomain(customer: Customer, business: Business, coupon: Coupon?,  items: List<CreditNoteItem>): CreditNote {
    return CreditNote(
        id = id,
        invoiceId = invoiceId,
        invoiceNumber = invoiceNumber,
        invoiceDate = invoiceDate,
        totalPrice = totalPrice,
        totalDiscount = totalDiscount,
        finalPrice = finalPrice,
        customer = customer,
        business = business,
        isSynced = true,
        cashDiscount = cashDiscount,
        items = items,
        coupon = coupon,
        creditNoteDate = creditNoteDate,
        description = description,
        updatedAt = updatedAt
    )
}

fun CreditNoteFireStoreDto.toDomain(customer: Customer, business: Business, coupon: Coupon?,  items: List<CreditNoteItem>): CreditNote {
    return CreditNote(
        id = id,
        invoiceId = invoiceId,
        invoiceNumber = invoiceNumber,
        invoiceDate = invoiceDate,
        totalPrice = totalPrice,
        totalDiscount = totalDiscount,
        finalPrice = finalPrice,
        customer = customer,
        business = business,
        isSynced = true,
        cashDiscount = cashDiscount,
        items = items,
        coupon = coupon,
        creditNoteDate = creditNoteDate,
        description = description,
        updatedAt = updatedAt
    )
}
