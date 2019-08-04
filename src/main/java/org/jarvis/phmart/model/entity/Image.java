package org.jarvis.phmart.model.entity;

import com.vaadin.flow.server.StreamResource;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.jarvis.orm.hibernate.domain.base.AnyEntity;
import org.jarvis.vaadin.util.StreamResourceUtil;

import javax.persistence.*;

/**
 * Created: kim chheng
 * Date: 19-Jan-2019 Sat
 * Time: 4:40 PM
 */
@Entity
@Table(name = "ph_image")
public class Image extends AnyEntity<Integer> {

    private static final long serialVersionUID = -1673744745000510267L;
    private Boolean isProfile;
    private String name;
    private String path;
    private String mimeType;
    private String description;
    private Product product;
    private byte[] bytes;
    private String url;

    public Image() {
    }

    public Image(String name) {
        this(name, null);
    }

    public Image(String name, String path) {
        this(name, path, null);
    }

    public Image(String name, String path, String mimeType) {
        this(name, path, name, new byte[0]);
    }

    public Image(String name, String path, String mimeType, byte[] bytes) {
        this(name, path, mimeType, bytes, false);
    }

    public Image(String name, String path, String mimeType, byte[] bytes, boolean isProfile) {
        this.name = name;
        this.path = path;
        this.mimeType = mimeType;
        this.bytes = bytes;
        this.isProfile = isProfile;
    }

    @Id
    @SequenceGenerator(name = "imageSeq", sequenceName = "ph_image_img_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "imageSeq", strategy = GenerationType.SEQUENCE)
    @Column(name = "img_id", nullable = false, unique = true)
    @Override
    public Integer getId() {
        return id;
    }

    @Column(name = "img_name", length = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "img_path")
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Column(name = "img_description", length = 150)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "img_mime_type", length = 20)
    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pro_id")
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Column(name = "img_bytes")
    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    @Column(name = "img_profile")
    public Boolean isProfile() {
        return isProfile == null ? false : isProfile;
    }

    public void setProfile(Boolean profile) {
        isProfile = profile;
    }

    @Transient
    public StreamResource getStreamResource() {
        return bytes == null ? null :
                StreamResourceUtil.toStreamResource(name, bytes);
    }

    @Transient
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
