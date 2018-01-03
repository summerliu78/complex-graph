package com.yinker.xiaov.tivyv;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;

public class MongoDBDaoImpl implements MongoDBDao {

	private Mongo mongoClient = null;
	
	public MongoDBDaoImpl() {
		if (mongoClient == null) {
			try {
				mongoClient = new MongoClient(TelemetryProperties.MONGODB_SERVER_URL, TelemetryProperties.MONGODB_SERVER_PORT);
			} catch (MongoException e) {
				e.printStackTrace();
			}	
		}
	}
		
	@Override
	public DB getDB(String dbName) {
		return mongoClient.getDB(dbName);
	}

	@Override
	public List<DBObject> query(DB db, String collectionName, int startPosition, int getNum) {

		DBCursor cursor = null;
		List<DBObject> resultList = new ArrayList<DBObject>();

		DBCollection dbCollection = db.getCollection(collectionName);

		cursor = dbCollection.find().skip(startPosition).limit(getNum);
		Iterator<DBObject> iter = cursor.iterator();
		
		while(iter.hasNext()) {
			resultList.add(iter.next());
		}
				
		if (null != cursor) {
			cursor.close();
		}
		
		return resultList;
	}

	@Override
	public int count(DB db, String collectionName) {
		DBCollection dbCollection = db.getCollection(collectionName);
		return dbCollection.find().count();
	}
	
	@Override
	public void close() {
		mongoClient.close();
	}
}
