package events;

public enum EventType {
	TERMINATE,
	GOTO,
	NEWLEADER, 
	HELLO, 
	ANSWER, 
	JOIN,
	NACK,
	ACK,
	SLIDEPART,
	HELLOREPLY,
	ACK_EVENT, 
	REQUEST_TO_JOIN, 
	CRASH, 
	ALIVE, 
	ELECT, 
	STOP, 
	COORDINATOR,
	END_SLIDES,
	START_SESSION;
}
