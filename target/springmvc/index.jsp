<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
    request.setAttribute("basePath",basePath);
%>
<html>
<body>
<h2>Hello World!</h2>
<script src="http://cdn.sockjs.org/sockjs-0.3.min.js"></script>
<script type="text/javascript">
    var path = '<%=basePath%>';
    var userId = 'lys';
    if(userId==-1){
        window.location.href="<%=basePath%>";
    }
    var jspCode = userId+"_AAA";
    var websocket;
    if ('WebSocket' in window) {
        websocket = new WebSocket("ws://localhost:8080/springmvc/wsMy?jspCode=" + jspCode);
    } else if ('MozWebSocket' in window) {
        websocket = new MozWebSocket("ws://localhost:8080/springmvc/wsMy?jspCode=" + jspCode);
    } else {
        websocket = new SockJS("http://localhost:8080/springmvc/wsMy/sockjs?jspCode=" + jspCode);
    }
    websocket.onopen = function(event) {
        console.log("WebSocket:已连接");
        console.log(event);
    };
    websocket.onmessage = function(event) {
        var data = JSON.parse(event.data);
        console.log("WebSocket:收到一条消息-norm", data);
        alert("WebSocket:收到一条消息");
    };
    websocket.onerror = function(event) {
        console.log("WebSocket:发生错误 ");
        console.log(event);
    };
    websocket.onclose = function(event) {
        console.log("WebSocket:已关闭");
        console.log(event);
    }

</script>
</body>
</html>
