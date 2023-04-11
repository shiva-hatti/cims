package com.iris.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.iris.dateutility.util.DateManip;
import com.iris.dto.AttachmentInfoBean;
import com.iris.dto.DynamicContent;
import com.iris.dto.EmailHistoryBean;
import com.iris.dto.MailContentBean;
import com.iris.dto.MailInfoBean;
import com.iris.dto.MailServiceBean;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.dto.UserRoleEmailInfoBean;
import com.iris.model.EmailAlert;
import com.iris.model.EmailBodyBean;
import com.iris.model.EmailFormatter;
import com.iris.model.EmailGroupConfigure;
import com.iris.model.EmailGroupMapping;
import com.iris.model.EmailRoleMapping;
import com.iris.model.EmailSentHistory;
import com.iris.model.EmailUnsubscribe;
import com.iris.model.Menu;
import com.iris.model.Return;
import com.iris.model.ReturnEntityMappingNew;
import com.iris.model.ReturnRegulatorMapping;
import com.iris.model.UserEntityRole;
import com.iris.model.UserMaster;
import com.iris.model.UserRole;
import com.iris.model.UserRoleMaster;
import com.iris.model.WebServiceComponentUrl;
import com.iris.nbfc.model.NBFCEmailSentHistory;
import com.iris.nbfc.model.NbfcCorRegistrationBean;
import com.iris.service.GenericService;
import com.iris.service.MailService;
import com.iris.service.impl.ReturnEntityMapServiceNew;
import com.iris.service.impl.ReturnRegulatorMappingService;
import com.iris.service.impl.UserAuthorizationService;
import com.iris.service.impl.UserEntityRoleService;
import com.iris.service.impl.UserRoleMasterService;
import com.iris.service.impl.UserRoleReturnMappingService;
import com.iris.service.impl.WebServiceComponentService;
import com.iris.util.Validations;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.GeneralConstants;
import com.iris.util.constant.MailConstant;
import com.iris.util.constant.MethodConstants;
import com.iris.webservices.client.CIMSRestWebserviceClient;

/**
 * @author Siddique
 */
@RestController
@RequestMapping("/service/prepareSendMailController")
public class PrepareSendMailController {

	static final Logger logger = LogManager.getLogger(PrepareSendMailController.class);

	@Autowired
	private GenericService<EmailAlert, Long> prepareSendMailService;

	@Autowired
	private UserAuthorizationService userAuthorizationService;

	@Autowired
	private GenericService<EmailFormatter, Long> emailFormatterService;

	@Autowired
	private GenericService<EmailSentHistory, Long> emailSentHistoryService;

	@Autowired
	private GenericService<NBFCEmailSentHistory, Long> nbfcEmailSentHistoryService;

	@Autowired
	private WebServiceComponentService webServiceComponentService;

	@Autowired
	private UserRoleMasterService userRoleMasterService;

	@Autowired
	private UserEntityRoleService userEntityRoleService;

	@Autowired
	private ReturnRegulatorMappingService returnRegulatorMappingService;

	@Autowired
	private ReturnEntityMapServiceNew returnEntityMapServiceNew;

	@Autowired
	private UserRoleReturnMappingService userRoleReturnMappingService;

	//	private List<Long> usrRoleMastPkList = new ArrayList<>();

	private Long eAlertId;

	/*
	 * The below mathod working as follows
	 * 1. Accept list of beans that contain 1 or more than 1 bean
	 * 2. iterating each bean contain in the list
	 * 3. get the mail id's of each bean using getMailIdsData() and prepare the mail content using prepareMailContent().
	 * 4. attched the doc if any in and prepare the list  mailInfoBeanList.
	 * 5. paralelly prepare the history bean emailHistoryBean for each bean.
	 * 6. call the sendMail() with mailInfoBeanList/
	 * 7. call the saveMailHistory() with emailHistoryBean and save the history which returns saveEmailSentHistoryList.
	 * 8. Create the response and add saveEmailSentHistoryList in the response and send response to user.
	 */

