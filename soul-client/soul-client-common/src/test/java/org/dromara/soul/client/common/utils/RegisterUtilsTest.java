/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.soul.client.common.utils;

import lombok.SneakyThrows;
import org.dromara.soul.common.constant.AdminConstants;
import org.dromara.soul.common.enums.RpcTypeEnum;
import org.dromara.soul.common.utils.JsonUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mockStatic;

/**
 * Test case for {@link RegisterUtils}.
 *
 * @author Young Bean
 */
public final class RegisterUtilsTest {

    private OkHttpTools okHttpTools;

    private String json;

    private String url;

    @Before
    public void setUp() {
        okHttpTools = mock(OkHttpTools.class);

        Map<String, Object> jsonMap = new HashMap<String, Object>() {
            {
                put("appName", "dubbo");
                put("contextPath", "/dubbo");
                put("path", "/dubbo/findByArrayIdsAndName");
                put("pathDesc", "");
                put("serviceName", "org.dromara.soul.examples.dubbo.api.service.DubboMultiParamService");
                put("ruleName", "/dubbo/findByArrayIdsAndName");
                put("parameterTypes", "[Ljava.lang.Integer;,java.lang.String");
                put("rpcExt", "{\"group\":\"\",\"version\":\"\",\"loadbalance\":\"random\",\"retries\":2,\"timeout\":10000,\"url\":\"\"}");
                put("enabled", true);
            }
        };

        json = JsonUtils.toJson(jsonMap);
        url = "http://localhost:9095/soul-client/dubbo-register";
    }

    @SneakyThrows
    @Test
    public void testDoRegisterWhenSuccess() {
        when(okHttpTools.post(url, json)).thenReturn(AdminConstants.SUCCESS);

        try (MockedStatic<OkHttpTools> okHttpToolsMockedStatic = mockStatic(OkHttpTools.class)) {
            okHttpToolsMockedStatic.when(OkHttpTools::getInstance).thenReturn(okHttpTools);
            RegisterUtils.doRegister(json, url, RpcTypeEnum.DUBBO);
        }
    }

    @SneakyThrows
    @Test
    public void testDoRegisterWhenError() {
        when(okHttpTools.post(url, json)).thenReturn(AdminConstants.PARAMS_ERROR);

        try (MockedStatic<OkHttpTools> okHttpToolsMockedStatic = mockStatic(OkHttpTools.class)) {
            okHttpToolsMockedStatic.when(OkHttpTools::getInstance).thenReturn(okHttpTools);
            RegisterUtils.doRegister(json, url, RpcTypeEnum.DUBBO);
        }
    }

    @SneakyThrows
    @Test
    public void testDoRegisterWhenThrowException() {
        when(okHttpTools.post(url, json)).thenThrow(IOException.class);

        try (MockedStatic<OkHttpTools> okHttpToolsMockedStatic = mockStatic(OkHttpTools.class)) {
            okHttpToolsMockedStatic.when(OkHttpTools::getInstance).thenReturn(okHttpTools);
            RegisterUtils.doRegister(json, url, RpcTypeEnum.DUBBO);
        }
    }
}
