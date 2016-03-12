<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<h1><spring:message code="search.resultlist.h1" text="Search Result for searchterm" /> <i>${searchResult.searchterm}</i></h1>
<c:if test="${empty searchResult.taskList}">
<h2><spring:message code="search.resultlist.h2.noTasks" text="No Tasks found" /></h2>
</c:if>
<c:if test="${! empty searchResult.taskList}">
<h2><spring:message code="search.resultlist.h2.tasks" text="Tasks" /></h2>
<table class="table table-striped table-hover">
<tr>
    <th><spring:message code="search.resultlist.tasks.title" text="Title" /></th>
    <th><spring:message code="search.resultlist.tasks.text" text="Text" /></th>
    <th><spring:message code="search.resultlist.tasks.dueDate" text="Due Date" /></th>
    <th><spring:message code="search.resultlist.tasks.state" text="State" /></th>
    <th><spring:message code="search.resultlist.tasks.project" text="Project" /></th>
</tr>
<c:forEach var="task" items="${searchResult.taskList}">
    <tr>
        <td>
            <a href='<c:url value="/task/detail/${task.id}"/>' class="dataDetailListTitle"
               id="dataDetail_${task.id}" ><c:out
                    value="${task.title}" /></a>
        </td>
        <td>
            <c:out value="${task.textShortened}"></c:out>
        </td>
        <td>
            <c:out value="${task.dueDate}" />
        </td>
        <td>
            <c:choose>
                <c:when test="${task.taskState.name() eq 'INBOX'}">
                    <a href='<c:url value="/tasks/inbox"/>'><span class="glyphicon glyphicon-inbox" aria-hidden="true"></span> <spring:message code="search.resultlist.tasks.inbox" text="Inbox" /></a>
                </c:when>
                <c:when test="${task.taskState.name() eq 'TODAY'}">
                    <a href='<c:url value="/tasks/today"/>'><span class="glyphicon glyphicon glyphicon-time" aria-hidden="true"></span> <spring:message code="search.resultlist.tasks.today" text="Today" /></a>
                </c:when>
                <c:when test="${task.taskState.name() eq 'NEXT'}">
                    <a href='<c:url value="/tasks/next"/>'><span class="glyphicon glyphicon-cog" aria-hidden="true"></span> <spring:message code="search.resultlist.tasks.next" text="Next" /></a>
                </c:when>
                <c:when test="${task.taskState.name() eq 'WAITING'}">
                    <a href='<c:url value="/tasks/waiting"/>'><span class="glyphicon glyphicon-hourglass" aria-hidden="true"></span> <spring:message code="search.resultlist.tasks.waiting" text="Waiting" /></a>
                </c:when>
                <c:when test="${task.taskState.name() eq 'SCHEDULED'}">
                    <a href='<c:url value="/tasks/scheduled"/>'><span class="glyphicon glyphicon-calendar" aria-hidden="true"></span> <spring:message code="search.resultlist.tasks.scheduled" text="Scheduled" /></a>
                </c:when>
                <c:when test="${task.taskState.name() eq 'SOMEDAY'}">
                    <a href='<c:url value="/tasks/someday"/>'><span class="glyphicon glyphicon-road" aria-hidden="true"></span> <spring:message code="search.resultlist.tasks.someday" text="Someday" /></a>
                </c:when>
                <c:when test="${task.taskState.name() eq 'COMPLETED'}">
                    <a href='<c:url value="/tasks/completed"/>'><span class="glyphicon glyphicon-check" aria-hidden="true"></span> <spring:message code="search.resultlist.tasks.completed" text="Completed Tasks" /></a>
                </c:when>
                <c:when test="${task.taskState.name() eq 'TRASHED'}">
                    <a href='<c:url value="/tasks/trash"/>'><span class="glyphicon glyphicon-trash" aria-hidden="true"></span> <spring:message code="search.resultlist.tasks.trash" text="Trash" /></a>
                </c:when>
            </c:choose>
        </td>
        <td>
            <c:if test="${task.project != null}">
                <a href='<c:url value="/project/${task.project.id}"/>'><span class="glyphicon glyphicon-folder-open" aria-hidden="true"></span> <c:out value="${task.project.name}" /></a>
            </c:if>
        </td>
    </tr>
</c:forEach>
</table>
</c:if>
<c:if test="${empty searchResult.projectList}">
    <h2><spring:message code="search.resultlist.projects.h2.noProjects" text="No Projects found" /></h2>
</c:if>
<c:if test="${! empty searchResult.projectList}">
<h2><spring:message code="search.resultlist.projects.h2.projects" text="Projects" /></h2>
    <table class="table table-striped table-hover">
        <tr>
            <th><spring:message code="search.resultlist.projects.name" text="Name" /></th>
            <th><spring:message code="search.resultlist.projects.description" text="Description" /></th>
        </tr>
    <c:forEach var="cat" items="${searchResult.projectList}">
        <tr>
            <td>
                <a href='<c:url value="/project/${cat.id}"/>'
                   id="categoryList_${cat.id}" ><c:out value="${cat.name}" /></a>
            </td>
            <td>
                <c:out value="${cat.description}" />
            </td>
        </tr>
    </c:forEach>
    </table>
</c:if>
