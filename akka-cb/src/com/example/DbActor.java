package com.example;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;

import akka.actor.UntypedActor;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class DbActor extends UntypedActor {

	private final Cluster cluster = CouchbaseCluster.create("192.168.56.101", "192.168.56.102");
	private final Bucket bucket = cluster.openBucket("bucket");

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof FetchRecords) {
			System.out.println("Fetching records.");

			bucket.async().get("walter").flatMap(loaded -> {
				return bucket.async().replace(loaded);
			}).subscribe(record -> System.out.println("Record: " + record.content().toString()));

			getSender().tell(new OperationCompleted("Records fetched successfully."), getSelf());
		} else if (message instanceof InsertRecords) {
			System.out.println("Inserting records.");

			JsonObject user = JsonObject.empty().put("firstname", "Walter").put("lastname", "White")
					.put("job", "chemistry teacher").put("age", 50);
			JsonDocument doc = JsonDocument.create("walter", user);
			JsonDocument response = bucket.upsert(doc);
			getSender().tell(new OperationCompleted("Records inserted successfully."), getSelf());
		}
	}

}
