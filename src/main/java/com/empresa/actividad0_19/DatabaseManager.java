package com.empresa.actividad0_19;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.ConnectionString;
import org.bson.Document;

public class DatabaseManager {
    private static DatabaseManager instance;
    private final MongoClient mongoClient;
    private MongoDatabase database;
    private MongoDatabase db;
    private MongoCollection<Document> collection;

    private DatabaseManager(String connectionString, String databaseName) {
        this.mongoClient = MongoClients.create("mongodb+srv://admin:admin@ejemplo.oqmkxob.mongodb.net/");
        this.database = mongoClient.getDatabase("productos");
    }

    public static DatabaseManager getInstance(String connectionString) {
        if (instance == null) {
            instance = new DatabaseManager(connectionString, "productos");
        }
        return instance;
    }

    public MongoCollection<Document> getCollection(String collectionName) {
        return this.database.getCollection("productost");
    }

    public void insertDocument(String collectionName, Document document) {
        MongoCollection<Document> collection = getCollection(collectionName);
        collection.insertOne(document);
    }
}