	@PostMapping(value = "/prepareSendEmail")
	public ServiceResponse prepareSendEmail(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody List<MailServiceBean> mailBeanList) {
		ServiceResponse serviceResponse = null;
		EmailHistoryBean emailHistoryBean = null;
		List<EmailSentHistory> saveEmailSentHistoryList = null;
		try {
			logger.info("prepare Send mail started " + mailBeanList.size() + "JobProcessingId " + jobProcessId);
			if (CollectionUtils.isEmpty(mailBeanList)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusMessage(GeneralConstants.EMPTY_BEAN_RECEIVED.getConstantVal()).build();
			}
			List<MailInfoBean> mailInfoBeanList = new ArrayList<>();
			MailInfoBean mailInfoBean = new MailInfoBean();
			List<EmailHistoryBean> emailHistoryBeanList = new ArrayList<>();

			logger.info("Step1: iterating the mail bean" + mailBeanList.size() + "JobProcessingId " + jobProcessId);
			for (MailServiceBean mailServiceBean : mailBeanList) {
				try {
					logger.info("reading and preparing mail content for alert id" + mailServiceBean.getAlertId() + "and userId" + mailServiceBean.getUserId() + "JobProcessingId" + jobProcessId);
					eAlertId = mailServiceBean.getAlertId();
					Map<Integer, List<String>> emailMap = null;

					//condition added by siddique on 13-04-2020
					if (mailServiceBean.getAlertId() == 43l || mailServiceBean.getAlertId() == 40l || mailServiceBean.getAlertId() == 42l || mailServiceBean.getAlertId() == 39l || mailServiceBean.getAlertId() == 88l || mailServiceBean.getAlertId() == 89l || mailServiceBean.getAlertId() == 90l || mailServiceBean.getAlertId() == 91l) {
						emailMap = getEmailIdsForApproveRejectUnlockRequestUsers(mailServiceBean);
					} else if (!CollectionUtils.isEmpty(mailServiceBean.getEmailMap())) {
						emailMap = mailServiceBean.getEmailMap();
					} else {
						emailMap = getMailIdsData(mailServiceBean);
					}
					logger.info("Step2: get the mail ids for To, CC, BCC JobProcessingId " + jobProcessId);

					if (!CollectionUtils.isEmpty(emailMap)) {

						mailInfoBean = prepareMailContent(mailServiceBean.getDynamicContentsList(), emailMap);
						logger.info("Step3: prepare the mail content with mail id getted from step 2 JobProcessingId " + jobProcessId);

						logger.info("if attachement found in the bean then add attachement in the bean else skip the below code For JobProcessingId " + jobProcessId);
						if (!CollectionUtils.isEmpty(mailServiceBean.getAttachmentInfoBean())) {

							AttachmentInfoBean[] attachments = new AttachmentInfoBean[mailServiceBean.getAttachmentInfoBean().size()];
							int count = 0;
							for (AttachmentInfoBean aB : mailServiceBean.getAttachmentInfoBean()) {
								attachments[count] = aB;
								count++;
								mailInfoBean.setAttachments(attachments);
							}
						}

						logger.info("Step4: add each bean in the mailInfoBeanList and prepare the history bean for each mail bean For JobProcessingId " + jobProcessId);
						mailInfoBeanList.add(mailInfoBean);
					} else {
						logger.error("No Mail mapped for this record", mailServiceBean.getUniqueId());
					}
					emailHistoryBean = new EmailHistoryBean();
					if (!CollectionUtils.isEmpty(emailMap)) {
						emailHistoryBean.setMailSubject(mailInfoBean.getSubject());
						emailHistoryBean.setMailBody(mailInfoBean.getBody());
						//						emailHistoryBean.setUserRoleMstrList(usrRoleMastPkList);
						emailHistoryBean.setUserRoleId((mailServiceBean.getRoleId()));
						emailHistoryBean.setMenuId(mailServiceBean.getMenuId());
						emailHistoryBean.setUserId(mailServiceBean.getUserId());
						emailHistoryBean.setUniqueId(mailServiceBean.getUniqueId());
						emailHistoryBean.setNbfcEntityBeanFk(mailServiceBean.getNbfcEntityBeanFk());
						emailHistoryBean.setMailRecipients(new Gson().toJson(emailMap));

					} else {
						emailHistoryBean.setStatus(false);
						emailHistoryBean.setReason("Email List is blank");
					}
					emailHistoryBeanList.add(emailHistoryBean);

				} catch (Exception e) {
					logger.error("Error while creating bean For JobProcessingId " + jobProcessId + "Exception is" + e);

				}

			}
			logger.info("Step5: Pass the mail mailinfoBeanList to the sendMail method For JobProcessingId : %s", jobProcessId);
			sendMail(mailInfoBeanList);
			if (!CollectionUtils.isEmpty(emailHistoryBeanList)) {
				logger.info("Step6: save the mail history and completed the process of processId : %s", jobProcessId);
				if (eAlertId.equals(MailConstant.NBFC_VERIFICATION_EMAIL_ALERT_ID.getConstantLongVal()) || eAlertId.equals(MailConstant.NBFC_USER_CREATED_EMAIL_ALERT_ID.getConstantLongVal())) {
					saveEmailSentHistoryList = saveNbfcMailHistory(emailHistoryBeanList.get(0));
					return new ServiceResponseBuilder().setStatus(true).setResponse(saveEmailSentHistoryList).build();
				} else {
					saveEmailSentHistoryList = saveMailHistory(emailHistoryBeanList);
					return new ServiceResponseBuilder().setStatus(true).setResponse(saveEmailSentHistoryList).build();
				}
			}
		} catch (Exception e) {
			logger.error("Exception in prepare send mail JobProcessingId " + jobProcessId + "Exception is" + e);
			return new ServiceResponseBuilder().setStatus(false).setResponse(saveEmailSentHistoryList).build();

		}
		return serviceResponse;
	}

