package nchu2.webhw.model;

import nchu2.webhw.model.tables.pojos.Login;
import nchu2.webhw.service.LoginService;

import java.io.Serializable;

public abstract class User implements Serializable {
    public abstract User setId(Long id);

    public abstract String getName();
}
