package com.exam.events;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;

import org.apache.ofbiz.base.util.UtilHttp;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;

import com.exam.forms.HibernateValidation;
import com.exam.forms.checks.RegisterFormCheck;
import com.exam.helper.HibernateHelper;
import com.exam.util.EntityConstants;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class RegistrationEvent {
	public static final String module = RegistrationEvent.class.getName();
	public static String resource_error = "OnlineExamPortalUiLabels";

	public static String register(HttpServletRequest request, HttpServletResponse response) {
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");

		Locale locale = UtilHttp.getLocale(request);
		Map<String, Object> combinedMap = UtilHttp.getCombinedMap(request);
		System.out.println("Attributes : " + combinedMap);
		String username = combinedMap.get(EntityConstants.CAPS_USER_NAME).toString();
		String firstname = combinedMap.get(EntityConstants.CAPS_FIRSTNAME).toString();
		String lastname = combinedMap.get(EntityConstants.CAPS_LASTNAME).toString();
		String password = combinedMap.get(EntityConstants.PASSWORD).toString();
		String confirmpassword = combinedMap.get(EntityConstants.CAPS_CONFIRMPASSWORD).toString();
		Map<String, Object> obj = UtilMisc.toMap(EntityConstants.USER_LOGIN_ID, username, EntityConstants.FIRST_NAME,
				firstname, EntityConstants.LAST_NAME, lastname, EntityConstants.CURRENT_PASSWORD, password,
				EntityConstants.CURRENT_PASSWORD_VERIFY, confirmpassword);
		System.out.println("UtilMap-" + obj);
		Map<String, Object> obj2 = UtilMisc.toMap(EntityConstants.USER_LOGIN_ID, username, EntityConstants.FIRST_NAME,
				firstname, EntityConstants.LAST_NAME, lastname, EntityConstants.PASSWORD, password,
				EntityConstants.CURRENT_PASSWORD_VERIFY, confirmpassword);
		try {
			HibernateValidation Hibernet = HibernateHelper.populateBeanFromMap(obj2, HibernateValidation.class);
			Set<ConstraintViolation<HibernateValidation>> errors = HibernateHelper.checkValidationErrors(Hibernet,
					RegisterFormCheck.class);
			boolean hasFormErrors = HibernateHelper.validateFormSubmission(delegator, errors, request, locale,
					"MandatoryFieldErrMsgLoginForm", resource_error, false);
			if (!hasFormErrors) {
				Map<String, Object> result = dispatcher.runSync("createPersonAndUserLogin", obj);

				if (ServiceUtil.isError(result)) {
					request.setAttribute("RegisterError", "error");
					return "error";
				}

				Map<String, Object> roleresult = dispatcher.runSync("assignPartyRole",
						UtilMisc.toMap("partyId", result.get("partyId"), "roleTypeId", "user"));
				System.out.println("roleResult : " + roleresult);
				request.setAttribute("roleResult", result);
				request.setAttribute("result", result);
			}
		} catch (Exception e) {
			request.setAttribute("RegisterError", e);
			return "error";
		}
		return "success";
	}

}
