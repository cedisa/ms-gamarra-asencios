package com.codigo.msgamarraasencios.infraestructure.repository;

import com.codigo.msgamarraasencios.infraestructure.entity.EmpresaEntity;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmpresaRepository  extends JpaRepository<EmpresaEntity, Long> {

    EmpresaEntity findByNumDocu(@Param("numDocu") String numDocu);
    List<EmpresaEntity> findByEstado(@Param("estado") Integer estado);
}