	private Map<Integer, List<String>> getEmailIdsForApproveRejectUnlockRequestUsers(MailServiceBean mailServiceBean) {
		logger.info("getEmailIdsForApproveRejectUnlockRequestUsers method called");
		Map<Integer, List<String>> emailMap = new HashMap<>();

		if (StringUtils.isEmpty(mailServiceBean.getReturnCode()) || StringUtils.isEmpty(mailServiceBean.getEntityCode()) || StringUtils.isEmpty(mailServiceBean.getRoleId())) {
			return emailMap;
		}

		try {
			Map<String, Object> columnValueMap = new HashMap<>();

			columnValueMap.put(ColumnConstants.ENT_CODE.getConstantVal(), mailServiceBean.getEntityCode());
			columnValueMap.put(ColumnConstants.RETURN_CODE.getConstantVal(), mailServiceBean.getReturnCode());
			columnValueMap.put(ColumnConstants.ROLEID.getConstantVal(), mailServiceBean.getRoleId());

			List<ReturnEntityMappingNew> returnEntityMappingNewList = returnEntityMapServiceNew.getDataByObject(columnValueMap, MethodConstants.GET_DATA_BY_ENTITY_CODE_AND_RETURN_CODE.getConstantVal());
			if (CollectionUtils.isEmpty(returnEntityMappingNewList)) {
				logger.info("Mapping not found for this entity and return in ReturnEntityMappingNew table For Entity code" + mailServiceBean.getEntityCode() + " For Return Code" + mailServiceBean.getReturnCode());
				return emailMap;
			}

			List<UserEntityRole> userEntityRoleList = userEntityRoleService.getDataByObject(columnValueMap, MethodConstants.GET_ENTITY_ROLE_DATA.getConstantVal());

			if (CollectionUtils.isEmpty(userEntityRoleList)) {
				logger.info("Mapping not found for this entity and return in ReturnEntityMappingNew table For Entity code" + mailServiceBean.getEntityCode() + " and For Return Code" + mailServiceBean.getReturnCode());
				return emailMap;
			}

			List<Long> userRoleId = userEntityRoleList.stream().map(f -> f.getUserRoleId()).distinct().collect(Collectors.toList());

			if (CollectionUtils.isEmpty(userRoleId)) {
				logger.info("No mapping found for this role id" + mailServiceBean.getEntityCode() + " and For Return Code" + mailServiceBean.getReturnCode());
				return emailMap;
			}

			Map<Long, Map<Return, Boolean>> roleReturnMap = userRoleReturnMappingService.getRoleRetunMap(userRoleId.toArray(new Long[userRoleId.size()]));

			List<String> finalEntityUsersEmailList = new ArrayList<>();

			if (CollectionUtils.isEmpty(roleReturnMap)) {
				finalEntityUsersEmailList.addAll(userEntityRoleList.stream().map(m -> m.getCompanyEmail().trim()).distinct().collect(Collectors.toList()));
			} else {
				for (UserEntityRole userEntityRole : userEntityRoleList) {
					if (roleReturnMap.get(userEntityRole.getUserRoleId()) != null) {
						Map<Return, Boolean> map = roleReturnMap.get(userEntityRole.getUserRoleId());
						map.forEach((k, v) -> {
							if (k.getReturnCode().equalsIgnoreCase(mailServiceBean.getReturnCode()) && v.equals(Boolean.TRUE) && !finalEntityUsersEmailList.contains(userEntityRole.getCompanyEmail())) {
								finalEntityUsersEmailList.add(userEntityRole.getCompanyEmail().trim());
							}
						});
					} else {
						if (!finalEntityUsersEmailList.contains(userEntityRole.getCompanyEmail())) {
							finalEntityUsersEmailList.add(userEntityRole.getCompanyEmail().trim());
						}
					}
				}
			}

			if (CollectionUtils.isEmpty(finalEntityUsersEmailList)) {
				logger.info("No mail id found for this role id" + mailServiceBean.getRoleId() + "for Entity" + mailServiceBean.getEntityCode() + " For Return Code" + mailServiceBean.getReturnCode());
				return emailMap;
			}

			List<String> regulatorEmailidList = null;
			if (mailServiceBean.getAlertId() == 40l || mailServiceBean.getAlertId() == 42l || mailServiceBean.getAlertId() == 43l || mailServiceBean.getAlertId() == 39l) {
				List<ReturnRegulatorMapping> returnRegulatorMappingList = returnRegulatorMappingService.getDataByObject(columnValueMap, MethodConstants.GET_DATA_BY_RETURN_CODE.getConstantVal());
				if (CollectionUtils.isEmpty(returnRegulatorMappingList)) {
					logger.info("No regulator id mapped to this return" + mailServiceBean.getReturnCode());
					return emailMap;
				}

				emailMap = new HashMap<>();
				regulatorEmailidList = new ArrayList<>();
				for (ReturnRegulatorMapping regulatorEmailIds : returnRegulatorMappingList) {

					if (!StringUtils.isEmpty(regulatorEmailIds.getEmailIds())) {
						String emailArr[] = regulatorEmailIds.getEmailIds().split(",");

						if (emailArr.length > 0) {
							for (String email : emailArr) {

								if (!regulatorEmailidList.contains(email)) {
									regulatorEmailidList.add(email.trim());
								}
							}
						}
					}
				}
			}
			if (mailServiceBean.getAlertId() == 40l || mailServiceBean.getAlertId() == 42l) {
				emailMap.put(MailConstant.EMAIL_TO.getConstantIntVal(), regulatorEmailidList);
				emailMap.put(MailConstant.EMAIL_CC.getConstantIntVal(), finalEntityUsersEmailList);

			} else if (mailServiceBean.getAlertId() == 43l || mailServiceBean.getAlertId() == 39l) {
				emailMap.put(MailConstant.EMAIL_TO.getConstantIntVal(), finalEntityUsersEmailList);
				emailMap.put(MailConstant.EMAIL_CC.getConstantIntVal(), regulatorEmailidList);

			} else if (mailServiceBean.getAlertId() == 88l || mailServiceBean.getAlertId() == 89l || mailServiceBean.getAlertId() == 90l || mailServiceBean.getAlertId() == 91l) {
				emailMap.put(MailConstant.EMAIL_TO.getConstantIntVal(), finalEntityUsersEmailList);
			}
			logger.info("getEmailIdsForApproveRejectUnlockRequestUsers method completed");
		} catch (Exception e) {
			logger.error("Exception occoured while getting mail id" + e);
		}

		return emailMap;
	}

