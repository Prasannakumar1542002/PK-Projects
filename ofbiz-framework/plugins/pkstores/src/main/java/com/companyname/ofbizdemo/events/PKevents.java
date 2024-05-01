package com.companyname.ofbizdemo.events;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;

public class PKevents {

 public static final String module = PKevents.class.getName();

    public static String createOfbizDemoEvent(HttpServletRequest request, HttpServletResponse response) {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
        GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");

        String user = request.getParameter("user");
        String password = request.getParameter("password");
        Debug.log("=======\\\\\\\\\\\\Event values\\\\\\\\\\\\========="+user+"\\\\\\\\\\\\"+password);
        if (UtilValidate.isEmpty(user) || UtilValidate.isEmpty(password)) {
            String errMsg = "First Name and Last Name are required fields on the form and can't be empty.";
            request.setAttribute("_ERROR_MESSAGE_", errMsg);
            return "error";
        }
        String comments = request.getParameter("comments");

        try {
            Debug.log("=======Creating OfbizDemo record in event using service pkservice=========", module);
            Map<String, Object> result = dispatcher.runSync("Sim", UtilMisc.toMap("user", user,
                    "password", password,"userLogin", userLogin));
            System.out.println("result======================" + result);
            request.setAttribute("result", result);
        } catch (GenericServiceException e) {
            String errMsg = "Unable to create new records in OfbizDemo entity: " + e.toString();
            request.setAttribute("_ERROR_MESSAGE_", errMsg);
            return "error";
        }
        request.setAttribute("_EVENT_MESSAGE_", "OFBiz Demo created succesfully.");
        return "success";
    }
}