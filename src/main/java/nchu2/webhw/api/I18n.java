package nchu2.webhw.api;

import lombok.Data;
import nchu2.webhw.properties.Flags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * 翻译字符串
 */
@Path("i18n")
@RestController
@PropertySource("classpath:flags.properties")
public class I18n {
    private final MessageSource messageSource; //在代码里获取Messages
    private final Flags flags;

    @Autowired
    public I18n(MessageSource messageSource, Flags flags) {
        this.messageSource = messageSource;
        this.flags = flags;
    }

    /**
     * 表格列名翻译和可见性
     *
     * @param tableName 表格名
     * @param cols      列
     * @return 填充的信息列
     */
    @POST
    @Path("tableCol/{tableName}")
    @Produces(MediaType.APPLICATION_JSON) //返回类型
    @Consumes(MediaType.APPLICATION_JSON) //输入类型
    public BootstrapTableCol[] bootstrapTableCol(@PathParam("tableName") String tableName, BootstrapTableCol[] cols) {
        List<BootstrapTableCol> list = new LinkedList<>();
        for (BootstrapTableCol col : cols) {
            try {
                col.setTitle(messageSource.getMessage(tableName + "." + col.field, null, Locale.getDefault()));
                Flags.Field colFlag = flags.getFieldFlag(tableName, col.field);
                col.setVisible(colFlag.visibility);
                list.add(col);
            } catch (NoSuchMessageException ignored) {
            }
        }
        return list.toArray(new BootstrapTableCol[0]);
    }

    /**
     * 列属性
     */
    @Data
    static class BootstrapTableCol {
        String field;
        String title;
        boolean sortable;
        boolean visible;
        boolean checkbox;
    }
}
