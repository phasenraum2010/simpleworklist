<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>	
<!-- Document Window -->
<form:form id="formId" commandName="actionItem" method="post" class="form-horizontal">
 	<div class="control-group">
    	<form:label path="title" class="control-label">Titel <sup class="ym-required">*</sup></form:label>
     	<div class="controls">
     		<form:input path="title" class="input-large"/>
     		<form:errors path="title" class="alert alert-error"/>
     	</div>
     </div>
     <div class="control-group">
     	<form:label path="text" class="control-label">Text <sup class="ym-required">*</sup></form:label>
     	<div class="controls">
     		<form:textarea path="text" rows="20" cols="50"  class="input-large"/>
     		<form:errors path="text" class="alert alert-error"/>
     	</div>
    </div>
	<div class="controls">
		<input id="editDataLeaf" type="submit" value="Save Data" class="btn btn-primary"/>
	</div>
</form:form>