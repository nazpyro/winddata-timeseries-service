/*
 * Copyright (c) 2015 General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */

package com.ge.predix.solsvc.winddata.impl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.ge.predix.solsvc.timeseries.bootstrap.config.DefaultTimeseriesConfig;
import com.ge.predix.solsvc.timeseries.bootstrap.config.ITimeseriesConfig;

/**
 * Properties needed to make rest calls to a second Time Series instance
 * Please uncomment the @Component and add any needed properties to config/application.properties or manifest.yml
 * 
 * @author 212421693
 */
//@Component("secondaryTimeseriesConfig")
public class SecondaryTimeseriesConfig extends DefaultTimeseriesConfig
        implements EnvironmentAware, ITimeseriesConfig
{
    	private static Logger log = LoggerFactory.getLogger(SecondaryTimeseriesConfig.class);

    /**
     * @param oauthIssuerId the oauthIssuerId to set
     */
    @Override
    @Value("${predix.timeseries2.oauth.issuerId.url}")
    public void setOauthIssuerId(String oauthIssuerId)
    {
        super.setOauthIssuerId(oauthIssuerId);
    }

    /**
     * @param oauthClientId the oauthClientId to set
     */
    @Override
    @Value("${predix.timeseries2.oauth.clientId:#{null}}")
    public void setOauthClientId(String oauthClientId)
    {
        super.setOauthClientId(oauthClientId);
    }

    /**
     * @param queryUrl the queryUrl to set
     */
    @Override
    @Value("${predix.timeseries2.queryUrl}")
    public void setQueryUrl(String queryUrl)
    {
        super.setQueryUrl(queryUrl);
    }

    @Override
    /**
     * @param wsUri the wsUri to set
     */
    @Value("${predix.timeseries2.websocket.uri}")
    public void setWsUri(String wsUri)
    {
        super.setWsUri(wsUri);
    }


    /**
     * @param zoneId -
     */
    @Override
    @Value("${predix.timeseries2.zoneid}")
    public void setZoneId(String zoneId)
    {
        super.setZoneId(zoneId);
    }

    

    /**
     * The name of the VCAP property holding the name of the bound time series endpoint
     */
    public static final String TIME_SERIES2_VCAPS_NAME = "predix_timeseries2_name";   //$NON-NLS-1$
    private static final String UAA2_VCAPS_NAME = "predix_timeseries2_uaa_name";   //$NON-NLS-1$

    /*
     * (non-Javadoc)
     * @see org.springframework.context.EnvironmentAware#setEnvironment(org.
     * springframework.core.env.Environment)
     */
    @SuppressWarnings("nls")
    @Override
    public void setEnvironment(Environment env)
    {
        String vcapPropertyName = null;
        String uaaName = env.getProperty(UAA2_VCAPS_NAME); // this is set on the manifest of the application

        vcapPropertyName = null;
        vcapPropertyName = "vcap.services." + uaaName + ".credentials.issuerId";
        if ( !StringUtils.isEmpty(env.getProperty(vcapPropertyName)) ) {
        	log.debug("env var found for oauthIssuerId=" + env.getProperty(vcapPropertyName) );
            this.setOauthIssuerId(env.getProperty(vcapPropertyName));
        }
        
        String tsName = env.getProperty(TIME_SERIES2_VCAPS_NAME); // this is set
                                                                 // on the
                                                                 // manifest
                                                                 // of the
                                                                 // application
        vcapPropertyName = null;
        vcapPropertyName = "vcap.services." + tsName + ".credentials.query.uri";
        if ( !StringUtils.isEmpty(env.getProperty(vcapPropertyName)) )
        {
            this.setQueryUrl(env.getProperty(vcapPropertyName));

        }

        vcapPropertyName = "vcap.services." + tsName + ".credentials.query.zone-http-header-value";
        if ( !StringUtils.isEmpty(env.getProperty(vcapPropertyName)) )
        {
            this.setZoneId(env.getProperty(vcapPropertyName));
        }
        
        // set ingest.uri
        if (StringUtils.isNotBlank(env.getProperty("vcap.services." + tsName + ".credentials.ingest.uri"))) //$NON-NLS-1$ //$NON-NLS-2$
        {
            this.setWsUri(env.getProperty("vcap.services." + tsName + ".credentials.ingest.uri")); //$NON-NLS-1$ //$NON-NLS-2$
        }

    }

 
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @SuppressWarnings("nls")
    @Override
    public String toString()
    {
        return "SecondaryTimeseriesConfig [queryUrl=" + this.getQueryUrl() + "] " + super.toString();
    }
}