	private List<EmailSentHistory> saveMailHistory(List<EmailHistoryBean> emailHistoryBeanList) {
		logger.info("save mail history method called");
		List<EmailSentHistory> saveMailHistoryList = null;
		if (!CollectionUtils.isEmpty(emailHistoryBeanList)) {
			saveMailHistoryList = new ArrayList<>();
			EmailSentHistory emailSentHistory = null;
			for (EmailHistoryBean emailHistoryBean : emailHistoryBeanList) {
				try {
					if (emailHistoryBean.getMenuId() != null && !Validations.isEmpty(emailHistoryBean.getMailBody()) && !Validations.isEmpty(emailHistoryBean.getMailRecipients()) && !Validations.isEmpty(emailHistoryBean.getMailSubject())) {
						emailSentHistory = new EmailSentHistory();

						if (!StringUtils.isEmpty(emailHistoryBean.getUserId())) {
							UserMaster user = new UserMaster();
							user.setUserId(emailHistoryBean.getUserId());
							emailSentHistory.setUserMasterFk(user);
						}
						if (!StringUtils.isEmpty(emailHistoryBean.getUserRoleId())) {
							UserRole usrRole = new UserRole();
							usrRole.setUserRoleId(emailHistoryBean.getUserRoleId());
							emailSentHistory.setUserRoleFk(usrRole);
						}
						Menu menu = new Menu();
						menu.setMenuId(emailHistoryBean.getMenuId());
						emailSentHistory.setMenuFk(menu);

						emailSentHistory.setMailBody(emailHistoryBean.getMailBody());
						emailSentHistory.setMailRecipients(emailHistoryBean.getMailRecipients());
						emailSentHistory.setMailSubject(emailHistoryBean.getMailSubject());
						emailSentHistory.setSentDtTime(DateManip.getCurrentDateTime());
						emailSentHistory.setMailRecipients(emailHistoryBean.getMailRecipients());
						emailSentHistoryService.add(emailSentHistory);
						Long emailSentHistId = emailSentHistory.getMailSentHistId();

						//						if (!CollectionUtils.isEmpty(usrRoleMastPkList)) {
						//							boolean flag = userRoleMasterService.updateUserRoleMaster(
						//									emailSentHistory.getMailSentHistId(),
						//									usrRoleMastPkList.toArray(new Long[emailHistoryBean.getUserRoleMstrList().size()]));
						//						}
						emailSentHistory = new EmailSentHistory();
						emailSentHistory.setUniqueId(emailHistoryBean.getUniqueId());
						emailSentHistory.setIsHistoryMaintained(true);
						emailSentHistory.setMailSentHistId(emailSentHistId);
						emailSentHistory.setReason("History maintained successfully");
						saveMailHistoryList.add(emailSentHistory);
					}
				} catch (Exception e) {
					emailSentHistory = new EmailSentHistory();
					emailSentHistory.setUniqueId(emailHistoryBean.getUniqueId());
					emailSentHistory.setIsHistoryMaintained(false);
					emailSentHistory.setReason(e.getMessage());
					saveMailHistoryList.add(emailSentHistory);
					logger.error("Exception while saving mail history", e);
				}
			}
		}
		return saveMailHistoryList;
	}

	private void sendMail(List<MailInfoBean> mailInfoBeanList) {
		logger.info("inside send mail method : PrepareSendMailcontroller.java" + mailInfoBeanList.size());
		try {
			Map<String, List<String>> valueMap = new HashMap<>();

			List<String> valueList = new ArrayList<>();
			valueList.add(GeneralConstants.MAIL_NEW.getConstantVal());
			valueMap.put(ColumnConstants.COMPONENTTYPE.getConstantVal(), valueList);

			valueList = new ArrayList<>();
			valueList.add(CIMSRestWebserviceClient.HTTP_METHOD_TYPE_POST);
			valueMap.put(ColumnConstants.METHODTYPE.getConstantVal(), valueList);

			WebServiceComponentUrl componentUrl = webServiceComponentService.getDataByColumnValue(valueMap, MethodConstants.GET_ACTIVE_DATA_BY_COMPONENTTYPE_METHODTYPE.getConstantVal()).get(0);
			MailService.sendMail(componentUrl, mailInfoBeanList);
		} catch (Exception e) {
			logger.error("Exception in sendMail method PrepareSendMailcontroller.java", e);
		}

	}

	private MailInfoBean prepareMailContent(List<DynamicContent> dynamicContentsList, Map<Integer, List<String>> emailMap) {

		MailInfoBean mailInfoBean = null;
		try {
			logger.info("prepare mail content method started");

			MailContentBean mailContentBean = new MailContentBean();

			for (Map.Entry<Integer, List<String>> map : emailMap.entrySet()) {
				if (CollectionUtils.isEmpty(map.getValue())) {
					continue;
				}

				if (map.getKey() != null && map.getKey().equals(GeneralConstants.EMAIL_TO.getConstantIntVal())) {
					mailContentBean.setEmailTOList(map.getValue());
				}
				if (map.getKey() != null && map.getKey().equals(GeneralConstants.EMAIL_CC.getConstantIntVal())) {
					mailContentBean.setEmailCCList(map.getValue());
				}
				if (map.getKey() != null && map.getKey().equals(GeneralConstants.EMAIL_BCC.getConstantIntVal())) {
					mailContentBean.setEmailBCCList(map.getValue());
				}
			}
			mailContentBean.setEmailAlertId(eAlertId);
			mailContentBean.setDynamicContents(dynamicContentsList);

			mailInfoBean = mailContent(mailContentBean);

		} catch (Exception e) {
			logger.error("Exception in prepare mail content method", e);
		}
		return mailInfoBean;
	}

