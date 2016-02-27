<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<!-- New Project Form -->
<h1><span class="glyphicon glyphicon-folder-open" aria-hidden="true"></span> &nbsp; Add Project</h1>
<div>
<form:form id="formId" commandName="project" method="post">
	<div class="form-group">
		<form:label path="name" class="control-label">Name</form:label>
		<form:input path="name" class="form-control" />
		<form:errors path="name" class="alert alert-error"/>
	</div>
	<div class="form-group">
		<form:label path="description" class="control-label">Description</form:label>
		<form:input path="description" class="form-control" />
		<form:errors path="description" class="alert alert-error"/>
	</div>
	<button id="createNewProject" type="submit" class="btn btn-default"><span class="glyphicon glyphicon-floppy-save" aria-hidden="true"></span> Add Project</button>
</form:form>
</div>
<!-- Document Window End -->