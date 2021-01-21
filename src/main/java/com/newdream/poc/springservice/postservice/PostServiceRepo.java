/************************************************************************
* Defining the Post Service repository.
* WHAT:
*     class as Repository
*
*
* WHEN         WHO       WHY
* 2021-01-18   Bala      Created
/************************************************************************/
package com.newdream.poc.springservice.postservice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostServiceRepo extends JpaRepository<StoreItems,Long> {

}
