package com.yinker.xiaov.tivyv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.mongodb.DB;
import com.mongodb.DBObject;

public class DataTransfer implements Runnable{
	
	private static Logger logger = Logger.getLogger(DataTransfer.class);
	private String envId;
	
	public DataTransfer(String envId){
		this.envId = envId;
	}
	
	@Override
	public void run() {
		try{
			logger.info("CallDetailClient started.");
			String offsetStr = "mongodb_offset";
			int mongodbOffset = readMongodbOffset(envId, offsetStr);
			logger.info("mongodb Offset value: " + mongodbOffset);
			
			TelemetryClient client = new TelemetryClient(Configs.getConfig("KAFKA_TOPIC"), false);
			String collectionName = Configs.getConfig("MONGODB_DB_COLLECTION");
			
			//read from mongodb
			int startPosition = mongodbOffset;
			int MONGODB_PAGE_SIZE = Integer.parseInt(Configs.getConfig("MONGODB_PAGE_SIZE"));
			
			while(true) {
				
				MongoDBDaoImpl mongodb = new MongoDBDaoImpl();
				DB db = mongodb.getDB(Configs.getConfig(envId + ".MONGODB_DB_NAME"));
				//db.slaveOk();
				
				mongodbOffset = startPosition;
				
				int totalCount = mongodb.count(db, collectionName);
				logger.info("totalCount: " + totalCount);
				int getNum = totalCount - mongodbOffset;
				int getTimes = getNum / MONGODB_PAGE_SIZE;
				if (getNum % MONGODB_PAGE_SIZE != 0) {
					getTimes += 1;
				}
				
				for (int i = 0; i < getTimes && startPosition < totalCount; ++i) {
					
					boolean errFlag = false;
					
					try {
						List<DBObject> result = mongodb.query(db, collectionName, startPosition, MONGODB_PAGE_SIZE);
						try {
							for (DBObject obj : result) {
								String channel = obj.get("channel").toString();
								if (channel.equalsIgnoreCase("TongD") || channel.equalsIgnoreCase("JuXinLi")) {
									client.SendMsg(obj.get("mobile").toString(), obj.toString());
								} else {
									logger.debug(channel);
//								System.out.println(channel);
								}
							}
						} catch (Exception e) {
							client.close();
							e.printStackTrace();
							logger.error("kafka exception, restart kafka client! Exception: " + e.getMessage());
							System.err.println("kafka exception, restart kafka client!");
							errFlag = true;
						}
						
						if (errFlag) {
							Thread.sleep(Integer.parseInt(Configs.getConfig("FETCH_INTERVAL")));
							client = new TelemetryClient(Configs.getConfig("KAFKA_TOPIC"), false);
							break;
						}
						
						startPosition += result.size();
						
					} catch (RuntimeException e) {
						logger.error("Caught RuntimeException: " + e.getMessage());
						// move 1 step if reading failed
						startPosition ++;
						i -= 1;
					}			
					
					FileWriter fw = new FileWriter(new File(Configs.getConfig(envId + ".MONGODB_OFFSET_FILE_PATH")));
					try{
						logger.info("#Update mongodb_offset from " + String.valueOf(mongodbOffset) + " to " + String.valueOf(startPosition) + "\n");
						fw.write("#Update mongodb_offset from " + String.valueOf(mongodbOffset) + " to " + String.valueOf(startPosition) + "\n");
						fw.write("#" + new Date() + "\n");
						fw.write(offsetStr + "=" + startPosition);
					}finally{
						fw.flush();
						fw.close();
					}
				}
				logger.info(MONGODB_PAGE_SIZE + " messages sent.");
				
				mongodb.close();
				//load data from mongodb every 5 minutes.
				Thread.sleep(Integer.parseInt(Configs.getConfig("FETCH_INTERVAL")));
			}
		}catch(Throwable t){
			logger.error("Exception happened: " + getErrorInfoFromException(t)); 
		}
		
	}
	
	private static int readMongodbOffset(String envId, String offsetStr) throws FileNotFoundException{
		Scanner sc = new Scanner(new File(Configs.getConfig(envId + ".MONGODB_OFFSET_FILE_PATH")));
		int i = 0;
		while(sc.hasNextLine()){
			String s = sc.nextLine().trim();
			if(s.startsWith(offsetStr)){
				i = Integer.parseInt(s.replace(offsetStr + "=", ""));
				break;
			}
		}
		return i;
	}
	
	public String getErrorInfoFromException(Throwable e) {
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return "\r\n" + sw.toString() + "\r\n";
        } catch (Exception e2) {
            return "bad getErrorInfoFromException";
        }
    }

	public static void main(String[] args) throws Exception {
		ExecutorService es = Executors.newCachedThreadPool();
    	String[] envsToMonitor = Configs.getConfig("EnvIDs").split(",");
    	for (String env: envsToMonitor)
    		es.submit(new DataTransfer(env.trim()));
    	es.shutdown();
	}
}
