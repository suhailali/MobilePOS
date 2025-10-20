package com.skegworks.mobilepos.data.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class FirestoreHelper {

    private val db = FirebaseFirestore.getInstance()

    /** Add a document to a collection **/
    fun addDocument(
        collection: String,
        data: Map<String, Any>,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection(collection)
            .add(data)
            .addOnSuccessListener { docRef -> onSuccess(docRef.id) }
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
}
