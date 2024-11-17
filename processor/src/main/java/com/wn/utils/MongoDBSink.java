package com.wn.utils;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.flink.api.connector.sink2.Sink;
import org.apache.flink.api.connector.sink2.SinkWriter;
import org.bson.Document;

import java.io.IOException;
import java.io.Serializable;

public class MongoDBSink<T> implements Sink<T> {

    private final String connectionString;
    private final String databaseName;
    private final String collectionName;
    public MongoDBSink(String connectionString, String databaseName, String collectionName) {
        this.connectionString = connectionString;
        this.databaseName = databaseName;
        this.collectionName = collectionName;
    }

    @Override
    public SinkWriter<T> createWriter(InitContext context) throws IOException {
        return new MongoDBSinkWriter();
    }

    private class MongoDBSinkWriter implements SinkWriter<T> {
        private transient MongoClient mongoClient;
        private transient MongoCollection<Document> collection;

        public MongoDBSinkWriter() {
            initializeConnection();
        }

        private void initializeConnection() {
            try {
                mongoClient = MongoClients.create(connectionString);
                MongoDatabase database = mongoClient.getDatabase(databaseName);
                collection = database.getCollection(collectionName);
            } catch (Exception e) {
                System.err.println("not connecting " + e);
                throw new RuntimeException("Failed to initialize MongoDB connection", e);
            }
        }
        @Override
        public void write(T t, Context context) throws IOException, InterruptedException {
            try {
                Document document = Mapper.getInstance().convertValue(t, Document.class);
                document.append("timestamp", context.timestamp());
                System.out.println(document.toJson());
                collection.insertOne(document);
            } catch (Exception e) {
                throw new IOException("Failed to write to MongoDB", e);
            }
        }

        @Override
        public void flush(boolean b) throws IOException, InterruptedException {

        }

        @Override
        public void close() throws Exception {
            if (mongoClient != null) {
                mongoClient.close();
            }
        }
    }

}
