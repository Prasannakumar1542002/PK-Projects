package com.exam.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.ServiceUtil;

import com.exam.util.ConstantValues;

public class EnumerationEntityListEvent {
	private static final String MODULE = EnumerationEntityListEvent.class.getName();

	// Retrieve All Values From Enumeration Entity
	public static String fetchEnumerationEntityEvent(HttpServletRequest request, HttpServletResponse response) {

		Delegator delegator = (Delegator) request.getAttribute("delegator");
		List<Map<String, Object>> enumerationdata = new ArrayList<Map<String, Object>>();
		try {
			// Query to retrieve data's from Enumeration Entity
			List<GenericValue> listOfEnumerationData = EntityQuery.use(delegator).from("Enumeration")
					.where(ConstantValues.ENUM_TYPE_ID, "Q_TYPE").cache().queryList();
			if (UtilValidate.isNotEmpty(listOfEnumerationData)) {
				for (GenericValue list : listOfEnumerationData) {
					Map<String, Object> listOfEnumerationEntity = new HashMap<String, Object>();
					listOfEnumerationEntity.put(ConstantValues.ENUM_ID, list.get(ConstantValues.ENUM_ID));
					listOfEnumerationEntity.put(ConstantValues.ENUM_SEQUENCE_ID,
							list.get(ConstantValues.ENUM_SEQUENCE_ID));
					listOfEnumerationEntity.put(ConstantValues.ENUM_TYPE_ID, list.get(ConstantValues.ENUM_TYPE_ID));
					listOfEnumerationEntity.put(ConstantValues.ENUM_DESCRIPTION,
							list.get(ConstantValues.ENUM_DESCRIPTION));
					enumerationdata.add(listOfEnumerationEntity);
				}
				request.setAttribute("EnumerationData", enumerationdata);
				return "success";
			} else {
				String errorMessage = "No matched fields in Enumeration Entity";
				request.setAttribute("_ERROR_MESSAGE_", errorMessage);
				Debug.logError(errorMessage, MODULE);
				return "error";
			}
		} catch (GenericEntityException e) {
			request.setAttribute("Error", e);
			return "error";
		}
	}
}
