/************************************************************************
* Defining the PostService.
* WHAT:
*     class as Interface
*
*
* WHEN         WHO       WHY
* 2021-01-18   Bala      Created
/************************************************************************/
package com.newdream.poc.springservice.postservice;

public interface PostService {

	public boolean toAzureFileStorage(String url);	
	public boolean fromAzureFileStorage();	
	public boolean toAzureSQL();
}
