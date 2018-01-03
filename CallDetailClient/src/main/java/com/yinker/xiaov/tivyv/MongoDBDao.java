package com.yinker.xiaov.tivyv;

import java.util.List;

import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.client.MongoDatabase;

public interface MongoDBDao {
	
	/**
	 * 
	 * @param dbName
	 * @return
	 */
	public DB getDB(String dbName);
		
	/**
	 * 
	 * @param //page
	 * @param //pageSize
	 * @param //lastId
	 * @return
	 */
	public List<DBObject> query(DB db, String collectionName, int startPosition, int getNum);
	
	/**
	 * 
	 * @param //dbName
	 * @param collectionName
	 * @return
	 */
	public int count(DB db, String collectionName);
	
	/**
	 * 
	 * @param //db
	 */
	public void close();
}