	private MailInfoBean mailContent(MailContentBean mailContentBean) {
		MailInfoBean mailInfoBean = null;

		try {
			if (mailContentBean.getEmailAlertId() == null) {
				return mailInfoBean;
			}

			EmailAlert eAlert = prepareSendMailService.getDataById(mailContentBean.getEmailAlertId());
			if (eAlert == null) {
				return mailInfoBean;
			}

			EmailBodyBean emailBodyBean = eAlert.getEmailBody();
			if (emailBodyBean == null) {
				return mailInfoBean;
			}

			Map<String, String> emailKeyValMap = new HashMap<>();
			for (DynamicContent obj : mailContentBean.getDynamicContents()) {
				if (!Validations.isEmpty(obj.getKey())) {
					emailKeyValMap.put(obj.getKey(), obj.getValue());
				}
			}

			mailInfoBean = new MailInfoBean();
			String subject = null;
			String primaryMailSubject = null;
			String secondaryMailSubject = null;

			if (!Validations.isEmpty(emailBodyBean.getEmailSubject1())) {
				if (!CollectionUtils.isEmpty(emailKeyValMap) && emailBodyBean.getEmailSubject1().contains("#")) {
					primaryMailSubject = getDynamicSubjectBody(emailBodyBean.getEmailSubject1(), emailKeyValMap);
				} else {
					primaryMailSubject = emailBodyBean.getEmailSubject1();
				}
			}

			if (!Validations.isEmpty(primaryMailSubject)) {
				subject = primaryMailSubject;
			}

			if (!Validations.isEmpty(emailBodyBean.getEmailSubject2())) {
				if (!CollectionUtils.isEmpty(emailKeyValMap) && emailBodyBean.getEmailSubject2().contains("#")) {
					secondaryMailSubject = getDynamicSubjectBody(emailBodyBean.getEmailSubject2(), emailKeyValMap);
				} else {
					secondaryMailSubject = emailBodyBean.getEmailSubject2();
				}
			}

			if (!Validations.isEmpty(secondaryMailSubject)) {
				subject += " | " + secondaryMailSubject;
			}

			String mailBody = null;
			try {
				mailBody = buildMailBody(emailBodyBean, mailContentBean.getDynamicContents(), emailKeyValMap);
			} catch (Exception e) {
				logger.error("Error while creating mail body", e);
			}

			mailInfoBean.setSubject(subject);
			if (!Validations.isEmpty(mailBody)) {
				mailInfoBean.setBody(mailBody);
			}

			if (!CollectionUtils.isEmpty(mailContentBean.getEmailTOList())) {
				mailInfoBean.setRecipientIds(getEmailArray(mailContentBean.getEmailTOList()));
			}

			if (!CollectionUtils.isEmpty(mailContentBean.getEmailCCList())) {
				mailInfoBean.setCcMailIds(getEmailArray(mailContentBean.getEmailCCList()));
			}

			if (!CollectionUtils.isEmpty(mailContentBean.getEmailBCCList())) {
				mailInfoBean.setBccMailIds(getEmailArray(mailContentBean.getEmailBCCList()));
			}
		} catch (Exception e) {
			logger.error("Error while creating mail content bean", e);
		}
		return mailInfoBean;
	}

	private String[] getEmailArray(List<String> emailTOList) {
		return emailTOList.toArray(new String[emailTOList.size()]);

	}

