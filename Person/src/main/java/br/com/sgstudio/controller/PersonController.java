package br.com.sgstudio.controller;

import br.com.sgstudio.data.vo.PersonVO;
import br.com.sgstudio.services.PersonServices;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@Api(value = "Person endpoint", tags = "PersonEndpoint")
@RestController
@RequestMapping("/api/person/v1")
public class PersonController {

    @Autowired
    private PersonServices services;

    @Autowired
    PagedResourcesAssembler<PersonVO> assembler;

    @ApiOperation(value = "Find all people record")
    @GetMapping(produces = { "application/json", "application/xml", "application/x-yaml" })
    public ResponseEntity<?> findAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "15") int limit,
            @RequestParam(value = "direction", defaultValue = "as") String direction) throws Exception {

        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "firstName"));

        Page<PersonVO> personsVO = services.findAll(pageable);
        personsVO.forEach(p -> {
            try {
                p.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class).findById(p.getKey())).withSelfRel());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return new ResponseEntity<>(assembler.toModel(personsVO), HttpStatus.OK);
    }

    @ApiOperation(value = "Find people by first name record")
    @GetMapping(value = "/findPersonByName/{firstName}", produces = { "application/json", "application/xml", "application/x-yaml" })
    public ResponseEntity<?> findByName(
            @PathVariable("firstName") String firstName,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "15") int limit,
            @RequestParam(value = "direction", defaultValue = "as") String direction) throws Exception {

        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "firstName"));

        Page<PersonVO> personsVO = services.findPersonByName(firstName, pageable);
        personsVO.forEach(p -> {
            try {
                p.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class).findById(p.getKey())).withSelfRel());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return new ResponseEntity<>(assembler.toModel(personsVO), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = { "application/json", "application/xml", "application/x-yaml" })
    public PersonVO findById(@PathVariable("id") Long id) throws Exception {
        PersonVO personVO = services.findById(id);
        personVO.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class).findById(id)).withSelfRel());
        return personVO;
    }

    @PostMapping(consumes = { "application/json", "application/xml", "application/x-yaml" }, produces = { "application/json", "application/xml", "application/x-yaml" })
    public PersonVO create(@RequestBody PersonVO person) throws Exception {
        PersonVO personVO =  services.create(person);
        personVO.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class).findById(personVO.getKey())).withSelfRel());
        return personVO;
    }

    @PutMapping(consumes = { "application/json", "application/xml", "application/x-yaml" }, produces = { "application/json", "application/xml", "application/x-yaml" })
    public PersonVO update(@RequestBody PersonVO person) throws Exception {
        PersonVO personVO =  services.update(person);
        personVO.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class).findById(personVO.getKey())).withSelfRel());
        return personVO;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) throws Exception {
        services.delete(id);
        return ResponseEntity.ok().build();
    }

    private Double convertToDouble(String number) {
        number = number.replaceAll(",",".");
        return Double.parseDouble(number);
    }

    private boolean isNumeric(String number) {
        if (number == null)
        {
            return false;
        }
        number = number.replaceAll(",",".");
        return number.matches("[-+]?[0-9]*\\.?[0-9]+");
    }
}
