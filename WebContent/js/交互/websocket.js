var websocket={
	init : function() {
		var websocket=null;
		if ('WebSocket' in window) {
			// 所有群websocket
			websocket = new WebSocket(
					"ws://localhost:8080/The_vertical_and_horizontal/websocket" );
		} else {
			alert('当前浏览器 Not support websocket')
		}
		return websocket;
	}
}
