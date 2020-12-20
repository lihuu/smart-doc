/*
 * smart-doc
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
package com.power.doc.model;

/**
 * @since 1.9.8
 * @author yu 2020/11/5.
 */
public class ResponseBodyAdvice {

    private String className;

    private Class wrapperClass;

    private String dataField;

    public static ResponseBodyAdvice builder(){
        return new ResponseBodyAdvice();
    }

    public String getClassName() {
        return className;
    }

    public ResponseBodyAdvice setClassName(String className) {
        this.className = className;
        return this;
    }

    public String getDataField() {
        return dataField;
    }

    public ResponseBodyAdvice setDataField(String dataField) {
        this.dataField = dataField;
        return this;
    }

    public Class getWrapperClass() {
        return wrapperClass;
    }

    public ResponseBodyAdvice setWrapperClass(Class wrapperClass) {
        this.wrapperClass = wrapperClass;
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"className\":\"")
                .append(className).append('\"');
        sb.append(",\"dataField\":\"")
                .append(dataField).append('\"');
        sb.append('}');
        return sb.toString();
    }
}