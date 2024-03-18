package org.fffd.l23o6.pojo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;


@Entity
@Table(name = "user1")  // table name "user" is a reserved word in H2
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String username;
    @NotNull
    private String password;

    private String name;
    private String phone;
    private String type;//证件类型
    private String idn;//身份证号
    private String role;
    private Long mileagePoints;//里程积分

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    //用户是否为会员
    private Boolean isMember;
    private String vippassword;

}
