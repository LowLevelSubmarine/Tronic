package com.tronic.bot.rest;
import com.tronic.bot.core.Core;
import com.tronic.bot.rest.requests.BotRequest;
import com.tronic.bot.rest.requests.GuildRequest;
import com.tronic.bot.rest.requests.HelpRequest;
import com.tronic.bot.rest.requests.UserRequest;
import com.tronic.updater.Updater;
import net.tetraowl.watcher.toolbox.JavaTools;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spark.Request;
import spark.Response;

import java.io.File;
import java.net.URI;

import static spark.Spark.*;


public class RestServer  {
    public static final int PORT = 8080;
    private final Core core;
    private final JWTStore jwtStore;
    private final Logger logger = LogManager.getLogger(RestServer.class);

    public RestServer(Core core) {
      this.core = core;
      this.jwtStore = new JWTStore(core.getStorage());
      String ptJar = JavaTools.getJarUrl(RestServer.class);
      port(PORT);
      new CORSFilter().apply();
      get("/ping",this::ping);
      get("/help",new HelpRequest(this.core));
      get("/guild",new GuildRequest(this.core));
      get("/user",new UserRequest(this.core));
      get("/bot", new BotRequest(this.core));
      get("*",this::pageNotFound);
      logger.info("Tronic api started succesfully");
    }

    private Object pageNotFound(Request request, Response response) {
        response.status(404);
        return "";
    }

    private Object ping(Request request, Response response) {
        response.status(200);
        return "";
    }

    public JWTStore getJwtStore() {
        return jwtStore;
    }

}
