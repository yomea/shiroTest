<!-- 引入包，与JSP中声明<%-- @ page --%>类似 -->
<%@ tag import="org.apache.shiro.util.StringUtils" %>
<%@ tag import="org.apache.shiro.SecurityUtils" %>
<%@ tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ attribute name="name" type="java.lang.String" required="true" description="权限字符串列表" %>
<%@ attribute name="delimiter" type="java.lang.String" required="false" description="权限字符串列表分隔符" %>
<%

	//如果没有传递delimiter，则默认是逗号
   if(!StringUtils.hasText(delimiter)) {
        delimiter = ",";//默认逗号分隔
    }
	/* 如果name是空字符或者null，则这个标签会执行标签体 */
    if(!StringUtils.hasText(name)) {
%>
        <jsp:doBody/>
<%
        return;
    }

    String[] roles = name.split(delimiter);

    if(!SecurityUtils.getSubject().isPermittedAll(roles)) {
        return;
    } else {
%>
        <jsp:doBody/>
<%
    }
%>