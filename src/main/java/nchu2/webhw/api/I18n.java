package nchu2.webhw.api;

import lombok.Data;
import nchu2.webhw.properites.Flags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

@Path("i18n")
@RestController
@PropertySource("classpath:flags.properties")
public class I18n {
    private final MessageSource messageSource;
    private final Flags flags;

    @Autowired
    public I18n(MessageSource messageSource, Flags flags) {
        this.messageSource = messageSource;
        this.flags = flags;
    }

    @POST
    @Path("tableCol/{tableName}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public BootstrapTableCol[] bootstrapTableCol(@PathParam("tableName") String tableName, BootstrapTableCol[] cols) {
        List list = new LinkedList();
        for (BootstrapTableCol col : cols) {
            col.setTitle(messageSource.getMessage(col.field, null, Locale.getDefault()));
            Flags.Field colFlag = flags.getFieldFlag(tableName, col.field);
            col.setVisible(colFlag.visibility);
        }
        return cols;
    }

    @Data
    static class BootstrapTableCol {
        String field;
        String title;
        boolean sortable;
        boolean visible;
    }
}
