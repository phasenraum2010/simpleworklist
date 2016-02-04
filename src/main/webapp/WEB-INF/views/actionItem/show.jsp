<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<%@ page import="org.woehlke.simpleworklist.entities.ActionState" %>
<!-- Document Window -->
<form:form id="formId" commandName="actionItem" method="post">
 	<div class="form-group">
    	<form:label path="title">Titel</form:label>
		<form:input path="title" class="form-control"/>
		<form:errors path="title" class="alert alert-error"/>
	</div>
	<div class="form-group">
		<form:label path="status">Status</form:label>
		<c:forEach var="state" items="${stateValues}">
			<form:radiobutton path="status" value="${state}"/>
				<c:choose>
				<c:when test="${state.name() == 'NEW'}">
					<button class="btn btn-small btn-small btn-danger" type="button">&nbsp;</button>
				</c:when>
				<c:when test="${state.name() == 'WORK'}">
					<button class="btn btn-small btn-warning" type="button">&nbsp;</button>
				</c:when>
				<c:when test="${state.name() == 'DONE'}">
					<button class="btn btn-small btn-success" type="button">&nbsp;</button>
				</c:when>
			</c:choose>
		</c:forEach>
	</div>
     <div class="form-group">
		 <form:label path="text">Text</form:label>
		 <form:textarea path="text" rows="10" cols="50" class="form-control"/>
		 <form:errors path="text" class="alert alert-error"/>
    </div>
	<button id="editDataLeaf" type="submit" class="btn btn-default"><span class="glyphicon glyphicon-floppy-save" aria-hidden="true"></span> Save Action Item</button>
	&nbsp;&nbsp;&nbsp;<a href="<c:url value="/actionItem/transform/${actionItem.id}"/>"><span class="glyphicon glyphicon-refresh" aria-hidden="true"></span> Transform to Category</a>
</form:form>
