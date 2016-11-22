package org.workspace7.utils.cli;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class MainVerticle2 extends AbstractVerticle {

	@Override
	public void start(Future<Void> startFuture) throws Exception {
		vertx.createHttpServer()
				.requestHandler(req -> req.response().end("Hello World 2!"))
				.listen(8080,"host");
	}
}