<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="authenticated.assistant.form.label.supervisor" path="supervisor"/>
	<acme:input-textbox code="authenticated.assistant.form.label.expertiseField" path="expertiseField"/>
	<acme:input-textbox code="authenticated.assistant.form.label.resume" path="resume"/>
	<acme:input-url code="authenticated.assistant.form.label.furtherInformation" path="furtherInformation"/>
	
	<acme:submit test="${_command == 'create'}" code="authenticated.assistant.form.button.create" action="/authenticated/assistant/create"/>
	<acme:submit test="${_command == 'update'}" code="authenticated.assistant.form.button.update" action="/authenticated/assistant/update"/>
</acme:form>