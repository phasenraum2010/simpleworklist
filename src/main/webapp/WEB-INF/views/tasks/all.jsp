<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<!-- Document Window -->
<h1><span class="glyphicon glyphicon-list" aria-hidden="true"></span> <spring:message code="tasks.all.h1" text="All Tasks" /></h1>
<c:if test="${! empty message}">
    <div class="alert alert-danger alert-dismissible" role="alert">
        <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <c:out value="${message}"></c:out>
    </div>
</c:if>
<c:if test="${! empty dataList}">
    <div>
        <table class="table table-striped table-hover">
            <tr>
                <th>&nbsp;</th>
                <c:choose>
                    <c:when test="${sortDir eq 'desc'}">
                        <c:set var="sortDirection" value="asc"/>
                        <c:set var="sign" value="down" />
                    </c:when>
                    <c:otherwise>
                        <c:set var="sortDirection" value="desc"/>
                        <c:set var="sign" value="up" />
                    </c:otherwise>
                </c:choose>
                <c:choose>
                    <c:when test="${sort eq 'title'}">
                        <c:set var="urlpostfix" value="?sort=title&sortDir=${sortDirection}"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="urlpostfix" value="?sort=title&sortDir=desc"/>
                        <c:set var="sign" value="up" />
                    </c:otherwise>
                </c:choose>
                <th>
                    <a href="${urlpostfix}">
                        <spring:message code="tasks.focus.title" text="Title" />
                        <span class="glyphicon glyphicon-chevron-${sign}" aria-hidden="true"></span>
                    </a>
                </th>
                <c:choose>
                    <c:when test="${sortDir eq 'desc'}">
                        <c:set var="sortDirection" value="asc"/>
                        <c:set var="sign" value="down" />
                    </c:when>
                    <c:otherwise>
                        <c:set var="sortDirection" value="desc"/>
                        <c:set var="sign" value="up" />
                    </c:otherwise>
                </c:choose>
                <c:choose>
                    <c:when test="${sort eq 'text'}">
                        <c:set var="urlpostfix" value="?sort=text&sortDir=${sortDirection}"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="urlpostfix" value="?sort=text&sortDir=desc"/>
                        <c:set var="sign" value="up" />
                    </c:otherwise>
                </c:choose>
                <th>
                    <a href="${urlpostfix}">
                        <spring:message code="tasks.focus.text" text="Text" />
                        <span class="glyphicon glyphicon-chevron-${sign}" aria-hidden="true"></span>
                    </a>
                </th>
                <c:choose>
                    <c:when test="${sortDir eq 'desc'}">
                        <c:set var="sortDirection" value="asc"/>
                        <c:set var="sign" value="down" />
                    </c:when>
                    <c:otherwise>
                        <c:set var="sortDirection" value="desc"/>
                        <c:set var="sign" value="up" />
                    </c:otherwise>
                </c:choose>
                <c:choose>
                    <c:when test="${sort eq 'duedate'}">
                        <c:set var="urlpostfix" value="?sort=duedate&sortDir=${sortDirection}"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="urlpostfix" value="?sort=duedate&sortDir=desc"/>
                        <c:set var="sign" value="up" />
                    </c:otherwise>
                </c:choose>
                <th>
                    <a href="${urlpostfix}">
                        <spring:message code="tasks.focus.dueDate" text="Due Date" />
                        <span class="glyphicon glyphicon-chevron-${sign}" aria-hidden="true"></span>
                    </a>
                </th>
                <c:choose>
                    <c:when test="${sortDir eq 'desc'}">
                        <c:set var="sortDirection" value="asc"/>
                        <c:set var="sign" value="down" />
                    </c:when>
                    <c:otherwise>
                        <c:set var="sortDirection" value="desc"/>
                        <c:set var="sign" value="up" />
                    </c:otherwise>
                </c:choose>
                <c:choose>
                    <c:when test="${sort eq 'state'}">
                        <c:set var="urlpostfix" value="?sort=state&sortDir=${sortDirection}"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="urlpostfix" value="?sort=state&sortDir=desc"/>
                        <c:set var="sign" value="up" />
                    </c:otherwise>
                </c:choose>
                <th>
                    <a href="${urlpostfix}">
                        <spring:message code="tasks.focus.taskState" text="Task State" />
                        <span class="glyphicon glyphicon-chevron-${sign}" aria-hidden="true"></span>
                    </a>
                </th>
                <c:choose>
                    <c:when test="${sortDir eq 'desc'}">
                        <c:set var="sortDirection" value="asc"/>
                        <c:set var="sign" value="down" />
                    </c:when>
                    <c:otherwise>
                        <c:set var="sortDirection" value="desc"/>
                        <c:set var="sign" value="up" />
                    </c:otherwise>
                </c:choose>
                <c:choose>
                    <c:when test="${sort eq 'project'}">
                        <c:set var="urlpostfix" value="?sort=project&sortDir=${sortDirection}"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="urlpostfix" value="?sort=project&sortDir=desc"/>
                        <c:set var="sign" value="up" />
                    </c:otherwise>
                </c:choose>
                <th>
                    <a href="${urlpostfix}">
                        <spring:message code="tasks.focus.project" text="Project" />
                        <span class="glyphicon glyphicon-chevron-${sign}" aria-hidden="true"></span>
                    </a>
                </th>
                <th>&nbsp;</th>
                <c:choose>
                    <c:when test="${sortDir eq 'desc'}">
                        <c:set var="sortDirection" value="asc"/>
                        <c:set var="sign" value="down" />
                    </c:when>
                    <c:otherwise>
                        <c:set var="sortDirection" value="desc"/>
                        <c:set var="sign" value="up" />
                    </c:otherwise>
                </c:choose>
                <c:choose>
                    <c:when test="${sort eq 'context'}">
                        <c:set var="urlpostfix" value="?sort=context&sortDir=${sortDirection}"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="urlpostfix" value="?sort=context&sortDir=desc"/>
                        <c:set var="sign" value="up" />
                    </c:otherwise>
                </c:choose>
                <th>
                    <a href="${urlpostfix}">
                        <spring:message code="tasks.all.context" text="Context" />
                        <span class="glyphicon glyphicon-chevron-${sign}" aria-hidden="true"></span>
                    </a>
                </th>
            </tr>
            <c:forEach items="${dataList}" var="task">
                <tr>
                    <td>
                        <c:if test="${task.taskState.name() eq 'COMPLETED'}">
                            <a alt="complete" href='<c:url value="/task/incomplete/${task.id}"/>'><span class="glyphicon glyphicon-check" aria-hidden="true"></span></a>
                        </c:if>
                        <c:if test="${task.taskState.name() ne 'COMPLETED'}">
                            <a alt="complete" href='<c:url value="/task/complete/${task.id}"/>'><span class="glyphicon glyphicon-unchecked" aria-hidden="true"></span></a>
                        </c:if>
                        &nbsp;&nbsp;
                        <c:if test="${task.focus}">
                            <a alt="focus" href='<c:url value="/task/unsetfocus/${task.id}"/>'><span class="glyphicon glyphicon-star" aria-hidden="true"></span></a>
                        </c:if>
                        <c:if test="${! task.focus}">
                            <a alt="focus" href='<c:url value="/task/setfocus/${task.id}"/>'><span class="glyphicon glyphicon-star-empty" aria-hidden="true"></span></a>
                        </c:if>
                    </td>
                    <td>
                        <c:if test="${task.taskState.name() eq 'COMPLETED'}">
                        <del>
                            </c:if>
                            <a href='<c:url value="/task/detail/${task.id}"/>' class="dataDetailListTitle"
                               id="dataDetail_${task.id}" ><c:out
                                    value="${task.title}" /></a>
                            <c:if test="${task.taskState.name() eq 'COMPLETED'}">
                        </del>
                        </c:if>
                    </td>
                    <td>
                        <c:if test="${task.taskState.name() eq 'COMPLETED'}">
                        <del>
                            </c:if>
                            <c:out value="${task.textShortened}" />
                            <c:if test="${task.taskState.name() eq 'COMPLETED'}">
                        </del>
                        </c:if>
                    </td>
                    <td>
                        <fmt:formatDate value="${task.dueDate}" />
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${task.taskState.name() eq 'INBOX'}">
                                <a href='<c:url value="/tasks/inbox"/>'><span class="glyphicon glyphicon-inbox" aria-hidden="true"></span> <spring:message code="tasks.focus.inbox" text="Inbox" /></a>
                            </c:when>
                            <c:when test="${task.taskState.name() eq 'TODAY'}">
                                <a href='<c:url value="/tasks/today"/>'><span class="glyphicon glyphicon glyphicon-time" aria-hidden="true"></span> <spring:message code="tasks.focus.today" text="Today" /></a>
                            </c:when>
                            <c:when test="${task.taskState.name() eq 'NEXT'}">
                                <a href='<c:url value="/tasks/next"/>'><span class="glyphicon glyphicon-cog" aria-hidden="true"></span> <spring:message code="tasks.focus.next" text="Next" /></a>
                            </c:when>
                            <c:when test="${task.taskState.name() eq 'WAITING'}">
                                <a href='<c:url value="/tasks/waiting"/>'><span class="glyphicon glyphicon-hourglass" aria-hidden="true"></span> <spring:message code="tasks.focus.waiting" text="Waiting" /></a>
                            </c:when>
                            <c:when test="${task.taskState.name() eq 'SCHEDULED'}">
                                <a href='<c:url value="/tasks/scheduled"/>'><span class="glyphicon glyphicon-calendar" aria-hidden="true"></span> <spring:message code="tasks.focus.scheduled" text="Scheduled" /></a>
                            </c:when>
                            <c:when test="${task.taskState.name() eq 'SOMEDAY'}">
                                <a href='<c:url value="/tasks/someday"/>'><span class="glyphicon glyphicon-road" aria-hidden="true"></span> <spring:message code="tasks.focus.someday" text="Someday" /></a>
                            </c:when>
                            <c:when test="${task.taskState.name() eq 'COMPLETED'}">
                                <a href='<c:url value="/tasks/completed"/>'><span class="glyphicon glyphicon-check" aria-hidden="true"></span> <spring:message code="tasks.focus.completed" text="Completed Tasks" /></a>
                            </c:when>
                            <c:when test="${task.taskState.name() eq 'TRASHED'}">
                                <a href='<c:url value="/tasks/trash"/>'><span class="glyphicon glyphicon-trash" aria-hidden="true"></span> <spring:message code="tasks.focus.trash" text="Trash" /></a>
                            </c:when>
                        </c:choose>
                    </td>
                    <td>
                        <c:if test="${task.project != null}">
                            <a href='<c:url value="/project/${task.project.id}"/>'><span class="glyphicon glyphicon-folder-open" aria-hidden="true"></span> <c:out value="${task.project.name}" /></a>
                        </c:if>
                    </td>
                    <td>
                        <c:if test="${task.taskEnergy ne 'NONE'}">
                            <span class="badge"><spring:message code="${task.taskEnergy.code}" /></span>
                        </c:if>
                        <c:if test="${task.taskTime ne 'NONE'}">
                            <span class="badge"><spring:message code="${task.taskTime.code}" /></span>
                        </c:if>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${locale eq 'de'}">
                                <c:out value="${task.context.nameDe}"/>
                            </c:when>
                            <c:otherwise>
                                <c:out value="${task.context.nameEn}"/>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>
    <nav>
        <c:url var="firstUrl" value="/tasks/all?sort=${sort}&sortDir=${sortDir}&page=1" />
        <c:url var="lastUrl" value="/tasks/all?sort=${sort}&sortDir=${sortDir}&page=${totalPages}" />
        <c:url var="prevUrl" value="/tasks/all?sort=${sort}&sortDir=${sortDir}&page=${currentIndex - 1}" />
        <c:url var="nextUrl" value="/tasks/all?sort=${sort}&sortDir=${sortDir}&page=${currentIndex + 1}" />
        <div>
            <ul class="pagination">
                <c:choose>
                    <c:when test="${currentIndex == 1}">
                        <li class="disabled"><a href="#">&lt;&lt;</a></li>
                        <li class="disabled"><a href="#">&lt;</a></li>
                    </c:when>
                    <c:otherwise>
                        <li><a href="${firstUrl}">&lt;&lt;</a></li>
                        <li><a href="${prevUrl}">&lt;</a></li>
                    </c:otherwise>
                </c:choose>
                <c:forEach var="i" begin="${beginIndex}" end="${endIndex}">
                    <c:url var="pageUrl" value="/tasks/all?sort=${sort}&sortDir=${sortDir}&page=${i}" />
                    <c:choose>
                        <c:when test="${i == currentIndex}">
                            <li class="active"><a href="${pageUrl}"><c:out value="${i}" /></a></li>
                        </c:when>
                        <c:otherwise>
                            <li><a href="${pageUrl}"><c:out value="${i}" /></a></li>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
                <c:choose>
                    <c:when test="${currentIndex == totalPages}">
                        <li class="disabled"><a href="#">&gt;</a></li>
                        <li class="disabled"><a href="#">&gt;&gt;</a></li>
                    </c:when>
                    <c:otherwise>
                        <li><a href="${nextUrl}">&gt;</a></li>
                        <li><a href="${lastUrl}">&gt;&gt;</a></li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </nav>
</c:if>
<c:if test="${empty dataList}">
    <p><spring:message code="tasks.all.noTasks" text="You have no Tasks at all" /></p>
</c:if>