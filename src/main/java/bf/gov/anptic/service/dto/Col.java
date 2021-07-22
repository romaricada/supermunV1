package bf.gov.anptic.service.dto;

import bf.gov.anptic.domain.enumeration.ObjectType;

public class Col {
    private String value;
    private Long id;
    private ObjectType typeObjet;
    private ObjectType unite;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ObjectType getTypeObjet() {
        return typeObjet;
    }

    public void setTypeObjet(ObjectType typeObjet) {
        this.typeObjet = typeObjet;
    }

    public ObjectType getUnite() {
        return unite;
    }

    public void setUnite(ObjectType unite) {
        this.unite = unite;
    }
}
