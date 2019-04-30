package org.woehlke.simpleworklist.oodm.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.*;
import javax.persistence.Index;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.search.annotations.*;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.woehlke.simpleworklist.oodm.entities.impl.AuditModel;
import org.woehlke.simpleworklist.oodm.entities.impl.ComparableById;

@Entity
@Table(
    name="project",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "ux_project",
            columnNames = {"uuid", "parent_id", "context_id"}
        )
    }, indexes = {
        @Index(name = "ix_project_uuid", columnList = "uuid"),
        @Index(name = "ix_project_row_created_at", columnList = "row_created_at")
    }
)
@Indexed
public class Project extends AuditModel implements Serializable, ComparableById<Project> {

    private static final long serialVersionUID = 4566653175832872422L;

    @Id
    @GeneratedValue(generator = "project_generator")
    @SequenceGenerator(
        name = "project_generator",
        sequenceName = "project_sequence",
        initialValue = 1000
    )
    @DocumentId(name="id")
    private Long id;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = true,
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.REFRESH
            }
    )
    @JoinColumn(name = "parent_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Project parent;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false,
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.REFRESH
            }
    )
    @JoinColumn(name = "context_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Context context;

    @SafeHtml(whitelistType=SafeHtml.WhiteListType.NONE)
    @NotBlank
    @Length(min=1,max=255)
    @Column(name="name",nullable = false)
    @Field(index= org.hibernate.search.annotations.Index.YES, analyze= Analyze.YES, store= Store.NO)
    private String name;

    //@SafeHtml(whitelistType= SafeHtml.WhiteListType.RELAXED)
    @NotBlank
    @Length(min=0,max=65535)
    @Column(name="description", nullable = true, length = 65535, columnDefinition="text")
    @Field(index= org.hibernate.search.annotations.Index.YES, analyze= Analyze.YES, store= Store.NO)
    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = { CascadeType.ALL })
    private List<Project> children = new ArrayList<>();

    @Transient
    public boolean hasNoChildren() {
        return this.children.size() == 0;
    }

    @Transient
    public boolean isRootProject() {
        return this.parent == null;
    }

    @Transient
    public boolean hasUser(UserAccount thisUser) {
        return (this.getContext().hasThisUser(thisUser));
    }

    @Transient
    @Override
    public boolean equalsById(Project otherObject) {
        return (this.getId().longValue() == otherObject.getId().longValue());
    }

    @Transient
    @Override
    public boolean equalsByUniqueConstraint(Project otherObject) {
        boolean okParent;
        if(this.isRootProject()){
            okParent = (otherObject.isRootProject());
        } else {
            okParent = this.getParent().equalsByUniqueConstraint(otherObject.getParent());
        }
        boolean okContext = this.getContext().equalsByUniqueConstraint(otherObject.getContext());
        boolean okUuid = this.equalsByUuid(otherObject);
        return okParent && okContext && okUuid;
    }

    @Transient
    @Override
    public boolean equalsByUuid(Project otherObject) {
        return super.equalsByMyUuid(otherObject);
    }

    public static Project newProjectFactory(Project parent) {
        Project n = new Project();
        n.setParent(parent);
        n.setContext(parent.getContext());
        return n;
    }

    public static Project newRootProjectFactory(UserAccount userAccount) {
        Project n = new Project();
        n.setParent(null);
        return n;
    }

    public static Project newRootProjectFactory(UserAccount userAccount,Context context) {
        Project n = new Project();
        n.setParent(null);
        n.setContext(context);
        return n;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Project getParent() {
        return parent;
    }

    public void setParent(Project parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Project> getChildren() {
        return children;
    }

    public void setChildren(List<Project> children) {
        this.children = children;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Project)) return false;
        if (!super.equals(o)) return false;
        Project project = (Project) o;
        return Objects.equals(getId(), project.getId()) &&
                Objects.equals(getParent(), project.getParent()) &&
                getContext().equals(project.getContext()) &&
                getName().equals(project.getName()) &&
                getDescription().equals(project.getDescription()) &&
                getChildren().equals(project.getChildren());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getId(), getParent(), getContext(), getName(), getDescription(), getChildren());
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", parent=" + parent +
                ", context=" + context +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", uuid='" + uuid + '\'' +
                ", rowCreatedAt=" + rowCreatedAt +
                ", rowUpdatedAt=" + rowUpdatedAt +
                '}';
    }

}
