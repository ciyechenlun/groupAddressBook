package com.cmcc.zysoft.groupaddressbook.util;


import java.io.IOException;  
import java.io.InputStream;  
import java.io.InputStreamReader;  
import java.security.Key;  
import java.util.Properties;  
  
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;  
import org.springframework.core.io.Resource;  
import org.springframework.util.DefaultPropertiesPersister;  
import org.springframework.util.PropertiesPersister;  

  
public class DecryptPropertyPlaceholderConfigurer extends  
		PropertyPlaceholderConfigurer {

	private static Logger logger = LoggerFactory
			.getLogger(DecryptPropertyPlaceholderConfigurer.class);
	private Resource[] locations;

	private Resource keyLocation;

	private String fileEncoding;

	public void setKeyLocation(Resource keyLocation) {
		this.keyLocation = keyLocation;
	}

	public void setLocations(Resource[] locations) {
		this.locations = locations;
	}

	public void loadProperties(Properties props) throws IOException {
		if (this.locations != null) {
			PropertiesPersister propertiesPersister = new DefaultPropertiesPersister();
			for (int i = 0; i < this.locations.length; i++) {
				Resource location = this.locations[i];
				if (logger.isInfoEnabled()) {
					logger.info("Loading properties file from " + location);
				}
				InputStream is = null;
				try {
					is = location.getInputStream();
					Key key = DESEncryptUtil.getKey(keyLocation
							.getInputStream());
					is = DESEncryptUtil.doDecrypt(key, is);
					if (fileEncoding != null) {
						propertiesPersister.load(props, new InputStreamReader(
								is, fileEncoding));
					} else {
						propertiesPersister.load(props, is);
					}
				} finally {
					if (is != null) {
						is.close();
					}
				}
			}
		}
	}
}
