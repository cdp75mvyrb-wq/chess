package handler;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;
import service.ClearService;

public class ClearHandler {

    ClearService clearService;

    public ClearHandler(ClearService clearService) {this.clearService = clearService;}

    public void clear(Context ctx) throws DataAccessException {
        clearService.clear();
        ctx.status(200);
        ctx.result("{}");
    }
}
