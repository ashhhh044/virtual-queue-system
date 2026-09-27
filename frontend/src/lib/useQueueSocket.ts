import { useEffect, useRef, useState } from "react";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";

const WS_URL = import.meta.env.VITE_WS_URL;

export function useQueueSocket<T>(topic: string | null, onMessage: (data: T) => void) {
  const [connected, setConnected] = useState(false);
  const onMessageRef = useRef(onMessage);
  onMessageRef.current = onMessage;

  useEffect(() => {
    if (!topic) return;

    const client = new Client({
      webSocketFactory: () => new SockJS(WS_URL) as any,
      reconnectDelay: 4000,
      onConnect: () => {
        setConnected(true);
        client.subscribe(topic, (message) => {
          try {
            onMessageRef.current(JSON.parse(message.body) as T);
          } catch {
            // ignore malformed messages
          }
        });
      },
      onDisconnect: () => setConnected(false),
      onWebSocketError: () => setConnected(false),
      onStompError: () => setConnected(false),
    });

    client.activate();
    return () => {
      client.deactivate();
    };
  }, [topic]);

  return { connected };
}