	private String buildMailBody(EmailBodyBean emailBodyBean, List<DynamicContent> dynamicContents, Map<String, String> emailKeyValMap) {

		StringBuilder bodyB = new StringBuilder("");

		List<EmailFormatter> emailFormtrList = emailFormatterService.getAllDataFor(null, null);
		if (CollectionUtils.isEmpty(emailFormtrList)) {
			return bodyB.toString();
		}
		EmailFormatter emailFormatter = emailFormtrList.get(0);

		String primaryMailBody = null;
		String secondaryMailbody = null;
		StringBuilder bodyContent = new StringBuilder("");
		StringBuilder bodyContentRtl = new StringBuilder("");

		try {
			if (emailBodyBean != null) {
				if (!Validations.isEmpty(emailBodyBean.getEmailBody1())) {
					if (!CollectionUtils.isEmpty(emailKeyValMap) && emailBodyBean.getEmailBody1().contains("#")) {
						primaryMailBody = getDynamicSubjectBody(emailBodyBean.getEmailBody1(), emailKeyValMap);
					} else {
						primaryMailBody = emailBodyBean.getEmailBody1();
					}
				}

				if (!Validations.isEmpty(primaryMailBody)) {
					if (emailBodyBean.getIsRtl1().equals(Boolean.TRUE)) {
						bodyB.append("<div dir='rtl'>" + primaryMailBody + "</div>");
					} else {
						bodyB.append(primaryMailBody);
					}
				}

				if (emailBodyBean.getIsRtl1().equals(Boolean.TRUE)) {
					if (!CollectionUtils.isEmpty(dynamicContents)) {
						bodyContentRtl.append("<br/><br/><div dir='rtl'><table font-family:" + emailFormatter.getFontFamily() + ";'>");
						for (DynamicContent dynamicContent : dynamicContents) {
							bodyContentRtl.append("<tr><td style='border:" + emailFormatter.getTableBorder() + "px solid #999999; font-size:" + emailFormatter.getThFontSize() + "px; color:" + emailFormatter.getThFontColor() + "; background-color:" + emailFormatter.getThBackgroundColor() + ";'>" + dynamicContent.getLabel() + "</td><td style='border:" + emailFormatter.getTableBorder() + "px solid #999999; font-size:" + emailFormatter.getTdFontSize() + "px; color:" + emailFormatter.getTdFontColor() + "; background-color:" + emailFormatter.getTdBackgroundColor() + ";'>" + dynamicContent.getValue() + "</td></tr>");
						}
						bodyContentRtl.append("</table></div>");
						bodyB.append(bodyContentRtl.toString());
					}
				} else {
					if (!CollectionUtils.isEmpty(dynamicContents)) {
						bodyContent.append("<br/><br/><table font-family:" + emailFormatter.getFontFamily() + ";'>");
						for (DynamicContent dynamicContent : dynamicContents) {
							bodyContent.append("<tr><td style='border:" + emailFormatter.getTableBorder() + "px solid #999999; font-size:" + emailFormatter.getThFontSize() + "px; color:" + emailFormatter.getThFontColor() + "; background-color:" + emailFormatter.getThBackgroundColor() + ";'>" + dynamicContent.getLabel() + "</td><td style='border:" + emailFormatter.getTableBorder() + "px solid #999999; font-size:" + emailFormatter.getTdFontSize() + "px; color:" + emailFormatter.getTdFontColor() + "; background-color:" + emailFormatter.getTdBackgroundColor() + ";'>" + dynamicContent.getValue() + "</td></tr>");
						}
						bodyContent.append("</table>");
						bodyB.append(bodyContent.toString());
					}
				}

				String signature = "";
				if (!Validations.isEmpty(emailFormatter.getSignature())) {
					signature = emailFormatter.getSignature();
				}
				if (emailBodyBean.getIsRtl1().equals(Boolean.TRUE)) {
					bodyB.append("<br/><div dir='rtl'>" + signature + "</div>");
				} else {
					bodyB.append("<br/>" + signature);
				}

				bodyB.append("<br/>");

				if (!Validations.isEmpty(emailBodyBean.getEmailBody2())) {
					if (!CollectionUtils.isEmpty(emailKeyValMap) && emailBodyBean.getEmailBody2().contains("#")) {
						secondaryMailbody = getDynamicSubjectBody(emailBodyBean.getEmailBody2(), emailKeyValMap);
					} else {
						secondaryMailbody = emailBodyBean.getEmailBody2();
					}
				}

				if (!Validations.isEmpty(secondaryMailbody)) {
					bodyB.append("<br/><hr>");

					if (emailBodyBean.getIsRtl2().equals(Boolean.TRUE)) {
						bodyContentRtl = new StringBuilder("");
						bodyB.append("<div dir='rtl'>" + secondaryMailbody + "</div>");
						if (!CollectionUtils.isEmpty(dynamicContents)) {
							bodyContentRtl.append("<br/><br/><div dir='rtl'><table style='font-family:" + emailFormatter.getFontFamily() + ";'>");
							for (DynamicContent dynamicContent : dynamicContents) {
								bodyContentRtl.append("<tr><td style='border:" + emailFormatter.getTableBorder() + "px solid #999999; font-size:" + emailFormatter.getThFontSize() + "px; color:" + emailFormatter.getThFontColor() + "; background-color:" + emailFormatter.getThBackgroundColor() + ";'>" + dynamicContent.getLabel() + "</td><td style='border:" + emailFormatter.getTableBorder() + "px solid #999999; font-size:" + emailFormatter.getTdFontSize() + "px; color:" + emailFormatter.getTdFontColor() + "; background-color:" + emailFormatter.getTdBackgroundColor() + ";'>" + dynamicContent.getValue() + "</td></tr>");
							}
							bodyContentRtl.append("</table></div>");
							bodyB.append(bodyContentRtl.toString());
							bodyB.append("<br/><div dir='rtl'>" + signature + "</div>");
						}
					} else {
						bodyB.append(secondaryMailbody);
						bodyContent = new StringBuilder("");
						if (!CollectionUtils.isEmpty(dynamicContents)) {
							bodyContent.append("<br/><br/><table style='font-family:" + emailFormatter.getFontFamily() + ";'>");
							for (DynamicContent dynamicContent : dynamicContents) {
								bodyContent.append("<tr><td style='border:" + emailFormatter.getTableBorder() + "px solid #999999; font-size:" + emailFormatter.getThFontSize() + "px; color:" + emailFormatter.getThFontColor() + "; background-color:" + emailFormatter.getThBackgroundColor() + ";'>" + dynamicContent.getLabel() + "</td><td style='border:" + emailFormatter.getTableBorder() + "px solid #999999; font-size:" + emailFormatter.getTdFontSize() + "px; color:" + emailFormatter.getTdFontColor() + "; background-color:" + emailFormatter.getTdBackgroundColor() + ";'>" + dynamicContent.getValue() + "</td></tr>");
							}
							bodyContent.append("</table>");
							bodyB.append(bodyContent.toString());
							bodyB.append("<br/>" + signature);
						}
					}
					bodyB.append("<br/>");
				}
			}
		} catch (Exception e) {
			logger.error("Exception while creating build mail body", e);
		}
		return bodyB.toString();

	}

	private String getDynamicSubjectBody(String content, Map<String, String> emailKeyValMap) {
		try {
			for (Map.Entry<String, String> entry : emailKeyValMap.entrySet()) {
				content = content.replaceAll(entry.getKey(), entry.getValue());
			}
		} catch (Exception e) {
			logger.error("Exception in getDynamicSubjectBody method", e);
		}
		return content;
	}

