<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<h1><span class="glyphicon glyphicon-edit" aria-hidden="true"></span> <spring:message code="user.selfservice.profile.changeLanguage" text="Set Default Language" /></h1>
<h2>for User: <strong><sec:authentication property="principal.username" /></strong></h2>
<br/>
<a href="<c:url value="/user/selfservice"/>"><spring:message code="button.back" text="back" /></a>