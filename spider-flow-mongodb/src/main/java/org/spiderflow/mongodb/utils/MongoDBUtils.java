package org.spiderflow.mongodb.utils;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.internal.MongoClientImpl;
import org.apache.commons.lang3.StringUtils;
import org.spiderflow.mongodb.model.MongoDataSource;

import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class MongoDBUtils {
	
	public static MongoClient createMongoClient(MongoDataSource dataSource){
		ServerAddress address = new ServerAddress(dataSource.getHost(), dataSource.getPort());
		MongoClientSettings.Builder options = MongoClientSettings.builder()
				.applyToClusterSettings(builder -> builder.hosts(Collections.singletonList(address)))
				.applyToSocketSettings(builder -> builder.connectTimeout(5000, TimeUnit.MILLISECONDS))
				.applyToConnectionPoolSettings(builder -> builder.maxWaitTime(3000, TimeUnit.MILLISECONDS))
				.applyToClusterSettings(builder -> builder.serverSelectionTimeout(3000, TimeUnit.MILLISECONDS));

		if(StringUtils.isNotBlank(dataSource.getUsername()) && StringUtils.isNotBlank(dataSource.getPassword())){
			MongoCredential credential = MongoCredential.createScramSha1Credential(dataSource.getUsername(),dataSource.getDatabase(),dataSource.getPassword().toCharArray());
		}

		MongoClientSettings settings = options.build();
		return MongoClients.create(settings);
	}
	
}
