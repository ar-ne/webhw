package nchu2.webhw.API;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

@Path("i18n")
@RestController
@PropertySource("classpath:flags.properties")
public class I18n {
    private final MessageSource messageSource;

    @Autowired
    public I18n(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @POST
    @Path("tableCol")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public BootstrapTableCol[] bootstrapTableCol(BootstrapTableCol[] cols) {
        List list = new LinkedList();
        for (BootstrapTableCol col : cols) {
            col.setTitle(messageSource.getMessage(col.field, null, Locale.getDefault()));
        }
        return cols;
    }

    @Data
    static class BootstrapTableCol {
        String field;
        String title;
        boolean sortable;
    }
}
