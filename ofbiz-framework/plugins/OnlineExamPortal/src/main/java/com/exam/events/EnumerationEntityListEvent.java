package com.exam.events;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilHttp;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import com.exam.util.ConstantValues;
import com.exam.util.EntityConstants;

public class EnumerationEntityListEvent {
	private static final String MODULE = EnumerationEntityListEvent.class.getName();
	private static final String RES_ERR = "OnlineExamPortalUiLabels";
	// Retrieve All Values From Enumeration Entity
	public static String fetchQuesType(HttpServletRequest request, HttpServletResponse response) {

		Delegator delegator = (Delegator) request.getAttribute(EntityConstants.DELEGATOR);
		List<Map<String, Object>> viewQuesTypeList = new ArrayList<Map<String, Object>>();
		try {
			// Query to retrieve data's from Enumeration Entity
			List<GenericValue> listOfQuesType = EntityQuery.use(delegator).from(EntityConstants.ENUMERTION)
					.where(ConstantValues.ENUM_TYPE_ID, "Q_TYPE").cache().queryList();
			if (UtilValidate.isNotEmpty(listOfQuesType)) {
				for (GenericValue quesType : listOfQuesType) {
					Map<String, Object> quesTypeList = new HashMap<String, Object>();
					quesTypeList.put(ConstantValues.ENUM_ID, quesType.get(ConstantValues.ENUM_ID));
					quesTypeList.put(ConstantValues.ENUM_SEQUENCE_ID,
							quesType.get(ConstantValues.ENUM_SEQUENCE_ID));
					quesTypeList.put(ConstantValues.ENUM_TYPE_ID, quesType.get(ConstantValues.ENUM_TYPE_ID));
					quesTypeList.put(ConstantValues.ENUM_DESCRIPTION,
							quesType.get(ConstantValues.ENUM_DESCRIPTION));
					viewQuesTypeList.add(quesTypeList);
				}
				Map<String, Object> questionTypeInfo = new HashMap<>();
				questionTypeInfo.put("QuestionTypeList", viewQuesTypeList);
				request.setAttribute("QuestionTypeInfo", questionTypeInfo);
				return "success";
			} else {
				String errorMessage = UtilProperties.getMessage(RES_ERR, "EnumerationEntityRecordNotFoundError", UtilHttp.getLocale(request));//"No records available for the field enumId=\"Q_TYPE\" in Enumeration Entity";
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
