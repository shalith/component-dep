/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * 
 * WSO2.Telco Inc. licences this file to you under  the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.wso2telco.oneapivalidation.service.impl.ussd;

import com.wso2telco.oneapivalidation.exceptions.CustomException;
import com.wso2telco.oneapivalidation.service.IServiceValidate;
import com.wso2telco.oneapivalidation.util.UrlValidator;
import com.wso2telco.oneapivalidation.util.Validation;
import com.wso2telco.oneapivalidation.util.ValidationRule;
import org.json.JSONObject;

 
// TODO: Auto-generated Javadoc
/**
 * The Class ValidateUssdSubscription.
 */
public class ValidateUssdSubscription implements IServiceValidate {

    /** The validation rules. */
    private final String[] validationRules = {"ussd", "*", "inbound", "subscriptions"};

    /* (non-Javadoc)
     * @see com.wso2telco.oneapivalidation.service.IServiceValidate#validate(java.lang.String)
     */
    public void validate(String json) throws CustomException {


        String callbackData = null;
        String notifyUrl = null;
        String destinationAddress = null;
        String clientCorrelator = null;
        String resourceUrl = null;

        try {
            JSONObject objJSONObject = new JSONObject(json);
            JSONObject requestData = objJSONObject.getJSONObject("subscription");

            if (requestData.has("destinationAddress")) {
                destinationAddress = nullOrTrimmed(requestData.getString("destinationAddress"));
            }
            if (requestData.has("clientCorrelator")) {
                clientCorrelator = nullOrTrimmed(requestData.getString("clientCorrelator"));
            }
            if (requestData.has("resourceURL")) {
                resourceUrl = nullOrTrimmed(requestData.getString("resourceURL"));
            }

            if (requestData.has("callbackReference")) {
                JSONObject callbackReference = requestData.getJSONObject("callbackReference");

                if (callbackReference.has("callbackData")) {
                    callbackData = nullOrTrimmed(callbackReference.getString("callbackData"));
                }
                if (callbackReference.has("notifyURL")) {
                    notifyUrl = nullOrTrimmed(callbackReference.getString("notifyURL"));
                }
            }

        } catch (Exception e) {

            System.out.println("Manipulating recived JSON Object: " + e);
            throw new CustomException("POL0299", "Unexpected Error", new String[]{""});
        }

        ValidationRule[] rules = null;

        rules = new ValidationRule[]{
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "destinationAddress", destinationAddress),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "resourceURL", resourceUrl),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "clientCorrelator", clientCorrelator),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "notifyURL", notifyUrl),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "callbackData", callbackData),};
        
        Validation.checkRequestParams(rules);
    }

    /* (non-Javadoc)
     * @see com.wso2telco.oneapivalidation.service.IServiceValidate#validateUrl(java.lang.String)
     */
    public void validateUrl(String pathInfo) throws CustomException {
        String[] requestParts = null;
        if (pathInfo != null) {
            if (pathInfo.startsWith("/")) {
                pathInfo = pathInfo.substring(1);
            }
            requestParts = pathInfo.split("/");
        }

        UrlValidator.validateRequest(requestParts, validationRules);

    }

    /* (non-Javadoc)
     * @see com.wso2telco.oneapivalidation.service.IServiceValidate#validate(java.lang.String[])
     */
    public void validate(String[] params) throws CustomException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /**
     * Null or trimmed.
     *
     * @param s the s
     * @return the string
     */
    private static String nullOrTrimmed(String s) {
        String rv = null;
        if (s != null && s.trim().length() > 0) {
            rv = s.trim();
        }
        return rv;
    }
}