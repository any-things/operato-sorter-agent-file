/* Copyright © HatioLab Inc. All rights reserved. */
package xyz.anythings.sorter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import xyz.elidom.sys.system.config.module.IModuleProperties;
import xyz.elidom.util.FormatUtil;

/**
 * anythings-logis-sorter 모듈 정보 파일
 * 
 * @author yang
 */
@Component("anythingsLogisSorterModuleProperties")
@EnableConfigurationProperties
@PropertySource("classpath:/properties/anythings-logis-sorter.properties")
public class ModuleProperties implements IModuleProperties {
	
	/**
	 * 모듈명
	 */
	@Value("${anythings.logis.sorter.name}")
	private String name;
	
	/**
	 * 버전
	 */
	@Value("${anythings.logis.sorter.version}")
	private String version;
	
	/**
	 * Module Built Time 
	 */
	@Value("${anythings.logis.sorter.built.at}")
	private String builtAt;	
	
	/**
	 * 모듈 설명
	 */
	@Value("${anythings.logis.sorter.description}")
	private String description;
	
	/**
	 * 부모 모듈
	 */
	@Value("${anythings.logis.sorter.parentModule}")
	private String parentModule;
	
	/**
	 * 모듈 Base Package
	 */
	@Value("${anythings.logis.sorter.basePackage}")
	private String basePackage;
	
	/**
	 * Scan Service Path
	 */
	@Value("${anythings.logis.sorter.scanServicePackage}")
	private String scanServicePackage;
	
	/**
	 * Scan Entity Path
	 */
	@Value("${anythings.logis.sorter.scanEntityPackage}")
	private String scanEntityPackage;
	
	/**
	 * Project Name
	 * @return
	 */
	@Value("${anythings.logis.sorter.projectName}")
	private String projectName;
	
	public String getName() {
		return this.name;
	}

	public String getVersion() {
		return this.version;
	}
	
	public String getBuiltAt() {
		return builtAt;
	}

	public String getDescription() {
		return this.description;
	}
	
	public String getParentModule() {
		return this.parentModule;
	}

	public String getBasePackage() {
		return this.basePackage;
	}

	public String getScanServicePackage() {
		return this.scanServicePackage;
	}

	public String getScanEntityPackage() {
		return this.scanEntityPackage;
	}
	
	public String getProjectName() {
		return this.projectName;
	}

	@Override
	public String toString() {
		return FormatUtil.toJsonString(this);
	}
}