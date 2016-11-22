package org.workspace7.utils.cli;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class MainVerticle extends AbstractVerticle {

	@Override
	public void start() {
		System.out.println("Hello World 2!");
		vertx.createHttpServer()
				.requestHandler(req -> req.response().end("Hello World 2!"))
				.listen(8040);
	}
}