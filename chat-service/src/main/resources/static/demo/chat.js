let stompClient = null;
window.addEventListener("DOMContentLoaded", function() {
    const socket = new SockJS("http://localhost:10002/chat");
    stompClient = Stomp.over(socket);

    stompClient.connect({}, (data) => {
       console.log("Connected: " + data);

       // 메세지 구독 - /topic/message
       stompClient.subscribe("/topic/message", (res) => {
           console.log("수신 데이터:", res);
       });
    });
});

function sendMessage() {
    const payload = {
        type: "SELLER",
        message: "안녕하세요"
    };
    stompClient.send("/app/c2d7b183-ec25-4c1d-bc16-e8d698bdeebc/message", {}, JSON.stringify(payload));
}