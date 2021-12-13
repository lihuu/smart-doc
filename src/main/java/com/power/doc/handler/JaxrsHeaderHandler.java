/*
 * smart-doc https://github.com/shalousun/smart-doc
 *
 * Copyright (C) 2018-2021 smart-doc
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.power.doc.handler;

import com.power.common.util.StringUtil;
import com.power.doc.builder.ProjectDocConfigBuilder;
import com.power.doc.constants.DocAnnotationConstants;
import com.power.doc.constants.DocTags;
import com.power.doc.constants.JAXRSAnnotations;
import com.power.doc.model.ApiReqParam;
import com.power.doc.utils.DocClassUtil;
import com.power.doc.utils.DocUtil;
import com.thoughtworks.qdox.model.JavaAnnotation;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaParameter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Zxq
 */
public class JaxrsHeaderHandler {

    /**
     * Handle JAX RS Header
     *
     * @param method         method
     * @param projectBuilder ProjectDocConfigBuilder
     * @return list of ApiReqParam
     */
    public List<ApiReqParam> handle(JavaMethod method, ProjectDocConfigBuilder projectBuilder) {
        Map<String, String> constantsMap = projectBuilder.getConstantsMap();

        List<ApiReqParam> apiReqHeaders = new ArrayList<>();
        List<JavaParameter> parameters = method.getParameters();
        for (JavaParameter javaParameter : parameters) {
            List<JavaAnnotation> annotations = javaParameter.getAnnotations();
            String paramName = javaParameter.getName();

            // hit target head annotation
            ApiReqParam apiReqHeader = new ApiReqParam();

            String defaultValue = "";
            for (JavaAnnotation annotation : annotations) {
                Map<String, Object> requestHeaderMap = annotation.getNamedParameterMap();
                //Obtain header default value
                defaultValue = handleDefaultValue(constantsMap, apiReqHeader, defaultValue, annotation, requestHeaderMap);

                // Obtain header value
                if (JAXRSAnnotations.JAX_HEADER_PARAM.equals(annotation.getType().getValue())) {
                    apiReqHeader.setName(getName(constantsMap, requestHeaderMap));

                    String typeName = javaParameter.getType().getValue().toLowerCase();
                    apiReqHeader.setType(DocClassUtil.processTypeNameForParams(typeName));

                    String className = method.getDeclaringClass().getCanonicalName();
                    Map<String, String> paramMap = DocUtil.getParamsComments(method, DocTags.PARAM, className);
                    String comments = paramMap.get(paramName);
                    apiReqHeader.setDesc(getComments(defaultValue, comments));
                    apiReqHeaders.add(apiReqHeader);
                }
            }
        }
        return apiReqHeaders;
    }

    private String getComments(String defaultValue, String comments) {
        if (Objects.nonNull(comments)) {
            StringBuilder desc = new StringBuilder();
            desc.append(comments);
            if (StringUtils.isNotBlank(defaultValue)) {
                desc.append("(defaultValue: ")
                        .append(defaultValue)
                        .append(")");
            }
            return desc.toString();
        }
        return "";
    }

    private String getName(Map<String, String> constantsMap, Map<String, Object> requestHeaderMap) {
        String name = StringUtil.removeQuotes((String) requestHeaderMap.get(DocAnnotationConstants.VALUE_PROP));
        Object constantsValue = constantsMap.get(name);
        if (Objects.nonNull(constantsValue)) {
            return constantsValue.toString();
        } else {
            return name;
        }
    }

    private String handleDefaultValue(Map<String, String> constantsMap, ApiReqParam apiReqHeader, String defaultValue, JavaAnnotation annotation, Map<String, Object> requestHeaderMap) {
        if (JAXRSAnnotations.JAX_DEFAULT_VALUE.equals(annotation.getType().getValue())) {
            defaultValue = StringUtil.removeQuotes((String) requestHeaderMap.get(DocAnnotationConstants.VALUE_PROP));
            Object constantsValue = constantsMap.get(defaultValue);
            if (Objects.nonNull(constantsValue)) {
                defaultValue = constantsValue.toString();
            }
            apiReqHeader.setValue(defaultValue);
        }
        return defaultValue;
    }

}