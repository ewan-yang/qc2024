package com.tecpie.platform.user.security.isc.entity;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IscUser implements Serializable {

  private static final long serialVersionUID = -6247798498796940914L;

  private String iscUserId;

  private String iscUserSourceId;

  private String name;

}
