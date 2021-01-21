/************************************************************************
* Defining the Configuration.
* WHAT:
*     class as Configuration
*
*
* WHEN         WHO       WHY
* 2021-01-18   Bala      Created
/************************************************************************/
package com.newdream.poc.springservice.postservice;

import com.azure.storage.blob.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class BeanConfig {

	@Autowired
	private Environment env;
 	
	// Builder API to help aid the configuration and instantiation of BlobClients
	@Bean
	public BlobClientBuilder getClient() { 
		
		// Return the property value associated with the given key
		String property = env.getProperty("azure.storage.ConnectionString");
		String containerName = env.getProperty("azure.storage.container.name");
		
		// Creates a builder instance that is able to configure and construct BlobClient
		BlobClientBuilder client = new BlobClientBuilder();
		client.connectionString(property);
		client.containerName(containerName);
		
		return client;
	}
}
