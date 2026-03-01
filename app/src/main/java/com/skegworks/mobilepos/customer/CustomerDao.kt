package com.skegworks.mobilepos.customer
import androidx.room.*
import com.skegworks.mobilepos.data.local.CustomerEntity

@Dao
interface CustomerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customer: CustomerEntity)

    @Query("SELECT * FROM customers WHERE id = :id")
    suspend fun getCustomerById(id: String): CustomerEntity?

    @Query("SELECT * FROM customers")
    suspend fun getAllCustomers(): List<CustomerEntity>

    @Update
    suspend fun updateCustomer(customer: CustomerEntity)

    @Delete
    suspend fun deleteCustomer(customer: CustomerEntity)

    @Query("SELECT * FROM customers WHERE phone LIKE '%' || :phone || '%' ORDER BY updated_at DESC")
    suspend fun getCustomersByPhone(phone: String): List<CustomerEntity>

    @Query("SELECT * FROM customers WHERE phone LIKE '%' || :value || '%' OR name LIKE '%' || :value || '%' ORDER BY updated_at DESC")
    suspend fun getCustomersByPhoneOrName(value: String): List<CustomerEntity>
}
