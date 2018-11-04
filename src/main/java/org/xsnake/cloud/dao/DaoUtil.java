package org.xsnake.cloud.dao;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.xsnake.cloud.common.search.Page;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;

/**
 * 
 * @author Jerry.Zhao
 * 具名DAO查询常用类，本类中都为具名方式查询
 * 封装的DAO的最上层操作,主要分为两大类方式，SQL模板查询，以及一般SQL查询，
 * SQL模板查询：SQL模板可以是一个FreeMarker语法支持的模板，首先通过FreeMarker解析，然后将解析后的内容作为SQL语句进行查询
 *              所有的模板查询都以$符号开始的方法表示
 * 
 * 支持两种返回值:Bean返回值与Map返回值
 * 查询分为:1、单个对象或者数值查询，queryObject
 *         2、列表对象查询  queryList
 *         3、分页对象查询 queryPage
 * 
 */
@org.springframework.context.annotation.Configuration
@RefreshScope
public class DaoUtil {

	private FreeMarkerConfigurer config = new FreeMarkerConfigurer();
	private StringTemplateLoader loader = new StringTemplateLoader();
	private Configuration freemarkerTemplateConfiguration;

	@Value(value = "${jdbc.sqlTemplateURI}")
	String sqlTemplateURI;
	
	@Autowired
	NamedDaoTemplate namedDaoTemplate;

	private void putFileTemplate(File f) throws IOException {
		if(f.isFile()){
			String sql = new String(Files.readAllBytes(f.toPath()));
			loader.putTemplate(f.getName(), sql);
		}
	}

	public void scanPackage(String basePackage){
		if(StringUtils.isEmpty(basePackage)){
			return ;
		}
		//扫描符合条件的接口
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		String DEFAULT_RESOURCE_PATTERN = "**/*.sql";
		String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + basePackage.replace('.', '/') + "/" + DEFAULT_RESOURCE_PATTERN;
		Resource[] resources = null;
		
		try {
			resources = resourcePatternResolver.getResources(packageSearchPath);
		} catch (IOException e) {
			e.printStackTrace();
			throw new BeanCreationException("包扫描失败:"+e.getMessage());
		}
		
		for (Resource resource : resources) {
			if (resource.isReadable()) {
				try {
					putFileTemplate(resource.getFile());
				}
				catch (Throwable ex) {
					throw new BeanDefinitionStoreException(
							"Failed to read candidate component class: " + resource, ex);
				}
			}
		}
	}
	
	@PostConstruct
	private void afterPropertiesSet() throws Exception {
		scanPackage(sqlTemplateURI);
		freemarkerTemplateConfiguration = config.createConfiguration();
		freemarkerTemplateConfiguration.setTemplateLoader(loader);
	}

	private String getSqlByTemplateName(String templateName,Object paramObj) {
		try {
			if(paramObj instanceof Map){
				return FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplateConfiguration.getTemplate(templateName), paramObj);
			}else{
				return FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplateConfiguration.getTemplate(templateName), BeanMap.create(paramObj));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("模板出错");
		}
	}
	
	public String processTemplate(String freemarkTemlate,Object paramObj){
		String templateName = UUID.randomUUID().toString();
		loader.putTemplate(templateName, freemarkTemlate);
		try {
			if(paramObj instanceof Map){
				return FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplateConfiguration.getTemplate(templateName), paramObj);
			}else{
				return FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplateConfiguration.getTemplate(templateName), BeanMap.create(paramObj));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("模板SQL出错：" + freemarkTemlate);
		} finally{
			loader.removeTemplate(templateName);
		}
	}
	
	//模板+类返回
	public <T> T $queryObject(String templateName,Object paramObj,Class<T> requiredType) {
		return namedDaoTemplate.queryForObject(getSqlByTemplateName(templateName, paramObj),paramObj,requiredType);
	}
	
	public <T> List<T> $queryList(String templateName,Object paramObj,Class<T> requiredType) {
		return namedDaoTemplate.queryForList(getSqlByTemplateName(templateName, paramObj),paramObj,requiredType);
	}
	
	public <T> Page<T> $queryPage(String templateName,Object paramObj, int page ,int rows , Class<T> requiredType){
		return namedDaoTemplate.queryForPage(getSqlByTemplateName(templateName, paramObj),paramObj,page , rows,requiredType);
	}

	public int $update(String templateName,Object paramObj) {
		return namedDaoTemplate.update(getSqlByTemplateName(templateName, paramObj), paramObj);
	}

	
	//sql+类返回
	public <T> T queryObject(String sql,Object paramObj,Class<T> requiredType) {
		return namedDaoTemplate.queryForObject(sql,paramObj,requiredType);
	}
	
	public <T> List<T> queryList(String sql,Object paramObj,Class<T> requiredType) {
		return namedDaoTemplate.queryForList(sql,paramObj,requiredType);
	}
	
	public <T> Page<T> queryPage(String sql,Object paramObj, int page ,int rows , Class<T> requiredType){
		return namedDaoTemplate.queryForPage(sql,paramObj,page , rows,requiredType);
	}

	public int update(String sql,Object paramObj) {
		return namedDaoTemplate.update(sql, paramObj);
	}

	
	//SQL+Map返回
	public Map<String,Object> queryObject(String sql,Object paramObj) {
		return namedDaoTemplate.queryForObject(sql,paramObj);
	}
	
	public List<Map<String,Object>> queryList(String sql,Object paramObj) {
		return namedDaoTemplate.queryForList(sql,paramObj);
	}
	
	public Page<Map<String,Object>> queryPage(String sql,Object paramObj, int page ,int rows ){
		return namedDaoTemplate.queryForPage(sql,paramObj,page , rows);
	}
	
	//模板+Map返回
	public Map<String,Object> $queryObject(String templateName,Object paramObj) {
		return namedDaoTemplate.queryForObject(getSqlByTemplateName(templateName, paramObj),paramObj);
	}
	
	public List<Map<String,Object>> $queryList(String templateName,Object paramObj) {
		return namedDaoTemplate.queryForList(getSqlByTemplateName(templateName, paramObj),paramObj);
	}
	
	public Page<Map<String,Object>> $queryPage(String templateName,Object paramObj, int page ,int rows){
		return namedDaoTemplate.queryForPage(getSqlByTemplateName(templateName, paramObj),paramObj,page , rows);
	}

	public HashMapX createMap(){
		HashMapX map = new HashMapX();
		return map;
	}
	
	public HashMapX createMap(String key,Object value){
		HashMapX map = new HashMapX();
		map.put(key, value);
		return map;
	}
	
	public static class HashMapX extends HashMap<String, Object>{
		private static final long serialVersionUID = 1L;
		@Override
		public HashMapX put(String key, Object value) {
			super.put(key, value);
			return this;
		}
	}
	
}
