<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<div>
    <form:form id="formId" commandName="userAccountFormBean" method="post">
        <div class="form-group">
            <form:label path="userPassword">Password</form:label>
            <form:password path="userPassword" class="form-control"/><br/>
            <form:errors path="userPassword" class="alert alert-error"/>
        </div>
        <div class="form-group">
            <form:label path="userPasswordConfirmation">Password again</form:label>
            <form:password path="userPasswordConfirmation" class="form-control"/><br/>
            <form:errors path="userPasswordConfirmation" class="alert alert-error"/>
        </div>
        <form:hidden path="userFullname"/>
        <form:hidden path="userEmail"/>
        <button id="saveNewPassword" type="submit" class="btn btn-default">Save New Password</button>
    </form:form>
</div>