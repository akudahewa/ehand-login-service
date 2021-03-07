package lk.dialog.loginservice.payload;

import lk.dialog.loginservice.model.Module;

import java.util.List;

public class UserSummary {
    private Long id;
    private String username;
    private String name;
    private List<String> privileges;
    private List<Module> modules;

    public UserSummary(Long id, String username, String name,List<String> privileges,
                       List<Module> modules) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.privileges = privileges;
        this.modules = modules;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<String> privileges) {
        this.privileges = privileges;
    }

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }
}
