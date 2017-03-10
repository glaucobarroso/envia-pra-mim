package org.enviapramim.repository;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jmethods.catatumbo.Entity;
import com.jmethods.catatumbo.Identifier;

/**
 * Created by glauco on 10/03/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity(kind = "user")
public class UserMlData {

    @Identifier
    private String username;
    private String mlAccessToken;
    private String mlRefreshToken;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMlAccessToken() {
        return mlAccessToken;
    }

    public void setMlAccessToken(String mlAccessToken) {
        this.mlAccessToken = mlAccessToken;
    }

    public String getMlRefreshToken() {
        return mlRefreshToken;
    }

    public void setMlRefreshToken(String mlRefreshToken) {
        this.mlRefreshToken = mlRefreshToken;
    }

}
