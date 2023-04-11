package com.iris.interceptor;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class CIMSCustomizedServiceInterceptorConfig implements WebMvcConfigurer {

	@Autowired
	CIMSCustomizedServiceInterceptor cimsCustomizedServiceInterceptor;

	@Autowired
	JWTTokenInterceptor jwtTokenInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		java.util.List<String> pathList = new ArrayList<>();
		pathList.add("/service/**");
		//		pathList.add("/service/channelMngtController/**");
		//		pathList.add("/service/channelMngtController/**");
		registry.addInterceptor(cimsCustomizedServiceInterceptor).addPathPatterns(pathList);

		pathList = new ArrayList<>();
		pathList.add("/service/fileDetailsAPIController/**");
		registry.addInterceptor(jwtTokenInterceptor).addPathPatterns(pathList);
	}

}
