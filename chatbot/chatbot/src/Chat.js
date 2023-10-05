import React, { useState } from 'react';
import axios from 'axios';

function Chat() {
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState('');
  const fetchReply = async (message) => {
    try {
      const { data } = await axios.post('http://localhost:8080/api/chat', message);
      setMessages((prevMessages) => [...prevMessages, { text: data.choices[0].text, sender: 'bot' }]);
    } catch (error) {
      console.error("Error fetching reply:", error);
    }
  }
  const handleSend = () => {
    setMessages((prevMessages) => [...prevMessages, { text: input, sender: 'user' }]);
    fetchReply(input);
    setInput('');
  };
  return (
    <div>
      {messages.map((message, index) => (
        <p key={index}><strong>{message.sender}:</strong> {message.text}</p>
      ))}
      <input value={input} onChange={e => setInput(e.target.value)} />
      <button onClick={handleSend}>Send</button>
    </div>
  );
}

export default Chat;