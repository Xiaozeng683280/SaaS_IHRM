package com.ihrm.domain.system;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author: hyl
 * @date: 2020/05/25
 **/
@Entity
@Table(name = "pe_user_role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RoleAndUserRelations implements Serializable {

    @Id
    private String id;

    @Column(name = "role_id")
    private String roleId;

    @Column(name = "user_id")
    private String userId;
}
