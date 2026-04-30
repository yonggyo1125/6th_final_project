let stompClient = null;
// 임시 방 ID (실제 운영 시에는 이전 페이지에서 전달받아야 합니다)
const ROOM_ID = "c2d7b183-ec25-4c1d-bc16-e8d698bdeebc";

window.addEventListener("DOMContentLoaded", function() {
    connectChat();
});

function connectChat() {
    const socket = new SockJS("http://localhost:10002/chat");
    stompClient = Stomp.over(socket);

    // 콘솔 로그 간소화
    stompClient.debug = null;

    stompClient.connect({}, (frame) => {
        console.log("연결 성공: " + frame);

        // 백엔드 제안에 맞춰 수정된 방 번호 기반 토픽 구독
        stompClient.subscribe(`/topic/room/${ROOM_ID}`, (response) => {
            const data = JSON.parse(response.body);
            displayMessage(data);
        });
    });
}

function sendMessage() {
    const messageInput = document.getElementById("message-input");
    const content = messageInput.value.trim();
    const currentRole = document.getElementById("user-role").value; // BUYER or SELLER

    if (content === "") return;

    const payload = {
        type: currentRole,
        message: content
    };

    if (stompClient && stompClient.connected) {
        stompClient.send(`/app/${ROOM_ID}/message`, {}, JSON.stringify(payload));
        messageInput.value = "";
    } else {
        alert("채팅 서버와 연결되어 있지 않습니다.");
    }
}

// 엔터키 입력 처리
function handleKeyPress(event) {
    if (event.key === "Enter") {
        sendMessage();
    }
}

// 수신한 메세지를 UI에 렌더링
function displayMessage(data) {
    const chatBox = document.getElementById("chat-box");
    const currentRole = document.getElementById("user-role").value;

    const messageDiv = document.createElement("div");
    messageDiv.classList.add("message");

    // 보낸 사람의 타입(BUYER/SELLER)이 현재 선택된 내 역할과 같으면 'mine', 다르면 'other'
    if (data.type === currentRole) {
        messageDiv.classList.add("mine");
    } else {
        messageDiv.classList.add("other");

        // 상대방 메세지인 경우 발신자 표시
        const senderSpan = document.createElement("span");
        senderSpan.classList.add("message-sender");
        senderSpan.textContent = data.type === "SELLER" ? "판매자" : "구매자";
        messageDiv.appendChild(senderSpan);
    }

    const textNode = document.createTextNode(data.message);
    messageDiv.appendChild(textNode);
    chatBox.appendChild(messageDiv);

    // 스크롤을 항상 최하단으로 이동
    chatBox.scrollTop = chatBox.scrollHeight;
}

// 거래 상태 변경 처리 (완료 / 취소)
function updateRoomStatus(status) {
    let confirmMsg = status === 'COMPLETED' ? "거래를 완료하시겠습니까?" : "거래를 취소하고 채팅을 종료하시겠습니까?";

    if(!confirm(confirmMsg)) return;

    const endpoint = status === 'COMPLETED' ? 'complete' : 'cancel';

    // 백엔드 제안 코드의 API 호출 (Fetch API 사용)
    fetch(`http://localhost:10002/api/chat/${ROOM_ID}/${endpoint}`, {
        method: 'POST',
    })
        .then(response => {
            if(response.ok) {
                alert("처리되었습니다.");
                // 지시사항 3, 4번에 따라 채팅 종료 처리
                if (stompClient) stompClient.disconnect();
                document.getElementById("message-input").disabled = true;

                const chatBox = document.getElementById("chat-box");
                const endMsg = document.createElement("div");
                endMsg.classList.add("system-message");
                endMsg.textContent = status === 'COMPLETED' ? "거래가 완료되어 채팅이 종료되었습니다." : "거래가 취소되어 채팅이 종료되었습니다.";
                chatBox.appendChild(endMsg);
                chatBox.scrollTop = chatBox.scrollHeight;
            } else {
                alert("처리 중 오류가 발생했습니다.");
            }
        })
        .catch(error => console.error('Error:', error));
}