'use strict';

// Lấy các phần tử trên trang HTML để tương tác
const usernamePage = document.querySelector('#username-page');
const chatPage = document.querySelector('#chat-page');
const usernameForm = document.querySelector('#usernameForm');
const messageForm = document.querySelector('#messageForm');
const messageInput = document.querySelector('#message');
const messageArea = document.querySelector('#messageArea');
const connectingElement = document.querySelector('.connecting');

// Khai báo các biến sẽ sử dụng
let stompClient = null;
let username = null;

// Hàm để kết nối tới WebSocket server
function connect(event) {
    // Lấy tên người dùng từ form, loại bỏ khoảng trắng thừa
    username = document.querySelector('#name').value.trim();

    // Nếu có tên người dùng, tiến hành kết nối
    if (username) {
        // Ẩn trang đăng nhập và hiện trang chat
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        // Tạo một kết nối WebSocket mới bằng SockJS
        const socket = new SockJS('/ws');
        // Tạo một STOMP client trên nền kết nối WebSocket đó
        stompClient = Stomp.over(socket);

        // Bắt đầu kết nối tới server
        stompClient.connect({}, onConnected, onError);
    }
    // Ngăn form submit và tải lại trang
    event.preventDefault();
}

// Hàm xử lý khi đã kết nối thành công
function onConnected() {
    // Đăng ký (subscribe) vào topic '/topic/public'.
    // Bất kỳ tin nhắn nào được gửi tới topic này, hàm onMessageReceived sẽ được gọi.
    stompClient.subscribe('/topic/public', onMessageReceived);

    // Gửi một tin nhắn tới server để thông báo người dùng mới đã tham gia
    stompClient.send("/app/chat.addUser",
        {},
        JSON.stringify({sender: username, type: 'JOIN'})
    );

    // Ẩn thông báo "Đang kết nối..."
    connectingElement.classList.add('hidden');
}

// Hàm xử lý khi kết nối thất bại
function onError(error) {
    connectingElement.textContent = 'Không thể kết nối WebSocket. Vui lòng thử lại!';
    connectingElement.style.color = 'red';
}

// Hàm để gửi tin nhắn chat
function sendMessage(event) {
    const messageContent = messageInput.value.trim();

    // Chỉ gửi nếu có nội dung và kết nối đang hoạt động
    if (messageContent && stompClient) {
        const chatMessage = {
            sender: username,
            content: messageInput.value,
            type: 'CHAT'
        };

        // Gửi tin nhắn tới đích '/app/chat.sendMessage'
        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        // Xóa nội dung trong ô nhập liệu sau khi gửi
        messageInput.value = '';
    }
    event.preventDefault();
}

// Hàm xử lý khi nhận được tin nhắn từ server
function onMessageReceived(payload) {
    // Chuyển đổi chuỗi JSON nhận được thành object JavaScript
    const message = JSON.parse(payload.body);

    // Tạo một phần tử <li> mới cho tin nhắn
    const messageElement = document.createElement('li');

    // Xử lý tin nhắn sự kiện (tham gia hoặc rời phòng)
    if (message.type === 'JOIN' || message.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        message.content = message.sender + (message.type === 'JOIN' ? ' đã tham gia!' : ' đã rời đi!');

        const textElement = document.createElement('p');
        const messageText = document.createTextNode(message.content);
        textElement.appendChild(messageText);
        messageElement.appendChild(textElement);

    // Xử lý tin nhắn chat thông thường
    } else {
        messageElement.classList.add('chat-message');

        // *** LOGIC QUAN TRỌNG: CĂN CHỈNH TIN NHẮN TRÁI/PHẢI ***
        // Nếu người gửi tin nhắn là người dùng hiện tại, thêm class 'sender-message'
        if (message.sender === username) {
            messageElement.classList.add('sender-message');
        // Ngược lại, thêm class 'receiver-message'
        } else {
            messageElement.classList.add('receiver-message');
        }

        // Tạo và thêm tên người gửi
        const usernameElement = document.createElement('span');
        usernameElement.classList.add('sender');
        const usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);

        // Tạo và thêm nội dung tin nhắn
        const textElement = document.createElement('p');
        const messageText = document.createTextNode(message.content);
        textElement.appendChild(messageText);
        messageElement.appendChild(textElement);
    }

    // Thêm tin nhắn mới vào khu vực chat và cuộn xuống dưới cùng
    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}

// Gán các hàm xử lý sự kiện cho form
usernameForm.addEventListener('submit', connect, true);
messageForm.addEventListener('submit', sendMessage, true);