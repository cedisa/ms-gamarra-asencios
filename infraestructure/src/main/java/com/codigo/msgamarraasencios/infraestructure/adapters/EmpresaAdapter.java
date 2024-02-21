package com.codigo.msgamarraasencios.infraestructure.adapters;

import com.codigo.msgamarraasencios.domain.aggregates.constants.Constants;
import com.codigo.msgamarraasencios.domain.aggregates.dto.EmpresaDTO;
import com.codigo.msgamarraasencios.domain.aggregates.request.RequestEmpresa;
import com.codigo.msgamarraasencios.domain.aggregates.response.ResponseSunat;
import com.codigo.msgamarraasencios.domain.ports.out.EmpresaServiceOut;
import com.codigo.msgamarraasencios.infraestructure.entity.EmpresaEntity;
import com.codigo.msgamarraasencios.infraestructure.entity.TipoDocumentoEntity;
import com.codigo.msgamarraasencios.infraestructure.entity.TipoEmpresaEntity;
import com.codigo.msgamarraasencios.infraestructure.mapper.EmpresaMapper;
import com.codigo.msgamarraasencios.infraestructure.repository.EmpresaRepository;
import com.codigo.msgamarraasencios.infraestructure.repository.TipoDocumentoRepository;
import com.codigo.msgamarraasencios.infraestructure.repository.TipoEmpresaRepository;
import com.codigo.msgamarraasencios.infraestructure.rest.client.ClienteSunat;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmpresaAdapter implements EmpresaServiceOut {

    private final EmpresaRepository empresaRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final TipoEmpresaRepository tipoEmpresaRepository;
    private final ClienteSunat sunat;
    private final EmpresaMapper empresaMapper;

    @Value("${token.api}")
    private String tokenApi;

    @Override
    public EmpresaDTO crearEmpresaOut(RequestEmpresa requestEmpresa) {
        ResponseSunat responseSunat = getInfo(requestEmpresa.getNumDoc());
        empresaRepository.save(getEntity(responseSunat,requestEmpresa));
        return empresaMapper.mapToDto(getEntity(responseSunat,requestEmpresa));
    }

    @Override
    public Optional<EmpresaDTO> obtenerEmpresaOut(String numDoc) {
        return Optional.of(empresaMapper.mapToDto(empresaRepository.findByNumDocu(numDoc)));
    }

    @Override
    public List<EmpresaDTO> obtenerTodosOut() {
        List<EmpresaDTO> empresaDTOS = new ArrayList<>();
        List<EmpresaEntity> entityList = empresaRepository.findByEstado(Constants.STATUS_ACTIVE);
        for(EmpresaEntity entity : entityList){
            EmpresaDTO empresaDTO = empresaMapper.mapToDto(entity);
            empresaDTOS.add(empresaDTO);
        }
        return empresaDTOS;
    }

    @Override
    public EmpresaDTO actualizarOut(Long id, RequestEmpresa requestEmpresa) {
        boolean existe = empresaRepository.existsById(id);
        if(existe){
            Optional<EmpresaEntity> empresaRecuperada = empresaRepository.findById(id);
            ResponseSunat responseSunat = getInfo(requestEmpresa.getNumDoc());
            empresaRepository.save(getEntityUpdate(responseSunat,empresaRecuperada.get()));
            return empresaMapper.mapToDto(getEntityUpdate(responseSunat, empresaRecuperada.get()));
        }
        return null;
    }

    @Override
    public EmpresaDTO deleteOut(Long id) {
        boolean existe = empresaRepository.existsById(id);
        if(existe){
            Optional<EmpresaEntity> empresaRecuperada = empresaRepository.findById(id);
            empresaRecuperada.get().setEstado(Constants.STATUS_INACTIVE);
            empresaRecuperada.get().setUsuaDelet(Constants.AUDIT_ADMIN);
            empresaRecuperada.get().setDateDelet(getTimestamp());
            empresaRepository.save(empresaRecuperada.get());
            return empresaMapper.mapToDto(empresaRecuperada.get());
        }
        return null;
    }
    private ResponseSunat getInfo(String numero){
        String autho = "Bearer "+tokenApi;
        return sunat.getInfoSunat(numero,autho);
    }

    private EmpresaEntity getEntity(ResponseSunat sunat, RequestEmpresa requestEmpresa){
        TipoDocumentoEntity tipoDocumento = tipoDocumentoRepository.findByCodTipo(requestEmpresa.getTipoDoc());
        TipoEmpresaEntity tipoEmpresa = tipoEmpresaRepository.findByCodTipo(requestEmpresa.getTipoEmp());
        EmpresaEntity entity = new EmpresaEntity();
        entity.setNumDocu(sunat.getNumeroDocumento());
        entity.setRazonSocial(sunat.getRazonSocial());
        entity.setEstado(Constants.STATUS_ACTIVE);
        entity.setUsuaCrea(Constants.AUDIT_ADMIN);
        entity.setDateCreate(getTimestamp());
        entity.setTipoDocumento(tipoDocumento);
        entity.setTipoEmpresa(tipoEmpresa);
        return entity;
    }

    private EmpresaEntity getEntityUpdate(ResponseSunat sunat, EmpresaEntity empresaActualizar){
        empresaActualizar.setRazonSocial(sunat.getRazonSocial());
        empresaActualizar.setNumDocu(sunat.getNumeroDocumento());
        empresaActualizar.setUsuaModif(Constants.AUDIT_ADMIN);
        empresaActualizar.setDateModif(getTimestamp());
        return empresaActualizar;
    }

    private Timestamp getTimestamp(){
        long currentTime = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(currentTime);
        return timestamp;
    }
}
