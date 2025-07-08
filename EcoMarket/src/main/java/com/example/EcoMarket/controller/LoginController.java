package com.example.EcoMarket.controller;

import com.example.EcoMarket.model.LoginModel;
import com.example.EcoMarket.service.LoginService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/logins")
@Tag(name = "Login", description = "Operaciones relacionadas con usuarios login")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginServices) {
        this.loginService = loginServices;
    }

    @Operation(summary = "Obtener todos los logins")
    @ApiResponse(responseCode = "200", description = "Lista de logins obtenida correctamente")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<LoginModel>>> getAllLogins() {
        List<LoginModel> logins = loginService.obtenerTodosLosLogins();

        List<EntityModel<LoginModel>> loginModels = logins.stream()
                .map(login -> EntityModel.of(login,
                        linkTo(methodOn(LoginController.class).getLoginByRut(login.getRut())).withSelfRel(),
                        linkTo(methodOn(LoginController.class).getAllLogins()).withRel("all-logins")))
                .collect(Collectors.toList());

        return new ResponseEntity<>(
                CollectionModel.of(loginModels,
                        linkTo(methodOn(LoginController.class).getAllLogins()).withSelfRel()),
                HttpStatus.OK);
    }

    @Operation(summary = "Obtener login por RUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login encontrado"),
            @ApiResponse(responseCode = "404", description = "Login no encontrado")
    })
    @GetMapping("/{rut}")
    public ResponseEntity<EntityModel<LoginModel>> getLoginByRut(@PathVariable String rut) {
        Optional<LoginModel> login = loginService.buscarLoginPorRut(rut);
        return login.map(value -> EntityModel.of(value,
                        linkTo(methodOn(LoginController.class).getLoginByRut(rut)).withSelfRel(),
                        linkTo(methodOn(LoginController.class).getAllLogins()).withRel("all-logins")))
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Crear nuevo login")
    @ApiResponse(responseCode = "201", description = "Login creado correctamente")
    @PostMapping
    public ResponseEntity<EntityModel<LoginModel>> createLogin(@RequestBody LoginModel login) {
        LoginModel savedLogin = loginService.guardarLogin(login);
        EntityModel<LoginModel> resource = EntityModel.of(savedLogin,
                linkTo(methodOn(LoginController.class).getLoginByRut(savedLogin.getRut())).withSelfRel(),
                linkTo(methodOn(LoginController.class).getAllLogins()).withRel("all-logins"));
        return new ResponseEntity<>(resource, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Login no encontrado")
    })
    @PutMapping
    public ResponseEntity<EntityModel<LoginModel>> updateLogin(@RequestBody LoginModel login) {
        LoginModel updatedLogin = loginService.actualizarLogin(login);
        if (updatedLogin != null) {
            EntityModel<LoginModel> resource = EntityModel.of(updatedLogin,
                    linkTo(methodOn(LoginController.class).getLoginByRut(updatedLogin.getRut())).withSelfRel(),
                    linkTo(methodOn(LoginController.class).getAllLogins()).withRel("all-logins"));
            return new ResponseEntity<>(resource, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Eliminar login por RUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Login eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Login no encontrado")
    })
    @DeleteMapping("/{rut}")
    public ResponseEntity<Void> deleteLogin(@PathVariable String rut) {
        boolean deleted = loginService.eliminarLogin(rut);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
