package org.woehlke.simpleworklist.context;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.woehlke.simpleworklist.common.AuditModel;
import org.woehlke.simpleworklist.common.ComparableById;
import org.woehlke.simpleworklist.user.account.UserAccount;

import javax.persistence.*;
import javax.persistence.Index;
import java.io.Serializable;
import java.util.Objects;

/**
 * Created by tw on 13.03.16.
 */
@Entity
@Table(
    name="context",
    uniqueConstraints = {
        @UniqueConstraint(
            name="ux_context",
            columnNames = {"uuid", "user_account_id", "name_de", "name_en" }
        )
    },
    indexes={
        @Index(name = "ix_context_uuid", columnList = "uuid"),
        @Index(name = "ix_context_row_created_at", columnList = "row_created_at")
    }
)
public class Context extends AuditModel implements Serializable, ComparableById<Context> {

    private static final long serialVersionUID = -5035732370606951871L;

    @Id
    @GeneratedValue(generator = "context_generator")
    @SequenceGenerator(
            name = "context_generator",
            sequenceName = "context_sequence",
            initialValue = 1000
    )
    private Long id;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false,
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.REFRESH
            })
    @JoinColumn(name = "user_account_id")
    private UserAccount userAccount;

    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    @NotBlank
    @Length(min = 1, max = 255)
    @Column(name = "name_de", nullable = false)
    private String nameDe;

    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    @NotBlank
    @Length(min = 1, max = 255)
    @Column(name = "name_en", nullable = false)
    private String nameEn;

    public Context() {
    }

    public Context(String nameDe, String nameEn) {
        this.nameDe = nameDe;
        this.nameEn = nameEn;
    }

    @Transient
    public boolean hasThisUser(UserAccount userAccount){
        return (this.getUserAccount().getId().longValue()==userAccount.getId().longValue());
    }

    @Transient
    @Override
    public boolean equalsById(Context otherObject) {
        return (this.getId().longValue() == otherObject.getId().longValue());
    }

    @Transient
    @Override
    public boolean equalsByUniqueConstraint(Context otherObject) {
        boolean okUuid = super.equalsByMyUuid(otherObject);
        boolean okUser = (this.getUserAccount().equalsByUniqueConstraint(otherObject.getUserAccount()));
        boolean okNameDe = (this.getNameDe().compareTo(otherObject.getNameDe())==0);
        boolean okNameEn = (this.getNameEn().compareTo(otherObject.getNameEn())==0);
        return okUuid && okUser && okNameDe && okNameEn;
    }

    @Transient
    @Override
    public boolean equalsByUuid(Context otherObject) {
        return super.equalsByMyUuid(otherObject);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public String getNameDe() {
        return nameDe;
    }

    public void setNameDe(String name) {
        this.nameDe = name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Context)) return false;
        if (!super.equals(o)) return false;
        Context context = (Context) o;
        return Objects.equals(getId(), context.getId()) &&
                getUserAccount().equals(context.getUserAccount()) &&
                getNameDe().equals(context.getNameDe()) &&
                getNameEn().equals(context.getNameEn());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getId(), getUserAccount(), getNameDe(), getNameEn());
    }

    @Override
    public String toString() {
        return "Context{" +
                "id=" + id +
                ", userAccount=" + userAccount +
                ", nameDe='" + nameDe + '\'' +
                ", nameEn='" + nameEn + '\'' +
                ", uuid='" + uuid + '\'' +
                ", rowCreatedAt=" + rowCreatedAt +
                ", rowUpdatedAt=" + rowUpdatedAt +
                '}';
    }

}