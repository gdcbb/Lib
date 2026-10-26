<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>用户管理 - 图书管理系统</title>
  <style>
    body { margin: 0; font-family: "Microsoft YaHei", Arial, sans-serif; background: #f2f4f7; color: #333; }
    .header { display: flex; justify-content: space-between; align-items: center;
              padding: 14px 24px; background: #fff; box-shadow: 0 1px 4px rgba(0,0,0,.06); }
    .header h1 { margin: 0; font-size: 18px; }
    .header a { color: #2563eb; text-decoration: none; margin-left: 12px; }
    .container { padding: 24px; }
    .toolbar { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
    .btn { display: inline-block; padding: 8px 14px; border: 0; border-radius: 4px;
           background: #2563eb; color: #fff; font-size: 14px; cursor: pointer; text-decoration: none; }
    .btn:hover { background: #1d4ed8; }
    table { width: 100%; border-collapse: collapse; background: #fff;
            box-shadow: 0 1px 4px rgba(0,0,0,.06); }
    th, td { padding: 10px 12px; border-bottom: 1px solid #eef0f3; text-align: left; font-size: 14px; }
    th { background: #f8fafc; color: #555; font-weight: 600; }
    .role-badge { display: inline-block; padding: 2px 8px; border-radius: 10px; font-size: 12px; }
    .role-admin { background: #fef3c7; color: #92400e; }
    .role-user { background: #dbeafe; color: #1e40af; }
    .empty { padding: 40px; text-align: center; color: #999; background: #fff; }
  </style>
</head>
<body>
<div class="header">
  <h1>用户管理</h1>
  <div>
    <a href="${pageContext.request.contextPath}/books">返回图书列表</a>
    <a href="${pageContext.request.contextPath}/logout">退出登录</a>
  </div>
</div>

<div class="container">
  <c:choose>
    <c:when test="${empty users}">
      <div class="empty">暂无用户</div>
    </c:when>
    <c:otherwise>
      <table>
        <thead>
        <tr>
          <th>ID</th>
          <th>用户名</th>
          <th>昵称</th>
          <th>角色</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="u" items="${users}">
          <tr>
            <td>${u.id}</td>
            <td><c:out value="${u.username}"/></td>
            <td><c:out value="${u.realName}"/></td>
            <td>
              <c:choose>
                <c:when test="${u.role == 'admin'}">
                  <span class="role-badge role-admin">管理员</span>
                </c:when>
                <c:otherwise>
                  <span class="role-badge role-user">普通用户</span>
                </c:otherwise>
              </c:choose>
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </c:otherwise>
  </c:choose>
</div>
</body>
</html>
