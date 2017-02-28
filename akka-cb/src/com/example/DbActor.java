package com.example;

import akka.actor.UntypedActor;

public class DbActor extends UntypedActor {

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof FetchRecords) {
			System.out.println("Fetching records.");
			getSender().tell(new OperationCompleted(), getSelf());
		}
	}

}
