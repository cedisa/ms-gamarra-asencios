package com.codigo.msgamarraasencios.infraestructure.repository;

import com.codigo.msgamarraasencios.infraestructure.entity.TipoEmpresaEntity;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoEmpresaRepository extends JpaRepository<TipoEmpresaEntity, Long> {
    TipoEmpresaEntity findByCodTipo(@Param("codTipo") String codTipo);
}
