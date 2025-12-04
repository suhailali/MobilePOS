package com.skegworks.mobilepos.data.mapper

import com.skegworks.mobilepos.data.domain.InvoiceItem
import com.skegworks.mobilepos.data.domain.Product
import com.skegworks.mobilepos.data.local.ProductEntity
import com.skegworks.mobilepos.data.remote.firestore.ProductFireStoreDto

// --- Firestore DTO -> Domain ---
fun ProductFireStoreDto.toDomain(): Product {
    return Product(
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
        salePriceWithoutDiscount = salePriceWithoutDiscount,
        finalRoundedOffPrice = finalRoundedOffPrice,
        quantity = quantity,
        alertQuantity = alertQuantity,
        description = description,
        imageUrl = imageUrl,
        isActive = isActive,
        isDeleted = isDeleted,
        isSynced = isSynced,
        createdAt = createdAt,
        updatedAt = updatedAt,
        discountPercentage = discountPercentage,
        discountAmount = discountAmount,
        createdBy = createdBy,
        updatedBy = updatedBy
    )
}

// --- Domain -> Firestore DTO ---
fun Product.toFirestoreDto(): ProductFireStoreDto {
    return ProductFireStoreDto(
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
        salePriceWithoutDiscount = salePriceWithoutDiscount,
        finalRoundedOffPrice = finalRoundedOffPrice,
        quantity = quantity,
        alertQuantity = alertQuantity,
        description = description,
        imageUrl = imageUrl,
        isActive = isActive,
        isDeleted = isDeleted,
        isSynced = isSynced,
        createdAt = createdAt,
        updatedAt = updatedAt,
        discountPercentage = discountPercentage,
        discountAmount = discountAmount,
        createdBy = createdBy,
        updatedBy = updatedBy
    )
}

// --- Room Entity -> Domain ---
fun ProductEntity.toDomain(): Product {
    return Product(
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
        salePriceWithoutDiscount = salePriceWithoutDiscount,
        finalRoundedOffPrice = finalRoundedOffPrice,
        quantity = quantity,
        alertQuantity = alertQuantity,
        description = description,
        imageUrl = imageUrl,
        isActive = isActive,
        isDeleted = isDeleted,
        isSynced = isSynced,
        createdAt = createdAt,
        updatedAt = updatedAt,
        discountPercentage = discountPercentage,
        discountAmount = discountAmount,
        createdBy = createdBy,
        updatedBy = updatedBy
    )
}

// --- Domain -> Room Entity ---
fun Product.toEntity(): ProductEntity {
    return ProductEntity(
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
        salePriceWithoutDiscount = salePriceWithoutDiscount,
        finalRoundedOffPrice = finalRoundedOffPrice,
        quantity = quantity,
        alertQuantity = alertQuantity,
        description = description,
        imageUrl = imageUrl,
        isActive = isActive,
        isDeleted = isDeleted,
        isSynced = isSynced,
        createdAt = createdAt,
        updatedAt = updatedAt,
        discountPercentage = discountPercentage,
        discountAmount = discountAmount,
        createdBy = createdBy,
        updatedBy = updatedBy
    )
}

fun Product.toInvoiceItem(id: String): InvoiceItem {
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
        isActive = isActive,
        isSynced = isSynced,
        createdAt = createdAt,
        updatedAt = updatedAt,
        discountPercentage = discountPercentage,
        discountAmount = discountAmount,

        salePriceWithoutDiscount = salePriceWithoutDiscount,


        productId = id,
        // Invoice and user details will be added just before generating invoice
        invoiceId = "",
        invoiceNumber = "",
        invoiceDate = 0L,
        createdBy = "",
        updatedBy = "",
        //TODO - this quantity is not same as Product
        quantity = 1,

    )
}

