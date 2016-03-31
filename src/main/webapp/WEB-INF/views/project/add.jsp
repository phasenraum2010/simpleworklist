<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<!-- New Project Form -->
<h1><span class="glyphicon glyphicon-folder-open" aria-hidden="true"></span> &nbsp; <spring:message code="project.add.h1" text="Add Project" /></h1>
<div>
<form:form id="formId" commandName="project" method="post">
	<div class="form-group">
		<form:label path="name" class="control-label">Name</form:label>
		<form:input path="name" class="form-control" />
		<form:errors path="name" delimiter=", " element="div" class="alert alert-danger"/>
	</div>
	<div class="form-group">
		<form:label path="description" class="control-label"><spring:message code="project.add.description" text="Description" /></form:label>
		<form:input path="description" class="form-control" />
		<form:errors path="description"  delimiter=", " element="div" class="alert alert-danger"/>
	</div>
	<c:if test="${mustChooseArea}">
		<div class="form-group">
			<form:label path="context.id"><spring:message code="project.edit.context" text="Area" /></form:label>
			<form:select  path="context.id">
				<c:forEach items="${contexts}" var="areaOption">
					<c:choose>
						<c:when test="${locale == 'de'}">
							<c:set var="label" value="${areaOption.nameDe}"/>
						</c:when>
						<c:otherwise>
							<c:set var="label" value="${areaOption.nameEn}"/>
						</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${areaOption.id == project.context.id}">
							<option value="${areaOption.id}" selected><c:out value="${label}"/></option>
						</c:when>
						<c:otherwise>
							<option value="${areaOption.id}"><c:out value="${label}"/></option>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</form:select>
			<form:errors path="context.id" delimiter=", " element="div" class="alert alert-danger"/>
		</div>
	</c:if>
	<c:if test="${! mustChooseArea}">
		<form:hidden path="context.id"/>
	</c:if>
	<button id="createNewProject" type="submit" class="btn btn-default"><span class="glyphicon glyphicon-floppy-save" aria-hidden="true"></span> <spring:message code="project.add.button" text="Add Project" /></button>
</form:form>
</div>
<!-- Document Window End -->