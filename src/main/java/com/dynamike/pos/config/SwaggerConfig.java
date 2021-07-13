/***********************************************************
 * Copyright 2017 VMware, Inc.  All rights reserved.
***********************************************************/

package com.dynamike.pos.config;

import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.google.common.base.Predicate;

import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

//@Configuration
//@EnableSwagger2
public class SwaggerConfig {

//    @Autowired
    private Environment environment;

    public static final String API_TITLE = "VMware Solution Designer API Version 1.0";
    public static final String API_DESRIPTION = "VMware solution and related data APIs for VMware Solution Designer";
    public static final String API_TERMS_OF_SERVICE = "By using this API, you consent to be bound by the VMware APIs Terms of Service";
    public static final String API_LICENSE_URL = "www.vmware.com";
    public static final Contact API_CONTACT = new Contact("GTO Tools", "https://confluence.eng.vmware.com/display/VMSD/VMware+Solution+Designer+Home", "vsdadmin@vmware.com");

    public static final String API_PACKAGE = "com.dynamike.pos.controller";

    public ApiInfo getApiInfo() {
//        String profiles = environment != null && environment.getActiveProfiles() != null ? (" (" + StringUtils.join(environment.getActiveProfiles()) + ")") : "";
        return new ApiInfoBuilder().title(API_TITLE)
//                .description(API_DESRIPTION + profiles)
                .termsOfServiceUrl(API_TERMS_OF_SERVICE)
                .contact(API_CONTACT)
                .licenseUrl(API_LICENSE_URL)
                .version("1.0")
                .build();
    }

    @Bean
    public Docket api() {
        Predicate<RequestHandler> apipackages = RequestHandlerSelectors.basePackage(API_PACKAGE);
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.any())
                .apis(apipackages)
                .build().apiInfo(getApiInfo()).useDefaultResponseMessages(false);
    }
}
