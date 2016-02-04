<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<form:form id="formId" commandName="registerFormBean" method="post">
    <div class="form-group">
        <form:label path="email">Email</form:label>
        <form:input path="email" class="form-control"/>
        <form:errors path="email" class="alert alert-error"/>
    </div>
    <button id="requestPasswordReset" type="submit" class="btn btn-default">Request Password Reset</button>
</form:form>