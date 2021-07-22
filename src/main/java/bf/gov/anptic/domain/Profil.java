package bf.gov.anptic.domain;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Profil.
 */
@Entity
@Table(name = "profil")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Profil extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "profil_name", nullable = false, unique = true)
    private String profilName;

    @NotNull
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "jhi_profil_authority",
        joinColumns = {@JoinColumn(name = "profil_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)
    private Set<Authority> authorities = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProfilName() {
        return profilName;
    }

    public Profil profilName(String profilName) {
        this.profilName = profilName;
        return this;
    }

    public void setProfilName(String profilName) {
        this.profilName = profilName;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public Profil deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }


    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public Profil authorities(Set<Authority> authorities) {
        this.authorities = authorities;
        return this;
    }

    public Profil addAuthority(Authority authority) {
        this.authorities.add(authority);
        return this;
    }

    public Profil removeAuthority(Authority authority) {
        this.authorities.remove(authority);
        return this;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Profil)) {
            return false;
        }
        return id != null && id.equals(((Profil) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Profil{" +
            "id=" + getId() +
            ", profilName='" + getProfilName() + "'" +
            ", deleted='" + isDeleted() + "'" +
            "}";
    }
}
