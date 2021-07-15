'use strict';

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');

var stompClient = null;
var username = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

function connect(event) {
    username = document.querySelector('#name').value.trim();

    if (username) {
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        var socket = new SockJS('/web-socket');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
}


function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe('/topic/public/broadcast', onMessageReceived);

    // Tell your username to the server
    // stompClient.send(`/app/join/${username}`, {},)

    connectingElement.classList.add('hidden');
}


function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}


function sendMessage(event) {
    var messageContent = messageInput.value.trim();

    if (messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            receiver: null,
            content: messageInput.value
        };

        stompClient.send("/app/send-message", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}


function onMessageReceived(payload) {
    var body = JSON.parse(payload.body);
    console.info('body', body);

    var messageElement = document.createElement('li');

    if (body.type === 'JOIN') {
        messageElement.classList.add('event-message');
        body.content = body.sender + ' joined!';
    } else if (body.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        body.content = body.sender + ' left!';
    } else {
        messageElement.classList.add('chat-message');

        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(body.sender[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(body.sender);

        messageElement.appendChild(avatarElement);

        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(body.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    }

    var textElement = document.createElement('p');
    var messageText = document.createTextNode(body.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}


function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }

    var index = Math.abs(hash % colors.length);
    return colors[index];
}

usernameForm.addEventListener('submit', connect, true)
messageForm.addEventListener('submit', sendMessage, true)
