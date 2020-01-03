/* Copyright © HatioLab Inc. All rights reserved. */
package xyz.anythings.sorter.web.initializer;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import xyz.anythings.sorter.config.ModuleProperties;
import xyz.anythings.sorter.tcp.TcpConfigConstants;
import xyz.anythings.sorter.tcp.TcpServiceManager;
import xyz.elidom.sys.config.ModuleConfigSet;
import xyz.elidom.sys.system.service.api.IEntityFieldCache;
import xyz.elidom.sys.system.service.api.IServiceFinder;
import xyz.elidom.util.ThreadUtil;
import xyz.elidom.util.ValueUtil;

/**
 * Anythings Logis Sorter Startup시 Framework 초기화 클래스 
 * 
 * @author park
 */
@Component
public class AnythingsLogisSoterInitializer {

	/**
	 * Logger
	 */
	private Logger logger = LoggerFactory.getLogger(AnythingsLogisSoterInitializer.class);
	
	@Autowired
	@Qualifier("rest")
	private IServiceFinder restFinder;
	
	@Autowired
	private IEntityFieldCache entityFieldCache;
	
	@Autowired
	private ModuleProperties module;
	
	@Autowired
	private ModuleConfigSet configSet;
	
	@Autowired
	private Environment env;

	@Autowired
	private TcpServiceManager tcpServiceManager;
	
	@EventListener({ ContextRefreshedEvent.class })
	public void refresh(ContextRefreshedEvent event) {
		this.logger.info("Anythings Logistics Sorter module refreshing...");
		
		this.logger.info("Anythings Logistics Sorter module refreshed!");
	}
	
	@EventListener({ApplicationReadyEvent.class})
    void ready(ApplicationReadyEvent event) {
		this.logger.info("Anythings Logistics Sorter module initializing...");
		
		this.configSet.addConfig(this.module.getName(), this.module);
		this.configSet.setApplicationModule(this.module.getName());
		this.scanServices();
		
		// 2. TCP 서버 구동
		this.startTcpServers();
		
		// 3. TCP 클라이언트 구동
//		this.startTcpClients();
				
		this.logger.info("Anythings Logistics Sorter module initialized!");
    }
	
	/**
	 * 모듈 서비스 스캔 
	 */
	private void scanServices() {
		this.entityFieldCache.scanEntityFieldsByBasePackage(this.module.getBasePackage());
		this.restFinder.scanServicesByPackage(this.module.getName(), this.module.getBasePackage());
	}
	
	/**
	 * TCP Server 구동.
	 */
	private void startTcpServers() {
		String ports = env.getProperty(TcpConfigConstants.TCP_SERVER_PORT_LIST);
		
		if (!ValueUtil.isEmpty(ports)) {
			String[] portArr = StringUtils.tokenizeToStringArray(ports, ",");
			for (String port : portArr) {
				Integer serverPort = ValueUtil.toInteger(port);
				
				if (serverPort != null) {
					ThreadUtil.doAsynch(() -> tcpServiceManager.startServer(serverPort));
				}
			}
		}
	}
	
	/**
	 * TCP Client 구동.
	 */
	private void startTcpClients() {
		String ipStr = env.getProperty(TcpConfigConstants.TCP_IP_LIST_FOR_CLIENT);
		String portStr = env.getProperty(TcpConfigConstants.TCP_PORT_LIST_FOR_CLIENT);
		
		if (!ValueUtil.isEmpty(portStr)) {
			String[] ipArr = StringUtils.tokenizeToStringArray(ipStr, ",");
			String[] portArr = StringUtils.tokenizeToStringArray(portStr, ",");

			for (int i = 0 ; i < ipArr.length ; i++) {
				String ip = ipArr[i];
				String port = portArr[i];
				Integer clientPort = ValueUtil.toInteger(port);
				
				if (clientPort != null) {
					ThreadUtil.doAsynch(() -> tcpServiceManager.startClient(ip, clientPort));
				}
			}
		}
	}
}