package nchu2.webhw.model;

import java.io.Serializable;

public abstract class User implements Serializable {
    public abstract User setId(Long id);

    public abstract String getName();
}
