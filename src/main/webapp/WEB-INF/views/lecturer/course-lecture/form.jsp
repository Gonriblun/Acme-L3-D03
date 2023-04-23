<%--
- form.jsp
-
- Copyright (C) 2012-2023 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>
<jstl:choose>	 
	<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
		<h2>
			<acme:message code="lecturer.courseLecture.form.lecture.delete.info"/>
		</h2>
	</jstl:when>
	<jstl:when test="${_command == 'create'}">
		<h2>
			<acme:message code="lecturer.courseLecture.form.lecture.info"/>
		</h2>

	</jstl:when>		
	

</jstl:choose>
	<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:message code="lecturer.courseLecture.form.lecture.title"/>
		</th>
		<td>
			<acme:print value="${lecture.getTitle()}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="lecturer.courseLecture.form.lecture.summary"/>
		</th>
		<td>
			<acme:print value="${lecture.getAbstractt()}"/>
		</td>
	</tr>
	</table>

<acme:form>
	<acme:input-select code="lecturer.courseLecture.form.label.course" path="course" choices="${courses}"/>	
	<jstl:choose>	 
		<jstl:when test="${acme:anyOf(_command, 'delete') && !cursos.isEmpty()}">
			<acme:submit code="lecturer.courseLecture.form.button.delete" action="/lecturer/course-lecture/delete?lectureId=${lectureId}"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="lecturer.courseLecture.form.button.create" action="/lecturer/course-lecture/create?lectureId=${lectureId}"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>
