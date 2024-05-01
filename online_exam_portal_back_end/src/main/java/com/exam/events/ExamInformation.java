package com.exam.events;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;

import com.exam.util.ConstantValues;
import com.exam.util.EntityConstants;

public class ExamInformation {

    // Define a constant for the class name
    public static final String module = ExamInformation.class.getName();

    // Method to retrieve exam information
    public static String getExamInfo(HttpServletRequest request, HttpServletResponse response) {
        // Retrieve delegator and local dispatcher from the request attributes
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
        GenericValue userLogin=(GenericValue) request.getSession().getAttribute("userLogin");

        // Retrieve user ID from the request attribute
        System.out.println("userlogin-"+request.getSession().getAttribute("userLogin"));
        System.out.println("partyId-"+request.getSession().getAttribute("partyId"));
        String userid = request.getAttribute(EntityConstants.USER_LOGIN_ID).toString();
        System.out.println("userid========="+userid);

        // Log user ID for debugging
        Debug.log("=======\\\\\\\\\\\\Event values\\\\\\\\\\\\=========" + userid + "//////");

        // Validate that user ID is not empty
        if (UtilValidate.isEmpty(userid)) {
            String errMsg = "Userid is required fields on the form and can't be empty.";
            request.setAttribute("ERROR_MESSAGE", errMsg);
            return "error";
        }

        try {
            // Log the login process
            Debug.log("=======Logging in process started=========");

            // Invoke the service to get exam information
            Map<String, Object> getExamInformationresult = dispatcher.runSync("getExamInformation",
                    UtilMisc.toMap("userLogin", userLogin));

            // Check if the service call resulted in an error
            if (ServiceUtil.isError(getExamInformationresult)) {
                // Handle error scenario
                String errorMessage = ServiceUtil.getErrorMessage(getExamInformationresult);
                request.setAttribute("ERROR_MESSAGE", errorMessage);
                Debug.logError(errorMessage, module);
                return "error";
            } else {
                // Handle success scenario
                String successMessage = "getExamInformation successfully.";
                ServiceUtil.getMessages(request, getExamInformationresult, successMessage);
                request.setAttribute("exam", getExamInformationresult);
                return "success";
            }

        } catch (Exception e) {
            // Handle exceptions during service invocation
        	Debug.logError(e,module);
            String errMsg = "Error in calling or executing the service: " + e.toString();
            request.setAttribute("ERROR_MESSAGE", errMsg);
            return "error";
        }
    }
}