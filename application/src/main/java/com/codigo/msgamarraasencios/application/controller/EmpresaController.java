package com.codigo.msgamarraasencios.application.controller;

import com.codigo.msgamarraasencios.domain.aggregates.dto.EmpresaDTO;
import com.codigo.msgamarraasencios.domain.aggregates.request.RequestEmpresa;
import com.codigo.msgamarraasencios.domain.ports.in.EmpresaServiceIn;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@OpenAPIDefinition(
        info = @Info(
                title = "API-EMPRESA",
                version = "2.0",
                description = "Mantenimiento de una Empresa"
        )
)
@RestController
@RequestMapping("/v2/empresa")

public class EmpresaController {
    @Autowired
    @Qualifier("empresaServiceImpl")
    private EmpresaServiceIn empresaServiceIn;

    @Autowired
    @Qualifier("empresaServiceImplOther")
    private EmpresaServiceIn empresaServiceInOther;

    @Operation(summary = "Api para crear una Empresa")
    @PostMapping
    public ResponseEntity<EmpresaDTO> registro(@RequestBody RequestEmpresa requestEmpresa){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(empresaServiceIn.crearEmpresaIn(requestEmpresa));
    }

    @Operation(summary = "Api para obtener datos de una Empresa")
    @GetMapping("/{numDoc}")
    public ResponseEntity<EmpresaDTO> obtenerEmpresa(@PathVariable String numDoc){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(empresaServiceIn.obtenerEmpresaIn(numDoc).get());
    }

    @Operation(summary = "Api para obtener datos de todas Empresas Activas")
    @GetMapping()
    public ResponseEntity<List<EmpresaDTO>>obtenerTodos(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(empresaServiceIn.obtenerTodosIn());
    }

    @Operation(summary = "Api para actualziar los datos de una Empresa")
    @PutMapping("/{id}")
    public ResponseEntity<EmpresaDTO>actualizar(@PathVariable Long id, @RequestBody RequestEmpresa requestEmpresa){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(empresaServiceInOther.actualizarIn(id,requestEmpresa));
    }

    @Operation(summary = "Api para Borrar datos de una Empresa, Eliminado Logico Status =0")
    @DeleteMapping("/{id}")
    public ResponseEntity<EmpresaDTO>delete(@PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(empresaServiceInOther.deleteIn(id));
    }


}
