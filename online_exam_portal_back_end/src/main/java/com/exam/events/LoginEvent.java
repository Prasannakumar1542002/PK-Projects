package com.exam.events;

import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilHttp;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.webapp.control.LoginWorker;

import com.exam.forms.HibernateValidation;
import com.exam.forms.checks.LoginFormCheck;
import com.exam.helper.HibernateHelper;
import com.exam.util.ConstantValues;
import com.exam.util.EntityConstants;

public class LoginEvent {

	public static final String module = LoginEvent.class.getName();
	public static String resource_error = "OnlineExamPortalUiLabels";

	// Method to Login
	public static String dologin(HttpServletRequest request, HttpServletResponse response) {
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");

		Locale locale = UtilHttp.getLocale(request);
		String username = request.getAttribute(EntityConstants.CAPS_USER_NAME).toString();
		String password = request.getAttribute(EntityConstants.CAPS_PASSWORD).toString();

		Map<String, Object> userLoginMap = UtilMisc.toMap(EntityConstants.USER_LOGIN_ID, username, EntityConstants.PASSWORD, password);

		Debug.log("=======\\\\\\\\\\\\Event values\\\\\\\\\\\\=========" + username + "\\\\\\\\\\\\" + password);

		try {
			Debug.log("=======Logging in process started=========", module);
			System.out.println("Event Delegator-" + delegator);
			HibernateValidation Hibernet = HibernateHelper.populateBeanFromMap(userLoginMap, HibernateValidation.class);
			Set<ConstraintViolation<HibernateValidation>> errors = HibernateHelper.checkValidationErrors(Hibernet,
					LoginFormCheck.class);
			boolean hasFormErrors = HibernateHelper.validateFormSubmission(delegator, errors, request, locale,
					"MandatoryFieldErrMsgLoginForm", resource_error, false);
			if (!hasFormErrors) {
				String result = LoginWorker.login(request, response);
				if (result.equals("success")) {
					request.setAttribute("_EVENT_MESSAGE_", "Login succesfully.");
					
					// Query to retrieve data from UserLogin Entity
					GenericValue userid = EntityQuery.use(delegator).from("UserLogin").where(EntityConstants.USER_LOGIN_ID, username)
							.cache().queryOne();
					String partyid = userid.getString("partyId");
					
					//Query to retrieve data's from PartyRole Entity
					List<GenericValue> role = EntityQuery.use(delegator).from("PartyRole").where(ConstantValues.USEREXAM_PARTY_ID, partyid)
							.cache().queryList();
					request.setAttribute("Role", role.get(1).getString("roleTypeId"));
					request.getSession().setAttribute("partyId", partyid);
				}
				request.setAttribute("result", result);
				System.out.println("result======================" + result);
			} else {
				request.setAttribute("_ERROR_MESSAGE_", "The given information does not pass the hybernet validation");
			}
			request.setAttribute("hibernatresult", hasFormErrors);
		} catch (Exception e) {
			String errMsg = "Error in calling or excecuting the service: " + e.toString();
			request.setAttribute("_ERROR_MESSAGE_", errMsg);
			return "error";
		}
		return "success";
	}
}