package com.skegworks.mobilepos.data.remote.firestore

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await

class FirestoreHelper(val db: FirebaseFirestore = FirebaseFirestore.getInstance()) {

//    private val db = FirebaseFirestore.getInstance()

    /** Add a document to a collection **/
    fun <T> addDocument(
        collection: String,
        id: String,
        data: T,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val userRef = db.collection(collection)
            .document(id)

        userRef.set(data as Any, SetOptions.merge())
            .addOnSuccessListener { onSuccess(id) }
            .addOnFailureListener { e -> onFailure(e) }
    }

    /** Get all documents from a collection **/
    fun getDocument(
        collection: String,
        documentId: String,
        onSuccess: (DocumentSnapshot) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection(collection)
            .document(documentId)
            .get()
            .addOnSuccessListener { document -> onSuccess(document) }
            .addOnFailureListener { e -> onFailure(e) }
    }

    /** Get all documents from a collection **/
    fun getAllDocuments(
        collection: String,
        onSuccess: (QuerySnapshot) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection(collection)
            .get()
            .addOnSuccessListener { snapshot -> onSuccess(snapshot) }
            .addOnFailureListener { e -> onFailure(e) }
    }

    /** Update a document by ID **/
    fun updateDocument(
        collection: String,
        documentId: String,
        data: Map<String, Any>,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection(collection)
            .document(documentId)
            .update(data)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    /** Delete a document by ID **/
    fun deleteDocument(
        collection: String,
        documentId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection(collection)
            .document(documentId)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    suspend fun <T> addDocument(
        collection: String,
        id: String,
        data: T,
    ): Result<Unit> = runCatching {
        val userRef = db.collection(collection)
            .document(id)
        userRef.set(data as Any, SetOptions.merge()).await()
    }

    /** Get all documents from a collection **/
    suspend fun <T> getAllDocuments(
        collection: String,
        clazz: Class<T>
    ): Result<List<T>> = runCatching {
        val snapshot = db.collection(collection)
            .orderBy(FieldPath.documentId())
            .get(Source.SERVER).await()
        snapshot.documents.mapNotNull { it.toObject(clazz) }
    }

    /** Get all documents from a collection **/
    suspend fun <T> getLatestDocuments(
        collection: String,
        updatedDate: Long,
        clazz: Class<T>
    ): Result<List<T>> = runCatching {
        val snapshot = db.collection(collection)
            .whereGreaterThan("updatedAt", updatedDate)
            .get().await()
        snapshot.documents.mapNotNull { it.toObject(clazz) }
    }
}
