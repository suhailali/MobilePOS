package com.skegworks.mobilepos.data.mapper

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
        finalRoundedOffPrice = finalRoundedOffPrice,
        quantity = quantity,
        alertQuantity = alertQuantity,
        description = description,
        imageUrl = imageUrl,
        isActive = isActive,
        isSynced = isSynced,
        createdAt = createdAt,
        updatedAt = updatedAt,
        discountPercentage = discountPercentage
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
        finalRoundedOffPrice = finalRoundedOffPrice,
        quantity = quantity,
        alertQuantity = alertQuantity,
        description = description,
        imageUrl = imageUrl,
        isActive = isActive,
        isSynced = isSynced,
        createdAt = createdAt,
        updatedAt = updatedAt,
        discountPercentage = discountPercentage
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
        finalRoundedOffPrice = finalRoundedOffPrice,
        quantity = quantity,
        alertQuantity = alertQuantity,
        description = description,
        imageUrl = imageUrl,
        isActive = isActive,
        isSynced = isSynced,
        createdAt = createdAt,
        updatedAt = updatedAt,
        discountPercentage = discountPercentage
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
        finalRoundedOffPrice = finalRoundedOffPrice,
        quantity = quantity,
        alertQuantity = alertQuantity,
        description = description,
        imageUrl = imageUrl,
        isActive = isActive,
        isSynced = isSynced,
        createdAt = createdAt,
        updatedAt = updatedAt,
        discountPercentage = discountPercentage
    )
}
