package com.example;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class Main extends UntypedActor {
	
	public static void main(String args[]) {
		ActorSystem sys = ActorSystem.create();
		ActorRef dbActor = sys.actorOf(Props.create(DbActor.class));
		ActorRef main = sys.actorOf(Props.create(Main.class));
		dbActor.tell(new FetchRecords(), main);
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof OperationCompleted) {
			System.out.println("Success.");
		}
	}
}
