package com.example.afaloan.controllers.processes

import com.example.afaloan.controllers.processes.ProcessController.Companion.ROOT_URI
import com.example.afaloan.controllers.processes.dtos.CreateProcessRequest
import com.example.afaloan.controllers.processes.dtos.CreateProcessResponse
import com.example.afaloan.controllers.processes.dtos.ProcessDto
import com.example.afaloan.controllers.processes.dtos.ProcessView
import com.example.afaloan.mappers.ProcessMapper
import com.example.afaloan.services.ProcessService
import com.example.afaloan.utils.logger
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping(ROOT_URI)
class ProcessController(
    private val processMapper: ProcessMapper,
    private val processService: ProcessService
) {

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun find(@PathVariable id: UUID): ProcessDto {
        logger.trace { "Find process by id - $id" }
        val process = processService.find(id)
        return processMapper.convertToDto(process)
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun findPage(
        @Schema(hidden = true) @PageableDefault(size = DEFAULT_PAGE_SIZE) pageable: Pageable
    ): Page<ProcessView> {
        logger.trace { "Find page process's with - № ${pageable.pageNumber}" }
        return processService.findPage(pageable).map(processMapper::convertToView)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('SUPERVISOR', 'WORKER')")
    fun create(@Valid @RequestBody request: CreateProcessRequest): CreateProcessResponse {
        logger.trace { "Create process request" }
        val process = processMapper.convert(request)
        val processId = processService.create(process)
        return CreateProcessResponse(processId)
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('SUPERVISOR', 'WORKER')")
    fun update(
        @PathVariable id: UUID,
        @Valid @RequestBody dto: ProcessDto
    ): ProcessDto {
        logger.trace { "Update process by id - $id" }
        val process = processMapper.convert(dto)
        val updatedProcess = processService.update(id, process)
        return processMapper.convertToDto(updatedProcess)
    }

    companion object {
        const val ROOT_URI = "/processes"
        const val DEFAULT_PAGE_SIZE = 50
    }
}