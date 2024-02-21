package com.codigo.msgamarraasencios.domain.impl;

import com.codigo.msgamarraasencios.domain.aggregates.dto.EmpresaDTO;
import com.codigo.msgamarraasencios.domain.aggregates.request.RequestEmpresa;
import com.codigo.msgamarraasencios.domain.ports.in.EmpresaServiceIn;
import com.codigo.msgamarraasencios.domain.ports.out.EmpresaServiceOut;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmpresaServiceImpl implements EmpresaServiceIn {

    @Autowired
    private EmpresaServiceOut empresaServiceOut;

    @Override
    public EmpresaDTO crearEmpresaIn(RequestEmpresa requestEmpresa) {
        return empresaServiceOut.crearEmpresaOut(requestEmpresa);
    }

    @Override
    public Optional<EmpresaDTO> obtenerEmpresaIn(String numDoc) {

        return empresaServiceOut.obtenerEmpresaOut(numDoc);
    }

    @Override
    public List<EmpresaDTO> obtenerTodosIn() {

        return empresaServiceOut.obtenerTodosOut();
    }

    @Override
    public EmpresaDTO actualizarIn(Long id, RequestEmpresa requestEmpresa) {

        return null;
    }

    @Override
    public EmpresaDTO deleteIn(Long id) {

        return null;
    }
}