	private Map<Integer, List<String>> getMailIdsData(MailServiceBean mailServiceBean) {
		Map<Integer, List<String>> emailMap = new HashMap<>();
		UserRoleEmailInfoBean userRoleEmailInfoBean = null;
		try {
			logger.info("inside get mailIds Data method for alert id :" + mailServiceBean.getAlertId());
			if (mailServiceBean.getAlertId() != null) {
				EmailAlert eAlert = prepareSendMailService.getDataById(mailServiceBean.getAlertId());
				if (eAlert == null) {

					return emailMap;
				}
				userRoleEmailInfoBean = new UserRoleEmailInfoBean();
				List<String> toEmailList = new ArrayList<>();
				List<String> ccEmailList = new ArrayList<>();
				List<String> bccEmailList = new ArrayList<>();
				Set<UserRoleMaster> userRoleMasterSet;
				Set<EmailGroupConfigure> emailGrpConfSet;

				Set<EmailGroupMapping> emailGrpSet = eAlert.getEmailGroupMapSet();
				if (!CollectionUtils.isEmpty(emailGrpSet)) {
					for (EmailGroupMapping emailGrpMap : emailGrpSet) {
						if (emailGrpMap.getEmailTypeId() == null || emailGrpMap.getEmailTypeId() == 0) {
							continue;
						}
						emailGrpConfSet = emailGrpMap.getEmailGroupIdFk().getEmailGroupSet();
						if (CollectionUtils.isEmpty(emailGrpConfSet)) {
							continue;
						}

						for (EmailGroupConfigure emailConfg : emailGrpConfSet) {
							if (!emailConfg.getIsActive()) {
								continue;
							}
							if (Validations.isEmpty(emailConfg.getEmailId())) {
								continue;
							}

							if (emailGrpMap.getEmailTypeId().intValue() == MailConstant.EMAIL_TO.getConstantIntVal()) {
								// condition added by siddique on 23-06-2020 to handle null mail ids
								if (!StringUtils.isEmpty(emailConfg.getEmailId()) && !toEmailList.contains(emailConfg.getEmailId())) {
									toEmailList.add(emailConfg.getEmailId());
								}
							} else if (emailGrpMap.getEmailTypeId().intValue() == MailConstant.EMAIL_CC.getConstantIntVal()) {
								if (!StringUtils.isEmpty(emailConfg.getEmailId()) && !ccEmailList.contains(emailConfg.getEmailId())) {
									ccEmailList.add(emailConfg.getEmailId());
								}
							} else if (!StringUtils.isEmpty(emailConfg.getEmailId()) && emailGrpMap.getEmailTypeId().intValue() == MailConstant.EMAIL_BCC.getConstantIntVal() && !bccEmailList.contains(emailConfg.getEmailId())) {
								bccEmailList.add(emailConfg.getEmailId());
							}
						}
					}
				}

				Set<EmailRoleMapping> emailRoleMapSet = eAlert.getEmailRoleMapSet();

				if (!CollectionUtils.isEmpty(emailRoleMapSet)) {
					UserRole userRole;
					Set<EmailUnsubscribe> emailUnsubscribeSet;
					boolean isEmailUnscribe = false;
					Set<UserEntityRole> userEntityRoleSet;
					List<UserMaster> userList = null;
					//					List<Long> usrRoleMastPkList = new ArrayList<>();

					for (EmailRoleMapping emailRoleMap : emailRoleMapSet) {
						isEmailUnscribe = false;
						if (emailRoleMap.getEmailTypeId() == null || emailRoleMap.getEmailTypeId() == 0) {
							continue;
						}
						userRole = emailRoleMap.getUserRoleIdFk();
						userRoleMasterSet = userRole.getUsrRoleMstrSet();
						if (CollectionUtils.isEmpty(userRoleMasterSet)) {
							continue;
						}
						for (UserRoleMaster uRoleMstr : userRoleMasterSet) {
							isEmailUnscribe = false;
							if (!uRoleMstr.getIsActive() || !uRoleMstr.getUserMaster().getIsActive()) {
								continue;
							}
							emailUnsubscribeSet = uRoleMstr.getEmailUnsubscribeSet();

							if (!CollectionUtils.isEmpty(emailUnsubscribeSet)) {
								for (EmailUnsubscribe emailUnsbscribe : emailUnsubscribeSet) {
									if (emailUnsbscribe.getEmailAlertIdFk().getEmailAlertId() == eAlert.getEmailAlertId() && emailUnsbscribe.getIsEmailUnsubscribe()) {
										isEmailUnscribe = true;
										break;
									}
								}
							}

							if (isEmailUnscribe) {
								continue;
							}

							if (uRoleMstr.getUserRole().getRoleType().getRoleTypeId().equals(MailConstant.COMPANY.getConstantLongVal())) {
								userEntityRoleSet = uRoleMstr.getUserEntityRole();
								if (CollectionUtils.isEmpty(userEntityRoleSet)) {
									continue;
								}

								for (UserEntityRole usrEntityRole : userEntityRoleSet) {
									if (mailServiceBean.getEntityCode() != null && !mailServiceBean.getEntityCode().equals(usrEntityRole.getEntityBean().getEntityCode())) {
										continue;
									}

									if (usrEntityRole.getIsActive() && usrEntityRole.getCompanyEmail() != null) {
										if (emailRoleMap.getEmailTypeId().intValue() == MailConstant.EMAIL_TO.getConstantIntVal()) {
											if (!StringUtils.isEmpty(usrEntityRole.getCompanyEmail()) && !toEmailList.contains(usrEntityRole.getCompanyEmail())) {
												toEmailList.add(usrEntityRole.getCompanyEmail());
											}
										} else if (emailRoleMap.getEmailTypeId().intValue() == MailConstant.EMAIL_CC.getConstantIntVal()) {
											if (!StringUtils.isEmpty(usrEntityRole.getCompanyEmail()) && !ccEmailList.contains(usrEntityRole.getCompanyEmail())) {
												ccEmailList.add(usrEntityRole.getCompanyEmail());
											}
										} else if (!StringUtils.isEmpty(usrEntityRole.getCompanyEmail()) && emailRoleMap.getEmailTypeId().intValue() == MailConstant.EMAIL_BCC.getConstantIntVal() && !bccEmailList.contains(usrEntityRole.getCompanyEmail())) {
											bccEmailList.add(usrEntityRole.getCompanyEmail());
										}

										//										if (!usrRoleMastPkList.contains(uRoleMstr.getUserRoleMasterId())) {
										//											usrRoleMastPkList.add(uRoleMstr.getUserRoleMasterId());
										//										}
									}
								}
							}

							else if (uRoleMstr.getUserRole().getRoleType().getRoleTypeId().equals(MailConstant.REGULATOR.getConstantLongVal())) {
								logger.info("userList before change");
								userList = userAuthorizationService.getDataByRefId(uRoleMstr.getUserMaster().getUserId());
								logger.info("userList after change" + userList.size());
								if (!CollectionUtils.isEmpty(userList)) {
									for (UserMaster usrMstr : userList) {
										if (emailRoleMap.getEmailTypeId().intValue() == MailConstant.EMAIL_TO.getConstantIntVal()) {
											if (!StringUtils.isEmpty(usrMstr.getPrimaryEmail()) && !toEmailList.contains(usrMstr.getPrimaryEmail())) {
												toEmailList.add(usrMstr.getPrimaryEmail());
											}
										} else if (emailRoleMap.getEmailTypeId().intValue() == MailConstant.EMAIL_CC.getConstantIntVal()) {
											if (!StringUtils.isEmpty(usrMstr.getPrimaryEmail()) && !ccEmailList.contains(usrMstr.getPrimaryEmail())) {
												ccEmailList.add(usrMstr.getPrimaryEmail());
											}
										} else if (!StringUtils.isEmpty(usrMstr.getPrimaryEmail()) && emailRoleMap.getEmailTypeId().intValue() == MailConstant.EMAIL_BCC.getConstantIntVal() && !bccEmailList.contains(usrMstr.getPrimaryEmail())) {
											bccEmailList.add(usrMstr.getPrimaryEmail());
										}
									}

									//									if (!usrRoleMastPkList.contains(uRoleMstr.getUserRoleMasterId())) {
									//										usrRoleMastPkList.add(uRoleMstr.getUserRoleMasterId());
									//									}
								}
							}

						}
					}
					//					userRoleEmailInfoBean.setUsrRoleMastPkList(usrRoleMastPkList);
				}
				if (CollectionUtils.isEmpty(toEmailList) && CollectionUtils.isEmpty(ccEmailList) && CollectionUtils.isEmpty(bccEmailList)) {
					return null;
				}

				Map<String, List<String>> emailMap1 = new HashMap<>();
				emailMap1.put(String.valueOf(MailConstant.EMAIL_TO.getConstantIntVal()), toEmailList);
				emailMap1.put(String.valueOf(MailConstant.EMAIL_CC.getConstantIntVal()), ccEmailList);
				emailMap1.put(String.valueOf(MailConstant.EMAIL_BCC.getConstantIntVal()), bccEmailList);

				userRoleEmailInfoBean.setEmailMap(emailMap1);

				//				if (!CollectionUtils.isEmpty(userRoleEmailInfoBean.getUsrRoleMastPkList())) {
				//					usrRoleMastPkList.addAll(userRoleEmailInfoBean.getUsrRoleMastPkList());
				//				}

				Map<String, List<String>> emailAlertMap = userRoleEmailInfoBean.getEmailMap();

				for (Map.Entry<String, List<String>> map : emailAlertMap.entrySet()) {
					if (CollectionUtils.isEmpty(map.getValue())) {
						continue;
					}
					emailMap.put(Integer.parseInt(map.getKey()), map.getValue());
				}

				return emailMap;
			}
		} catch (Exception e) {
			logger.error("Exception in get get mail ids data method: ", e);
		}

		return emailMap;
	}

