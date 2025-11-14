package com.skegworks.mobilepos.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.skegworks.mobilepos.data.domain.CashCounterStatus


@Entity(tableName = "cash_counter")
data class CashCounterEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "opening_balance") val openingBalance: Double,
    @ColumnInfo(name = "closing_balance") val closingBalance: Double,
    @ColumnInfo(name = "status") val status: String,
    @ColumnInfo(name = "opened_at") val openedAt: Long,
    @ColumnInfo(name = "opened_by") val openedBy: String,
    @ColumnInfo(name = "closed_at") val closedAt: Long,
    @ColumnInfo(name = "closed_by") val closedBy: String,
    @ColumnInfo(name = "is_synced") val isSynced: Boolean,
)