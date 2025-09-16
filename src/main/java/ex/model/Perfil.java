package ex.model;

public enum Perfil {
    ADMIN("admin"),
    USER ("user"), 
    SUPER_ADMIN("super_admin");

    String perfil;

    Perfil(String perfil) {
        this.perfil = perfil;
    }

    public String getPerfil() {
        return perfil;
    }
}
