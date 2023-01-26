package com.cricket.configuration;

import java.util.concurrent.TimeUnit;

import org.bson.Document;
import com.mongodb.BasicDBObject;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.connection.ConnectionPoolSettings;

public class MongoDBUtility  {
	
	private static MongoClient mongoClient;
	
	private static final String URL = EnviromentProperties.getEnvProperty("com.ccconecct.db.mongo.url");
	private static final String USER_NAME = EnviromentProperties.getEnvProperty("com.ccconecct.db.mongo.username");
	private static final String PASSWORD =  EnviromentProperties.getEnvProperty("com.ccconecct.db.mongo.password");
	private static final String MONGO_DATABASE =  EnviromentProperties.getEnvProperty("com.ccconecct.db.mongo.db");
	private static final String MONGO_CLUSTER =  EnviromentProperties.getEnvProperty("com.ccconecct.db.mongo.cluster");
	
	private static  String MONGO_URL = "mongodb+srv://"+USER_NAME+":"+PASSWORD+"@"+MONGO_CLUSTER+"."+URL+"/"+MONGO_DATABASE+"?retryWrites=true&w=majority";
	
	public static void main(String args[])throws Exception{
		
		try {
			//System.out.println(MONGO_URL);
			MongoDatabase database = MongoDBUtility.getMongoDB();
			MongoCollection<Document> collection = database.getCollection("conversations");
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("conversationId", "5fc8a38c-6952-4535-a459-267f194b08f3");
			MongoCursor<Document> cursor = collection.find(whereQuery).iterator();
			while (cursor.hasNext()) {
				Document doc = cursor.next();
				System.out.println(doc.getString("conversationId")); 
				System.out.println(doc.getString("groupName")); 
			}
		} catch (Exception e) {
        	System.out.println(e.getMessage());

        }
    }

    /**
     * Once done with your query, close this client calling the {@link MongoClient#close()} method
     *
     * @return returns a @MongoClient instance.
     */
	public static MongoClient getMongoClient() {
        ConnectionString connectionString = new ConnectionString(MONGO_URL);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .applyToConnectionPoolSettings((ConnectionPoolSettings.Builder builder) -> builder.maxSize(1000) //connections count
                        .minSize(30) //minimum number of connections in pool
                        .maxConnectionLifeTime(30, TimeUnit.MINUTES)
                        .maxConnectionIdleTime(30, TimeUnit.MILLISECONDS))
                .applyToSocketSettings(builder -> builder.connectTimeout(2000, TimeUnit.MILLISECONDS))
                .applyToSslSettings(builder ->
                        builder.enabled(true))
                .serverApi(ServerApi.builder()
                        .version(ServerApiVersion.V1)
                        .build())
                .retryWrites(true)
                .build();

        return MongoClients.create(settings);
    }

    public static MongoDatabase getMongoDB() {
        if(mongoClient == null) {
            mongoClient = getMongoClient();
        }
        return mongoClient.getDatabase(MONGO_DATABASE);
    }
}