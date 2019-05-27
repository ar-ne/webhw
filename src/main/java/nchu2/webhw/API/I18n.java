package nchu2.webhw.API;

import lombok.Data;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Locale;

@Path("i18n")
public class I18n extends APIBase {
    private final MessageSource messageSource;

    public I18n(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @POST
    public I18nProp[] translate(I18nProp[] props) {
        for (I18nProp prop : props) {
            prop.setTrans(messageSource.getMessage(prop.key, null, Locale.getDefault()));
        }
        return props;
    }

    @Data
    static class I18nProp {
        String key;
        String trans;
    }
}
