package com.exam.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.lucene.analysis.CharArrayMap.EntrySet;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.LocalDispatcher;

public class UserResult {
	public static String getUserResult(HttpServletRequest request, HttpServletResponse response) {
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
//		String performanceId=(String) request.getSession().getAttribute("performanceId");
//		System.out.println("performanceId------->"+performanceId);
		try {
			Map<String, Object> ans = (Map<String, Object>) request.getAttribute("answers");
			for (Entry<String, Object> entry : ans.entrySet()) {
				System.out.println("--------->" + entry);
				List<List<Map<String, Object>>> re = (List<List<Map<String, Object>>>) request
						.getAttribute("questions");
				for (List<Map<String, Object>> re1 : re) {
					for (Map<String, Object> re2 : re1) {
						if (entry.getKey().equals(re2.get("questionId"))) {
							String selectedOption=(String) re2.get(entry.getValue());
							System.out.println("selectedOption====="+selectedOption);
							String quesId=(String) re2.get("questionId");
							System.out.println("quesId====="+quesId);
//							Map<String, Object> result2 = dispatcher.runSync("updateUserAttemptTopicMaster",
//									UtilMisc.toMap());
						}
							 
				
				
					}
				}
			
		}
		
		

	}catch (Exception e) {
		// TODO: handle exception
	}
		return null;
	}
}
