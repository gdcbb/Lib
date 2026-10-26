<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>图书列表 - 图书管理系统</title>
  <style>
    body { margin: 0; font-family: "Microsoft YaHei", Arial, sans-serif; background: #f2f4f7; color: #333; }
    .header { display: flex; justify-content: space-between; align-items: center;
              padding: 14px 24px; background: #fff; box-shadow: 0 1px 4px rgba(0,0,0,.06); }
    .header h1 { margin: 0; font-size: 18px; }
    .header .user { font-size: 14px; color: #555; }
    .header a { color: #2563eb; text-decoration: none; margin-left: 12px; }
    .role-badge { display: inline-block; padding: 2px 8px; border-radius: 10px;
            font-size: 12px; margin-left: 8px; vertical-align: middle; }
    .role-admin { background: #fef3c7; color: #92400e; }
    .role-user { background: #dbeafe; color: #1e40af; }
    .container { padding: 24px; }
    .toolbar { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
    .toolbar form { display: flex; gap: 8px; }
    input[type=text] { padding: 8px 10px; width: 240px; border: 1px solid #d0d5dd;
              border-radius: 4px; font-size: 14px; }
    .btn { display: inline-block; padding: 8px 14px; border: 0; border-radius: 4px;
           background: #2563eb; color: #fff; font-size: 14px; cursor: pointer; text-decoration: none; }
    .btn:hover { background: #1d4ed8; }
    .btn-plain { background: #fff; color: #2563eb; border: 1px solid #2563eb; }
    table { width: 100%; border-collapse: collapse; background: #fff;
            box-shadow: 0 1px 4px rgba(0,0,0,.06); }
    th, td { padding: 10px 12px; border-bottom: 1px solid #eef0f3; text-align: left; font-size: 14px; }
    th { background: #f8fafc; color: #555; font-weight: 600; }
    tr:hover td { background: #f9fbfd; }
    .link { color: #2563eb; text-decoration: none; margin-right: 10px; font-size: 14px; }
    .link-danger { color: #dc2626; background: none; border: 0; cursor: pointer;
            font-size: 14px; padding: 0; }
    .empty { padding: 40px; text-align: center; color: #999; background: #fff; }
  </style>
</head>
<body>
<div class="header">
  <h1>图书管理系统</h1>
  <div class="user">
    欢迎，<c:out value="${sessionScope.loginUser.realName}"/>
    <c:choose>
      <c:when test="${sessionScope.loginUser.role == 'admin'}">
        <span class="role-badge role-admin">管理员</span>
        <a href="${pageContext.request.contextPath}/admin/users">用户管理</a>
      </c:when>
      <c:otherwise>
        <span class="role-badge role-user">普通用户</span>
      </c:otherwise>
    </c:choose>
    <a href="${pageContext.request.contextPath}/logout">退出登录</a>
  </div>
</div>

<div class="container">
  <div class="toolbar">
    <form method="get" action="${pageContext.request.contextPath}/books">
      <input type="text" name="keyword" placeholder="按书名 / 作者 / ISBN 搜索"
             value="<c:out value='${keyword}'/>">
      <button class="btn" type="submit">搜索</button>
    </form>
    <c:if test="${sessionScope.loginUser.role == 'admin'}">
      <a class="btn" href="${pageContext.request.contextPath}/books/add">新增图书</a>
    </c:if>
  </div>

  <c:choose>
    <c:when test="${empty books}">
      <div class="empty">暂无图书数据</div>
    </c:when>
    <c:otherwise>
      <table>
        <thead>
        <tr>
          <th>ID</th>
          <th>ISBN</th>
          <th>书名</th>
          <th>作者</th>
          <th>出版社</th>
          <th>分类</th>
          <th>价格</th>
          <th>库存</th>
          <c:if test="${sessionScope.loginUser.role == 'admin'}">
            <th>操作</th>
          </c:if>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="book" items="${books}">
          <tr>
            <td>${book.id}</td>
            <td><c:out value="${book.isbn}"/></td>
            <td><c:out value="${book.title}"/></td>
            <td><c:out value="${book.author}"/></td>
            <td><c:out value="${book.publisher}"/></td>
            <td><c:out value="${book.category}"/></td>
            <td><fmt:formatNumber value="${book.price}" pattern="0.00"/></td>
            <td>${book.stock}</td>
            <c:if test="${sessionScope.loginUser.role == 'admin'}">
              <td>
                <a class="link"
                   href="${pageContext.request.contextPath}/books/edit?id=${book.id}">编辑</a>
                <form method="post" style="display:inline"
                      action="${pageContext.request.contextPath}/books/delete"
                      onsubmit="return confirm('确定删除这本图书吗？');">
                  <input type="hidden" name="id" value="${book.id}">
                  <button class="link-danger" type="submit">删除</button>
                </form>
              </td>
            </c:if>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </c:otherwise>
  </c:choose>
</div>
</body>
</html>
