<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
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
    .btn-borrow { padding: 5px 12px; border: 0; border-radius: 4px;
            background: #16a34a; color: #fff; font-size: 13px; cursor: pointer; }
    .btn-borrow:hover { background: #15803d; }
    .btn-borrow:disabled { background: #d1d5db; color: #9ca3af; cursor: not-allowed; }
    .mask { display: none; position: fixed; inset: 0; background: rgba(0,0,0,.45);
            align-items: center; justify-content: center; z-index: 10; }
    .mask.show { display: flex; }
    .dialog { width: 320px; background: #fff; border-radius: 8px; padding: 20px 22px;
            box-shadow: 0 10px 30px rgba(0,0,0,.2); }
    .dialog h3 { margin: 0 0 12px; font-size: 16px; }
    .dialog .book-name { margin: 0 0 16px; font-size: 14px; color: #555; }
    .dialog label { font-size: 14px; }
    .dialog select { padding: 6px 8px; border: 1px solid #d0d5dd; border-radius: 4px;
            font-size: 14px; margin: 0 6px; }
    .dialog-actions { margin-top: 20px; text-align: right; }
    .dialog-actions button { padding: 7px 16px; font-size: 14px; border-radius: 4px;
            border: 1px solid #d0d5dd; background: #fff; cursor: pointer; margin-left: 8px; }
    .dialog-actions .confirm { background: #2563eb; border-color: #2563eb; color: #fff; }
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
        <a href="${pageContext.request.contextPath}/admin/borrows">借阅记录</a>
      </c:when>
      <c:otherwise>
        <span class="role-badge role-user">普通用户</span>
      </c:otherwise>
    </c:choose>
    <a href="${pageContext.request.contextPath}/borrows">我的借阅</a>
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
          <th>借阅</th>
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
            <td>
              <c:choose>
                <c:when test="${book.stock > 0}">
                  <button class="btn-borrow" type="button"
                          data-id="${book.id}"
                          data-title="<c:out value='${book.title}'/>"
                          onclick="openBorrow(this)">借阅</button>
                </c:when>
                <c:otherwise>
                  <button class="btn-borrow" type="button" disabled>借阅</button>
                </c:otherwise>
              </c:choose>
            </td>
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

<div class="mask" id="borrowMask">
  <div class="dialog">
    <h3>借阅图书</h3>
    <p class="book-name" id="borrowBookName"></p>
    <label>借阅时间：
      <select id="borrowDays">
        <option value="1">1 天</option>
        <option value="2">2 天</option>
        <option value="3">3 天</option>
        <option value="4">4 天</option>
        <option value="5">5 天</option>
        <option value="6">6 天</option>
        <option value="7">7 天</option>
      </select>
    </label>
    <div class="dialog-actions">
      <button type="button" onclick="closeBorrow()">取消</button>
      <button type="button" class="confirm" onclick="confirmBorrow()">确定</button>
    </div>
  </div>
</div>

<form id="borrowForm" method="post"
      action="${pageContext.request.contextPath}/books/borrow">
  <input type="hidden" name="id" id="borrowId">
  <input type="hidden" name="days" id="borrowDaysInput">
</form>

<script>
  function openBorrow(button) {
    document.getElementById('borrowId').value = button.dataset.id;
    document.getElementById('borrowBookName').textContent = button.dataset.title;
    document.getElementById('borrowDays').value = '1';
    document.getElementById('borrowMask').classList.add('show');
  }

  function closeBorrow() {
    document.getElementById('borrowMask').classList.remove('show');
  }

  function confirmBorrow() {
    document.getElementById('borrowDaysInput').value =
        document.getElementById('borrowDays').value;
    document.getElementById('borrowForm').submit();
  }
</script>

<c:if test="${param.borrowed == '1'}">
  <script>alert('借阅成功');</script>
</c:if>
</body>
</html>
