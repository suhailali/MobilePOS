package com.skegworks.mobilepos.data.mapper

import com.skegworks.mobilepos.data.domain.InvoiceItem
import com.skegworks.mobilepos.data.local.InvoiceItemEntity
import com.skegworks.mobilepos.data.remote.firestore.InvoiceItemFireStoreDto

// --- Firestore DTO -> Domain ---
fun InvoiceItemFireStoreDto.toDomain(): InvoiceItem {
    return InvoiceItem(
        id = id,
        vendorId = vendorId,
        vendorName = vendorName,
        hsnCode = hsnCode,
        title = title,
        categoryId = categoryId,
        categoryName = categoryName,
        sku = sku,
        size = size,
        color = color,
        itemPrice = itemPrice,
        inputGstPercentage = inputGstPercentage,
        inputGst = inputGst,
        outputGstPercentage = outputGstPercentage,
        outputGst = outputGst,
        saleMargin = saleMargin,
        cost = cost,
        salePriceWithoutGst = salePriceWithoutGst,
        salePrice = salePrice,
        finalRoundedOffPrice = finalRoundedOffPrice,
        quantity = quantity,
        isActive = isActive,
        isSynced = isSynced,
        createdAt = createdAt,
        updatedAt = updatedAt,
        discountPercentage = discountPercentage,
        discountAmount = discountAmount,
        createdBy = createdBy,
        updatedBy = updatedBy,
        productId = productId,
        invoiceId = invoiceId,
        invoiceNumber = invoiceNumber,
        invoiceDate = invoiceDate,
        priceAfterDiscount = priceAfterDiscount
    )
}

// --- Domain -> Firestore DTO ---
fun InvoiceItem.toFirestoreDto(): InvoiceItemFireStoreDto {
    return InvoiceItemFireStoreDto(
        id = id,
        vendorId = vendorId,
        vendorName = vendorName,
        hsnCode = hsnCode,
        title = title,
        categoryId = categoryId,
        categoryName = categoryName,
        sku = sku,
        size = size,
        color = color,
        itemPrice = itemPrice,
        inputGstPercentage = inputGstPercentage,
        inputGst = inputGst,
        outputGstPercentage = outputGstPercentage,
        outputGst = outputGst,
        saleMargin = saleMargin,
        cost = cost,
        salePriceWithoutGst = salePriceWithoutGst,
        salePrice = salePrice,
        finalRoundedOffPrice = finalRoundedOffPrice,
        quantity = quantity,
        isActive = isActive,
        isSynced = isSynced,
        createdAt = createdAt,
        updatedAt = updatedAt,
        discountPercentage = discountPercentage,
        discountAmount = discountAmount,
        createdBy = createdBy,
        updatedBy = updatedBy,
        productId = productId,
        invoiceId = invoiceId,
        invoiceNumber = invoiceNumber,
        invoiceDate = invoiceDate,
        priceAfterDiscount = priceAfterDiscount
    )
}

// --- Room Entity -> Domain ---
fun InvoiceItemEntity.toDomain(): InvoiceItem {
    return InvoiceItem(
        id = id,
        vendorId = vendorId,
        vendorName = vendorName,
        hsnCode = hsnCode,
        title = title,
        categoryId = categoryId,
        categoryName = categoryName,
        sku = sku,
        size = size,
        color = color,
        itemPrice = itemPrice,
        inputGstPercentage = inputGstPercentage,
        inputGst = inputGst,
        outputGstPercentage = outputGstPercentage,
        outputGst = outputGst,
        saleMargin = saleMargin,
        cost = cost,
        salePriceWithoutGst = salePriceWithoutGst,
        salePrice = salePrice,
        finalRoundedOffPrice = finalRoundedOffPrice,
        quantity = quantity,
        isActive = isActive,
        isSynced = isSynced,
        createdAt = createdAt,
        updatedAt = updatedAt,
        discountPercentage = discountPercentage,
        discountAmount = discountAmount,
        createdBy = createdBy,
        updatedBy = updatedBy,
        productId = productId,
        invoiceId = invoiceId,
        invoiceNumber = invoiceNumber,
        invoiceDate = invoiceDate,
        priceAfterDiscount = priceAfterDiscount
    )
}

// --- Domain -> Room Entity ---
fun InvoiceItem.toEntity(): InvoiceItemEntity {
    return InvoiceItemEntity(
        id = id,
        vendorId = vendorId,
        vendorName = vendorName,
        hsnCode = hsnCode,
        title = title,
        categoryId = categoryId,
        categoryName = categoryName,
        sku = sku,
        size = size,
        color = color,
        itemPrice = itemPrice,
        inputGstPercentage = inputGstPercentage,
        inputGst = inputGst,
        outputGstPercentage = outputGstPercentage,
        outputGst = outputGst,
        saleMargin = saleMargin,
        cost = cost,
        salePriceWithoutGst = salePriceWithoutGst,
        salePrice = salePrice,
        finalRoundedOffPrice = finalRoundedOffPrice,
        quantity = quantity,
        isActive = isActive,
        isSynced = isSynced,
        createdAt = createdAt,
        updatedAt = updatedAt,
        discountPercentage = discountPercentage,
        discountAmount = discountAmount,
        createdBy = createdBy,
        updatedBy = updatedBy,
        productId = productId,
        invoiceId = invoiceId,
        invoiceNumber = invoiceNumber,
        invoiceDate = invoiceDate,
        priceAfterDiscount = priceAfterDiscount
    )
}

