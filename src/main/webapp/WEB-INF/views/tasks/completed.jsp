<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<!-- Document Window -->
<h1><span class="glyphicon glyphicon-check" aria-hidden="true"></span> Completed Tasks</h1>
<a href='<c:url value="/tasks/completed/deleteall"/>'><span class="glyphicon glyphicon-trash" aria-hidden="true"></span> Delete All Completed Tasks</a>
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
                <th>Title</th>
                <th>Text</th>
                <th>Project</th>
            </tr>
            <c:forEach items="${dataList}" var="task">
                <tr>
                    <td>
                        <del><a alt="incomplete" href='<c:url value="/task/incomplete/${task.id}"/>'><span class="glyphicon glyphicon-check" aria-hidden="true"></span></a></del>
                    </td>
                    <td>
                        <del><a href='<c:url value="/task/detail/${task.id}"/>' class="dataDetailListTitle"
                           id="dataDetail_${task.id}" ><c:out
                                value="${task.title}" /></a></del>
                    </td>
                    <td>
                        <del><c:out value="${task.textShortened}" /></del>
                    </td>
                    <td>
                        <c:if test="${task.project != null}">
                            <a href='<c:url value="/project/${task.project.id}"/>'><span class="glyphicon glyphicon-folder-open" aria-hidden="true"></span> <c:out value="${task.project.name}" /></a>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>
    <nav>
        <c:url var="firstUrl" value="/tasks/completed?page=1" />
        <c:url var="lastUrl" value="/focus/completed?page=${totalPages}" />
        <c:url var="prevUrl" value="/focus/completed?page=${currentIndex - 1}" />
        <c:url var="nextUrl" value="/focus/completed?page=${currentIndex + 1}" />
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
                    <c:url var="pageUrl" value="/focus/completed?page=${i}" />
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
    <p>You have no completed Tasks</p>
</c:if>