	private List<EmailSentHistory> saveNbfcMailHistory(EmailHistoryBean emailHistoryBean) {
		logger.info("save mail history method called");
		NBFCEmailSentHistory nbfcEmailSentHistory = null;
		EmailSentHistory emailSentHistory = null;
		List<EmailSentHistory> saveMailHistoryList = new ArrayList<EmailSentHistory>();
		if (emailHistoryBean != null) {
			try {
				if (!StringUtils.isEmpty(emailHistoryBean.getNbfcEntityBeanFk()) && !Validations.isEmpty(emailHistoryBean.getMailBody()) && !Validations.isEmpty(emailHistoryBean.getMailRecipients()) && !Validations.isEmpty(emailHistoryBean.getMailSubject())) {
					nbfcEmailSentHistory = new NBFCEmailSentHistory();
					NbfcCorRegistrationBean nbfcCorRegistrationBean = new NbfcCorRegistrationBean();
					nbfcCorRegistrationBean.setCorRegistrationId(emailHistoryBean.getNbfcEntityBeanFk());
					nbfcEmailSentHistory.setCorRegIdFK(nbfcCorRegistrationBean);
					nbfcEmailSentHistory.setMailBody(emailHistoryBean.getMailBody());
					nbfcEmailSentHistory.setMailRecipients(emailHistoryBean.getMailRecipients());
					nbfcEmailSentHistory.setMailSubject(emailHistoryBean.getMailSubject());
					nbfcEmailSentHistory.setSentDtTime(DateManip.getCurrentDateTime());
					nbfcEmailSentHistory.setMailRecipients(emailHistoryBean.getMailRecipients());
					nbfcEmailSentHistoryService.add(nbfcEmailSentHistory);

					emailSentHistory = new EmailSentHistory();
					emailSentHistory.setUniqueId(emailHistoryBean.getUniqueId());
					emailSentHistory.setIsHistoryMaintained(true);
					emailSentHistory.setReason("History maintained successfully");
					saveMailHistoryList.add(emailSentHistory);
				}
			} catch (Exception e) {
				emailSentHistory = new EmailSentHistory();
				emailSentHistory.setUniqueId(emailHistoryBean.getUniqueId());
				emailSentHistory.setIsHistoryMaintained(false);
				emailSentHistory.setReason(e.getMessage());
				saveMailHistoryList.add(emailSentHistory);
				logger.error("Exception while saving mail history", e);
			}
		}
		return saveMailHistoryList;
	}

}
