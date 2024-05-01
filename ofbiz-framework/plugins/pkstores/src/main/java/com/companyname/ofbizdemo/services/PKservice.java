package com.companyname.ofbizdemo.services;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.ServiceUtil;

public class PKservice {

	public static final String module = PKservice.class.getName();

	public static Map<String, Object> createOfbizDemo(DispatchContext dctx, Map<String, ? extends Object> context) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> dataResult = new HashMap<String, Object>();
		System.out.println("Entered....");
		for (Map.Entry<String, ? extends Object> entry : context.entrySet()) {
			Debug.log("/////////////////////////Key = " + entry.getKey() + ", Value = " + entry.getValue().toString()+"//////////////////////////////");
		}
		try {
			String user = (String) context.get("user");
			String pass = (String) context.get("password");
			if (user.equals("prasanna")) {
				if (pass.equals("kowsi")) {
					dataResult.put("dataresult", "success");
					result.put("dataResultStatus", dataResult);
					Debug.log("=======pkservice success=========");
					return result;
				}
			}
			Debug.log(
					"==========This is my first Java Service implementation in Apache OFBiz. OfbizDemo record created successfully with ofbizDemoId: ");
			result.put("status", "failed");
			System.out.println("resultservice"+result);
			return result;
		} catch (Exception e) {
			Debug.logError(e, module);
			return ServiceUtil.returnError("Error in creating record in OfbizDemo entity ........" + module);
		}
	}
}