<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<!-- Document Window -->
<h1><span class="glyphicon glyphicon-inbox" aria-hidden="true"></span> Inbox</h1>
<a href='<c:url value="/task/addtoproject/0"/>'><span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span> Add Task</a><br />
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
            </tr>
            <c:forEach items="${dataList}" var="task">
                <tr>
                    <td>
                        <a alt="complete" href='<c:url value="/task/complete/${task.id}"/>'><span class="glyphicon glyphicon-unchecked" aria-hidden="true"></span></a>
                    </td>
                    <td>
                        <a href='<c:url value="/task/detail/${task.id}"/>' class="dataDetailListTitle"
                           id="dataDetail_${task.id}" ><c:out
                                value="${task.title}" /></a>
                    </td>
                    <td>
                        <c:out value="${task.textShortened}" />
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>
    <nav>
        <c:url var="firstUrl" value="/focus/inbox?page=1" />
        <c:url var="lastUrl" value="/focus/inbox?page=${totalPages}" />
        <c:url var="prevUrl" value="/focus/inbox?page=${currentIndex - 1}" />
        <c:url var="nextUrl" value="/focus/inbox?page=${currentIndex + 1}" />
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
                    <c:url var="pageUrl" value="/focus/inbox?page=${i}" />
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
